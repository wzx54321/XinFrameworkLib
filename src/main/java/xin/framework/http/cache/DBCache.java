package xin.framework.http.cache;


import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xin.framework.http.output.BaseOutPut;
import xin.framework.store.box.HttpCacheBox;
import xin.framework.store.entity.EntityHttpCache;
import xin.framework.store.entity.EntityHttpCache_;
import xin.framework.utils.android.Loger.Log;
import xin.framework.utils.common.TypeUtil;

/**
 * 使用 object-box,这里可以替换自己的数据库存储
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class DBCache<T> implements ICache {


    private HttpCacheBox mBox;

    DBCache() {
        mBox = new HttpCacheBox();
    }

    @Override
    public Observable<BaseOutPut<T>> get(final String key, final Class cls) {

        return Observable.create(new ObservableOnSubscribe<BaseOutPut<T>>() {
            @SuppressWarnings("unchecked")
            @Override
            public void subscribe(ObservableEmitter<BaseOutPut<T>> emitter) throws Exception {


                EntityHttpCache entity = mBox.getQueryBuilder().equal(EntityHttpCache_.host, key).build().findFirst();

                if (entity == null || TextUtils.isEmpty(entity.getHost())) {

                    //noinspection unchecked
                    emitter.onNext(BaseOutPut.class.newInstance());

                    Log.i("数据库 没有获取到缓存数据：");
                } else {

                    Type objectType = TypeUtil.type(BaseOutPut.class, cls);
                    BaseOutPut t = new Gson().fromJson(entity.getData(), objectType);

                    Log.i("数据库 获取到缓存数据：" + entity.getData());
                    emitter.onNext(t);
                }
                emitter.onComplete();


            }
        } ).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable subscription) throws Exception {
                Log.i("cache-->load from disk: " + key);
            }
        }).compose(getScheduler());

    }

    @Override
    public void put(final String key, final BaseOutPut t, final Class cls) {

        Observable<BaseOutPut<T>> observable = Observable.create(new ObservableOnSubscribe<BaseOutPut<T>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseOutPut<T>> emitter) throws Exception {
                EntityHttpCache entity = mBox.getQueryBuilder().equal(EntityHttpCache_.host, key).build().findUnique();
                if (entity == null) {
                    entity = new EntityHttpCache();
                    entity.setHost(key);
                }

                entity.setData(new Gson().toJson(t));

                mBox.insert(entity);
                Log.i("存缓存数据：" + entity.getData());
                emitter.onComplete();
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Log.i("cache save to disk: " + key);
            }
        }).compose(getScheduler()).delaySubscription(100, TimeUnit.MILLISECONDS);
        observable.subscribe();


    }


    /**
     * 线程切换
     *
     */
    private ObservableTransformer<BaseOutPut<T>, BaseOutPut<T>> getScheduler() {
        return new ObservableTransformer<BaseOutPut<T>, BaseOutPut<T>>() {
            @Override
            public ObservableSource<BaseOutPut<T>> apply(Observable<BaseOutPut<T>> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };

    }
}
package xin.framework.http.request;


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
public class XinDBRequest<T> {


    private HttpCacheBox mBox;

    public  XinDBRequest() {
        mBox = new HttpCacheBox();
    }


    public Observable<T> get(final String key, final Type objectType) {

        return Observable.create(new ObservableOnSubscribe<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {


                EntityHttpCache entity = mBox.getQueryBuilder().equal(EntityHttpCache_.host, key).build().findFirst();

                if (entity == null || TextUtils.isEmpty(entity.getHost())) {


                    emitter.onNext(((Class<T>) objectType).newInstance());

                    Log.i("数据库 没有获取到缓存数据：");
                } else {
                    Type type = TypeUtil.type(BaseOutPut.class, objectType);
                    BaseOutPut t = new Gson().fromJson(entity.getData(), type);

                    Log.i("数据库 获取到缓存数据：" + entity.getData());
                    emitter.onNext((T) t.getData());
                }
                emitter.onComplete();


            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable subscription) throws Exception {
                Log.i("cache-->load from disk: " + key);
            }
        }).compose(getScheduler());

    }


    public void put(final String key, final BaseOutPut outPut) {

        Observable<BaseOutPut<T>> observable = Observable.create(new ObservableOnSubscribe<BaseOutPut<T>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseOutPut<T>> emitter) {
                EntityHttpCache entity = mBox.getQueryBuilder().equal(EntityHttpCache_.host, key).build().findUnique();
                if (entity == null) {
                    entity = new EntityHttpCache();
                    entity.setHost(key);
                }

                entity.setData(new Gson().toJson(outPut));

                mBox.insert(entity);
                Log.i("存缓存数据：" + entity.getData());
                emitter.onComplete();
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                Log.i("cache save to disk: " + key);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).delaySubscription(100, TimeUnit.MILLISECONDS);
        observable.subscribe();


    }


    /**
     * 线程切换
     */
    private ObservableTransformer<T, T> getScheduler() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };

    }
}
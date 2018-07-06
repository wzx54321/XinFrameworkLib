package xin.framework.http.cache;


import android.text.TextUtils;

import com.google.gson.Gson;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import xin.framework.http.output.BaseOutPut;
import xin.framework.store.box.HttpCacheBox;
import xin.framework.store.entity.EntityHttpCache;
import xin.framework.store.entity.EntityHttpCache_;
import xin.framework.utils.android.Loger.Log;
import xin.framework.utils.common.TypeUtil;

/**
 * 使用 object-box
 */
public class DBCache<T> implements ICache {


    private HttpCacheBox mBox;

    DBCache() {
        mBox = new HttpCacheBox();
    }

    @Override
    public Flowable<BaseOutPut<T>> get(final String key, final Class cls) {

        return Flowable.create(new FlowableOnSubscribe<BaseOutPut<T>>() {
            @SuppressWarnings("unchecked")
            @Override
            public void subscribe(FlowableEmitter<BaseOutPut<T>> emitter) throws Exception {


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
        }, BackpressureStrategy.BUFFER).doOnSubscribe(new Consumer<Subscription>() {
            @Override
            public void accept(Subscription subscription) throws Exception {
                Log.i("cache-->load from disk: " + key);
            }
        }).compose(getScheduler());

    }

    @Override
    public void put(final String key, final BaseOutPut t, final Class cls) {

        Flowable<BaseOutPut<T>> flowable = Flowable.create(new FlowableOnSubscribe<BaseOutPut<T>>() {
            @Override
            public void subscribe(FlowableEmitter<BaseOutPut<T>> emitter) throws Exception {

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
        }, BackpressureStrategy.BUFFER).doOnSubscribe(new Consumer<Subscription>() {
            @Override
            public void accept(Subscription subscription) throws Exception {
                Log.i("cache save to disk: " + key);
            }
        }).compose(getScheduler()).delaySubscription(100, TimeUnit.MILLISECONDS);
        flowable.subscribe();

    }


    /**
     * 线程切换
     *
     * @return
     */
    private FlowableTransformer<BaseOutPut<T>, BaseOutPut<T>> getScheduler() {
        return new FlowableTransformer<BaseOutPut<T>, BaseOutPut<T>>() {
            @Override
            public Publisher<BaseOutPut<T>> apply(Flowable<BaseOutPut<T>> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
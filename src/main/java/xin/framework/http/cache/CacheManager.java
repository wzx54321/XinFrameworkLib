package xin.framework.http.cache;


import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Consumer;
import xin.framework.http.output.BaseOutPut;
import xin.framework.utils.android.Loger.Log;


/**
 * 缓存管理
 * 作者：xin on 2018/6/7 0007 17:40
 * <p>
 * 邮箱：ittfxin@126.com <p>
 * @param <T>
 */
public class CacheManager<T> {


    private final DBCache<T> mDBCache;

    private CacheManager() {
        mDBCache = new DBCache<>();
    }

    public static   CacheManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Flowable<BaseOutPut<T>> load(String key, Class<T> cls, NetworkCache<T> networkCache) {

        return Flowable.concat(
                loadFromDB(key, cls),
                loadFromNetwork(key, cls, networkCache));

    }


    private Flowable<BaseOutPut<T>> loadFromDB(String key, Class<T> cls) {

        FlowableTransformer<BaseOutPut<T>, BaseOutPut<T>> transformer =
                log("load from disk: " + key);

        return mDBCache.get(key, cls).compose(transformer);
    }

    private Flowable<BaseOutPut<T>> loadFromNetwork(final String key, final Class<T> cls
            , NetworkCache<T> networkCache) {
        FlowableTransformer<BaseOutPut<T>, BaseOutPut<T>> transformer = log("load from network: " + key);

        return networkCache.get(key, cls)
                .compose(transformer)
                .doOnNext(new Consumer<BaseOutPut<T>>() {
                    @Override
                    public void accept(BaseOutPut<T> t) throws Exception {
                        if (null != t) {
                            mDBCache.put(key, t, cls);

                        }
                    }
                });

    }

    private FlowableTransformer<BaseOutPut<T>, BaseOutPut<T>> log(final String msg) {
        return new FlowableTransformer<BaseOutPut<T>, BaseOutPut<T>>() {

            @Override
            public Publisher<BaseOutPut<T>> apply(Flowable<BaseOutPut<T>> upstream) {
                Log.v(msg);
                return upstream;
            }
        };
    }

    private static final class LazyHolder {
        static final CacheManager INSTANCE = new CacheManager();
    }


}
package xin.framework.http.cache;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import xin.framework.http.output.BaseOutPut;
import xin.framework.utils.android.Loger.Log;


/**
 * 缓存管理
 * 作者：xin on 2018/6/7 0007 17:40
 * <p>
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 * @param <T>
 */
public class CacheManager<T> {


    private final DBCache<T> mDBCache;

    public CacheManager() {
        mDBCache = new DBCache<>();
    }

    public static   CacheManager  getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Observable<BaseOutPut<T>> load(String key, Class<T> cls, NetworkCache<T> networkCache) {

        return Observable.concat(
                loadFromDB(key, cls),
                loadFromNetwork(key, cls, networkCache));

    }


    private Observable<BaseOutPut<T>> loadFromDB(String key, Class<T> cls) {

        ObservableTransformer<BaseOutPut<T>, BaseOutPut<T>> transformer =
                log("load from disk: " + key);

        return mDBCache.get(key, cls).compose(transformer);
    }

    private Observable<BaseOutPut<T>> loadFromNetwork(final String key, final Class<T> cls
            , NetworkCache<T> networkCache) {
        ObservableTransformer<BaseOutPut<T>, BaseOutPut<T>> transformer = log("load from network: " + key);

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

    private ObservableTransformer<BaseOutPut<T>, BaseOutPut<T>> log(final String msg) {


        return new ObservableTransformer<BaseOutPut<T>, BaseOutPut<T>>() {
            @Override
            public ObservableSource<BaseOutPut<T>> apply(Observable<BaseOutPut<T>> upstream) {
                Log.v(msg);
                return upstream;
            }
        };

    }

    private static final class LazyHolder {
        static final CacheManager INSTANCE = new CacheManager<>();
    }


}
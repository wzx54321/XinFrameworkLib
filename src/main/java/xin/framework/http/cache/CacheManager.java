package xin.framework.http.cache;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import xin.framework.http.output.BaseOutPut;
import xin.framework.http.request.XinRequest;
import xin.framework.http.func.OutputFunction;
import xin.framework.http.request.XinDBRequest;
import xin.framework.utils.android.Loger.Log;


/**
 * 缓存管理
 * 作者：xin on 2018/6/7 0007 17:40
 * <p>
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 *
 * @param <T>
 */
public class CacheManager<T> {


    private XinDBRequest<T> mDBCache;

    CacheManager() {
        mDBCache = new XinDBRequest<>();
    }

    private static final class LazyHolder {
        static final CacheManager INSTANCE = new CacheManager<>();
    }


    public static CacheManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Observable<T> load(XinRequest<T> xinRequest) {

        return Observable.concat(
                loadFromDB(xinRequest),
                loadFromNetwork(xinRequest));

    }


    private Observable<T> loadFromDB(XinRequest<T> xinRequest) {


        return mDBCache.get(xinRequest.cachekey, xinRequest.rspClazz);
    }

    private Observable<T> loadFromNetwork(final XinRequest<T> xinRequest) {
        ObservableTransformer<BaseOutPut<T>, BaseOutPut<T>> transformer = log("load from network: " + xinRequest.cachekey);

        return xinRequest.reqObservable
                .compose(transformer).compose(xinRequest.<T>apiTransformer())
                .doOnNext(new Consumer<BaseOutPut<T>>() {
                    @Override
                    public void accept(BaseOutPut<T> output) {
                        if (null != output) {
                            mDBCache.put(xinRequest.cachekey, output);

                        }
                    }
                }).map(new OutputFunction<T>());

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


}
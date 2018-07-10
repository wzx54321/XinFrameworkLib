package xin.framework.http;


import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import xin.framework.http.cache.CacheManager;
import xin.framework.http.cache.NetworkCache;
import xin.framework.http.helper.HttpHelper;
import xin.framework.http.listener.ResultListener;
import xin.framework.http.output.BaseOutPut;

/**
 * <li>Description:网络请求封装
 * <li>Created by xin on 2018/6/14.
 * Net.requestWithCache(CacheKeyConfig.HOME_KEY, HomePageBean.class
 * , new NetworkRequest<>(Net.getApiManager(url).getHomePageData(map)), new ResultListener() {
 *
 * @Override public void onLoadSuccess(BaseOutPut o) {
 * HomePageBean data = (HomePageBean) o.getData();
 * Log.e(data.getAppGroups().get(0).getAppGroupId() + "请求成功net家缓存工具" + data.getVersion());
 * getV().showData(data);
 * }
 * });
 * Net.request(Net.getApiManager(url).getHomePageData(map), new ResultListener() {
 * @Override public void onLoadSuccess(BaseOutPut o) {
 * HomePageBean data = (HomePageBean) o.getData();
 * Log.e(data.getAppGroups().get(0).getAppGroupId() + "请求成功net工具" + data.getVersion());
 * getV().showData(data);
 * }
 * });
 */

public class Net {


    public static <V> V getApiManager(String url, Class<V> server) {

        return HttpHelper.getInstance().getRetrofit(url).create(server);
    }

    /**
     * @param observable 请求的Observable
     * @param listener   请求结果回调
     * @param <T>        具体的类的泛型
     */
    public static <T> void request(Observable<T> observable, final ResultListener listener) {
        request(observable, null, listener);
    }


    /**
     * @param observable           请求的Observable
     * @param lifecycleTransformer 生命周期管理
     * @param listener             请求结果回调
     * @param <T>                  具体的类的泛型
     */
    public static <T> void request(Observable<T> observable, LifecycleTransformer<T> lifecycleTransformer, final ResultListener listener) {

        if (lifecycleTransformer != null) {
            observable = observable.compose(lifecycleTransformer);
        }
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T t) {
                        if (null != listener) {
                            listener.onLoadCompleted(t);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * @param cacheKey             缓存的key
     * @param cls                  返回 T类型的具体的类型
     * @param lifecycleTransformer 生命周期管理
     * @param networkCache         网络请求
     * @param listener             请求结果回调
     * @param <T>                  具体的类的泛型
     */

    public static <T> void requestWithCache(String cacheKey, Class<T> cls,
                                            LifecycleTransformer<BaseOutPut<T>> lifecycleTransformer,
                                            NetworkCache<T> networkCache, final ResultListener listener) {
        Observable<BaseOutPut<T>> observable = new CacheManager<T>().load(cacheKey, cls, networkCache);
        if (lifecycleTransformer != null) {
            observable = observable.compose(lifecycleTransformer);
        }
        observable.subscribe(new Observer<BaseOutPut<T>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseOutPut<T> t) {
                if (null != listener) {
                    listener.onLoadCompleted(t);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    /**
     * @param cacheKey     缓存的key
     * @param cls          返回 T类型的具体的类型
     * @param networkCache 网络请求
     * @param listener     请求结果回调
     * @param <T>          具体的类的泛型
     */
    public static <T> void requestWithCache(String cacheKey, Class<T> cls,
                                            NetworkCache<T> networkCache, final ResultListener listener) {
        requestWithCache(cacheKey, cls, null, networkCache, listener);
    }
}

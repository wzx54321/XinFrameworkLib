package xin.framework.http;


import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import xin.framework.http.cache.CacheManager;
import xin.framework.http.cache.NetworkCache;
import xin.framework.http.helper.HttpHelper;
import xin.framework.http.listener.ResultListener;

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
     *
     * @param flowable 请求的flowable
     * @param lifecycleTransformer  生命周期管理
     * @param listener    请求结果回调
     * @param <T> 具体的类的泛型
     */
    public static <T> void request(Flowable<T> flowable, LifecycleTransformer<T> lifecycleTransformer, final ResultListener listener) {
        flowable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).compose(lifecycleTransformer).
                subscribe(new ResourceSubscriber<T>() {
                    @Override
                    public void onNext(T homePageBeanBaseOutPut) {
                        if (null != listener) {
                            listener.onLoadCompleted(homePageBeanBaseOutPut);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

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
    @SuppressWarnings("unchecked")
    public static <T> void requestWithCache(String cacheKey, Class<T> cls, LifecycleTransformer lifecycleTransformer, NetworkCache<T> networkCache, final ResultListener listener) {
        Flowable<T> observable = CacheManager.getInstance().load(cacheKey, cls, networkCache).compose(lifecycleTransformer);

        observable.subscribe(new ResourceSubscriber<T>() {


            @Override
            public void onNext(T homePageBean) {
                if (null != listener) {
                    listener.onLoadCompleted(homePageBean);
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


}

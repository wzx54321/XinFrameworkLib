package xin.framework.http.listener;

/**
 * 作者：xin on 2018/6/27 0027 16:06
 * <p>
 * 邮箱：ittfxin@126.com
 */
@SuppressWarnings("WeakerAccess")
public interface OnLoadListener<T> {

    void onLoadCompleted(T t);

    void onLoadFailed(String errMsg);
}

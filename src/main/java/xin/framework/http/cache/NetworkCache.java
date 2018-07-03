package xin.framework.http.cache;


import io.reactivex.Flowable;
import xin.framework.http.output.BaseOutPut;


/**
 * 作者：xin on 2018/6/27 0027 16:06
 * <p>
 * 邮箱：ittfxin@126.com
 */
public abstract class NetworkCache<T> {

    public abstract Flowable<BaseOutPut<T>> get(String key, final Class<T>  cls);
}
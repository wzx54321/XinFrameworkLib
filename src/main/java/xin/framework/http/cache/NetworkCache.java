package xin.framework.http.cache;


import io.reactivex.Observable;
import xin.framework.http.output.BaseOutPut;


/**
 * 作者：xin on 2018/6/27 0027 16:06
 * <p>
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public abstract class NetworkCache<T> {

    public abstract Observable<BaseOutPut<T>> get(String key, final Class<T>  cls);
}
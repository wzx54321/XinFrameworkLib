package xin.framework.http.cache;


import io.reactivex.Flowable;
import xin.framework.http.output.BaseOutPut;


/**
 *   作者：xin on 2018/6/27 0027 16:06
 * <p>
 * 邮箱：ittfxin@126.com
 */
public interface ICache<T> {

    Flowable<BaseOutPut<T>> get(String key, Class cls);

    void put(String key, BaseOutPut<T> t, Class cls);
}

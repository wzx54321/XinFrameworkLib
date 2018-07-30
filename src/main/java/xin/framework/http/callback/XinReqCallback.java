package xin.framework.http.callback;

/**
 * 作者：xin on 2018/7/27 18:47
 * <p>
 * 邮箱：ittfxin@126.com
 */
public interface XinReqCallback<T> {


    public void onSuccess(T rspObj);


    public void  onError(int code ,String details);



}

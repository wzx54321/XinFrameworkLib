package xin.framework.http.callback;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 作者：xin on 2018/7/30 14:35
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class XinRequestObserver<T> implements Observer<T> {
    XinReqCallback<T> reqCallback;

    public XinRequestObserver(XinReqCallback<T> callback) {
        this.reqCallback = callback;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T data) {
        if (reqCallback != null) {
            reqCallback.onSuccess(data);
        }
    }

    @Override
    public void onError(Throwable e) {
        // TODO 错误处理
    }

    @Override
    public void onComplete() {

    }
}
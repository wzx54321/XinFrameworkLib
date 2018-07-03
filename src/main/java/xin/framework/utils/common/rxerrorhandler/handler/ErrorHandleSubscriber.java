package xin.framework.utils.common.rxerrorhandler.handler;


import android.support.annotation.NonNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import xin.framework.utils.common.rxerrorhandler.core.RxErrorHandler;


public abstract class ErrorHandleSubscriber<T> implements Observer<T> {
    private ErrorHandlerFactory mHandlerFactory;

    public ErrorHandleSubscriber(RxErrorHandler rxErrorHandler){
        this.mHandlerFactory = rxErrorHandler.getHandlerFactory();
    }


    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }


    @Override
    public void onComplete() {

    }


    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        mHandlerFactory.handleError(e);
    }
}


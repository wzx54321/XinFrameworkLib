package xin.framework.utils.common.rxerrorhandler.handler.listener;

import android.content.Context;

/**
 * Created by jess on 9/2/16 13:58
 */
public interface ResponseErrorListener {
    void handleResponseError(Context context, Throwable t);

    ResponseErrorListener EMPTY = new ResponseErrorListener() {
        @Override
        public void handleResponseError(Context context, Throwable t) {


        }
    };
}

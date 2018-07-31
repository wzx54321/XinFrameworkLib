package xin.framework.http.func;


import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import xin.framework.utils.android.Loger.Log;

/**
 * 重试
 */
public class RetryFunction implements Function<Observable<? extends Throwable>, Observable<?>> {

    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public RetryFunction(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) {
        return observable
                .flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) {
                        if (++retryCount <= maxRetries && (throwable instanceof SocketTimeoutException
                                || throwable instanceof ConnectException)) {
                            Log.d("  it will try after " + retryDelayMillis + " millisecond, retry count " + retryCount);
                            return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
                        }
                        return Observable.error(throwable);
                    }
                });
    }
}

package xin.framework.utils.common.rxerrorhandler.handler;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import xin.framework.utils.android.Loger.Log;

/**
 * Created by jess on 9/2/16 14:32
 */
public class RetryWithDelay implements
        Function<Observable<Throwable>, ObservableSource<?>> {

    public final String TAG = this.getClass().getSimpleName();
    private final int maxRetries;
    private final int retryDelaySecond;
    private int retryCount;

    public RetryWithDelay(int maxRetries, int retryDelaySecond) {
        this.maxRetries = maxRetries;
        this.retryDelaySecond = retryDelaySecond;
    }

    @Override
    public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {

        return throwableObservable
                .flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                        if (++retryCount <= maxRetries) {
                            // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                            Log.d("get error, it will try after " + retryDelaySecond
                                    + " second, retry count " + retryCount);
                            return Observable.timer(retryDelaySecond,
                                    TimeUnit.SECONDS);
                        }
                        // Max retries hit. Just pass the error along.
                        return Observable.error(throwable);
                    }
                });
    }
}
package xin.framework.utils.common.utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * Function: RxJava使用管理类
 * Description:
 */
public class RxJavaManager {

    public interface TimerListener {
        /**
         * 倒计时结束回调
         */
        void timeEnd();
    }

    private static volatile RxJavaManager instance;

    private RxJavaManager() {
    }

    public static RxJavaManager getInstance() {
        if (instance == null) {
            synchronized (RxJavaManager.class) {
                if (instance == null) {
                    instance = new RxJavaManager();
                }
            }
        }
        return instance;
    }

    /**
     * 创建Observable
     *
     * @param value
     * @param delay
     * @param unit
     * @param <T>
     * @return
     */
    public <T> Observable<T> getDelayObservable(T value, long delay, TimeUnit unit) {
        return Observable.just(value)
                .delay(delay, unit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建 时延单位秒的Observable
     *
     * @param value
     * @param delay
     * @param <T>
     * @return
     */
    public <T> Observable<T> getDelayObservable(T value, long delay) {
        return getDelayObservable(value, delay, TimeUnit.SECONDS);
    }

    /**
     * 设置时延为毫秒的定时器
     *
     * @param delayTime
     * @return
     */
    public Observable<Long> setTimer(long delayTime) {
        return getDelayObservable(delayTime, delayTime, TimeUnit.MILLISECONDS);
    }

}

package xin.framework.http.cache;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import xin.framework.http.output.BaseOutPut;

/**
 * <li>Description:
 * <li>Created by xin on 2018/6/14.
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class NetworkRequest<T> extends NetworkCache {

    private Observable<BaseOutPut<T>> mObservable;

    public NetworkRequest(Observable<BaseOutPut<T>> ob) {
        mObservable = ob;
    }

    @Override
    public Observable<BaseOutPut<T>> get(String key, Class cls) {

        return mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}

package xin.framework.http.cache;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * <li>Description:
 * <li>Created by xin on 2018/6/14.
 */
public class NetworkRequest<T> extends NetworkCache {

    Flowable<T> mFlowable;

    public NetworkRequest(Flowable<T> ob) {
        mFlowable = ob;
    }

    @Override
    public Flowable<T> get(String key, Class cls) {

        return mFlowable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}

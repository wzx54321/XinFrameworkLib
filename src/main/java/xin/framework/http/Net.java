package xin.framework.http;

import android.text.TextUtils;

import xin.framework.http.cache.CacheManager;
import xin.framework.http.callback.XinRequestObserver;
import xin.framework.http.request.XinRequest;

/**
 * 作者：xin on 2018/7/27 18:24
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class Net<T> {
    private XinRequest<T> mXinRequest;


    public Net request(XinRequest<T> xinRequest) {
        mXinRequest = xinRequest;
        return this;
    }

    @SuppressWarnings("unchecked")
    public void OK() {

        XinRequestObserver<T> observer = new XinRequestObserver<>(mXinRequest.reqCallback);

        if (TextUtils.isEmpty(mXinRequest.cachekey)) {
            mXinRequest.reqObservable.compose(mXinRequest.apiTransformerMap()).subscribe(observer);
        } else {
            CacheManager.getInstance().load(mXinRequest).subscribe(observer);
        }

    }


}

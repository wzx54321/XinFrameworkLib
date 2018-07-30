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
public class Net {
    XinRequest mXinRequest;


    public Net request(XinRequest xinRequest) {
        mXinRequest = xinRequest;
        return this;
    }

    public void OK() {

        XinRequestObserver observer = new XinRequestObserver(mXinRequest.reqCallback);
        if (TextUtils.isEmpty(mXinRequest.cachekey)) {
            mXinRequest.reqObservable.compose(mXinRequest.apiTransformerMap()).subscribe(observer);
        } else {
            CacheManager.getInstance().load(mXinRequest).subscribe(observer);
        }

    }


}

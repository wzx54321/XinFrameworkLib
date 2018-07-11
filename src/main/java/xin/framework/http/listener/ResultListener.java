package xin.framework.http.listener;

import xin.framework.http.output.BaseOutPut;

/**
 * <li>Created by xin on 2018/6/13.
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public abstract class ResultListener implements OnLoadListener {


    public abstract void onLoadSuccess(BaseOutPut o);


    @Override
    public void onLoadCompleted(Object o) {
        BaseOutPut rep = (BaseOutPut) o;
        if (rep != null && rep.getMeta() != null) {
            onLoadSuccess(rep);
        }
    }

    @Override
    public void onLoadFailed(String errMsg) {

    }
}

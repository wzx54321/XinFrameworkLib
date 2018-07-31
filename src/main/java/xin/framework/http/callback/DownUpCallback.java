package xin.framework.http.callback;

import android.support.annotation.NonNull;

import xin.framework.http.output.ProgressData;

/**
 * 作者：xin on 2018/7/31 16:16
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * 下载
 */
public abstract class DownUpCallback implements XinReqCallback<ProgressData> {


    @Override
    public void onSuccess(@NonNull ProgressData progress) {

        if (progress.isDownComplete()) {
            onDownComplete();
        }else{
            progress(progress);
        }
    }

    public abstract void progress(ProgressData progress);


    public abstract void onStart();

    public abstract void onDownComplete();
}

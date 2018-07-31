package xin.framework.http;

import xin.framework.http.callback.DownUpCallback;
import xin.framework.http.request.DownloadRequest;
import xin.framework.http.request.XinRequest;

/**
 * 作者：xin on 2018/7/27 18:24
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class Net {

    /**
     * 网络请求
     */
    public <T> void request(XinRequest<T> xinRequest) {
        xinRequest.OK();
    }

    /**
     * 文件下载
     */
    public void downFile(DownloadRequest downloadInfo) {
        downloadInfo.OK();
    }



    /**
     * 文件下载
     */
    public void uplodFile(DownloadRequest downloadInfo, DownUpCallback callback) {
        // TODO 上传文件
    }


}

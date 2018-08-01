package xin.framework.http;

import xin.framework.http.request.DownloadRequest;
import xin.framework.http.request.UploadRequest;
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
    public <T> void request(XinRequest<T> request) {
        request.OK();
    }

    /**
     * 文件下载
     */
    public void downFile(DownloadRequest request) {
        request.OK();
    }


    /**
     * 文件上传
     */
    public void uploadFile(UploadRequest request) {
        request.OK();
    }


}

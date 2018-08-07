package xin.framework.http.request;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import xin.framework.http.api.ApiService;
import xin.framework.http.callback.DownUpCallback;
import xin.framework.http.callback.XinReqCallback;
import xin.framework.http.callback.XinRequestObserver;
import xin.framework.http.func.OutputFunction;
import xin.framework.http.func.ResultFunction;
import xin.framework.http.helper.HttpHelper;
import xin.framework.http.helper.MediaTypes;
import xin.framework.http.output.BaseOutPut;
import xin.framework.http.output.UploadRequestBody;

/**
 * 作者：xin on 2018/8/1 09:19
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class UploadRequest<T> {


    String baseUrl;
    String suffixUrl;
    List<MultipartBody.Part> parts;
    Type rspClazz;
    XinReqCallback<T> callback;
    Map<String, String> headers;


    private UploadRequest() {
    }

    public static class Builder {
        String baseUrl;
        String suffixUrl;
        List<MultipartBody.Part> parts;
        XinReqCallback callback;
        Type rspClazz;
        Map<String, String> headers;

        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers == null ? new HashMap<String, String>() : headers;

            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setSuffixUrl(String suffixUrl) {
            this.suffixUrl = suffixUrl;
            return this;
        }

        public Builder addFile(String name, File file, DownUpCallback downUpCallback) {

            if (!TextUtils.isEmpty(name) && file != null) {
                RequestBody requestBody = RequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, file);
                MultipartBody.Part part;
                if (downUpCallback != null) {
                    requestBody = new UploadRequestBody(requestBody, downUpCallback);
                }
                part = MultipartBody.Part.createFormData(name, file.getName(), requestBody);

                addPart(part);
            }


            return this;
        }


        public Builder addImage(String name, File img, DownUpCallback downUpCallback) {

            if (!TextUtils.isEmpty(name) && img != null) {
                RequestBody requestBody = RequestBody.create(MediaTypes.IMAGE_TYPE, img);
                MultipartBody.Part part;
                if (downUpCallback != null) {
                    requestBody = new UploadRequestBody(requestBody, downUpCallback);
                }
                part = MultipartBody.Part.createFormData(name, img.getName(), requestBody);
                addPart(part);
            }

            return this;
        }

        public Builder addBytes(String name, byte[] bytes, String fileName, DownUpCallback downUpCallback) {

            if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(fileName) && bytes != null) {
                RequestBody requestBody = RequestBody.create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, bytes);
                MultipartBody.Part part;
                if (downUpCallback != null) {
                    requestBody = new UploadRequestBody(requestBody, downUpCallback);
                }
                part = MultipartBody.Part.createFormData(name, fileName, requestBody);
                addPart(part);
            }

            return this;
        }


        public Builder addStream(String name, InputStream inputStream, String fileName, DownUpCallback downUpCallback) {
            if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(fileName) && inputStream != null) {
                RequestBody requestBody = create(MediaTypes.APPLICATION_OCTET_STREAM_TYPE, inputStream);
                MultipartBody.Part part;
                if (downUpCallback != null) {
                    requestBody = new UploadRequestBody(requestBody, downUpCallback);
                }
                part = MultipartBody.Part.createFormData(name, fileName, requestBody);
                addPart(part);
            }


            return this;
        }


        public Builder setRepCallback(Class rspClazz, XinReqCallback callback) {
            this.callback = callback;
            this.rspClazz = rspClazz;
            return this;
        }


        private RequestBody create(final MediaType mediaType, final InputStream inputStream) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return mediaType;
                }

                @Override
                public long contentLength() {
                    try {
                        return inputStream.available();
                    } catch (IOException e) {
                        return 0;
                    }
                }

                @Override
                public void writeTo(@NonNull BufferedSink sink) throws IOException {
                    Source source = null;
                    try {
                        source = Okio.source(inputStream);
                        sink.writeAll(source);
                    } finally {
                        Util.closeQuietly(source);
                    }
                }
            };
        }

        private void addPart(MultipartBody.Part part) {
            if (parts == null) {
                parts = new ArrayList<>();
            }
            if (part != null)
                parts.add(part);

        }


        public UploadRequest build() {
            UploadRequest uploadRequest = new UploadRequest();
            uploadRequest.callback = callback;
            if (TextUtils.isEmpty(baseUrl)) {
                if(callback==null){
                    throw new NullPointerException("baseUrl  不能为空");
                }else {
                    callback.onError(-100,"baseUrl  不能为空");
                }
            }
            uploadRequest.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";

            uploadRequest.suffixUrl = suffixUrl;


            if (parts == null) {
                if(callback==null){
                    throw new NullPointerException("请添加上传的文件数据");
                }else {
                    callback.onError(-100,"请添加上传的文件数据");
                }
            }

            uploadRequest.parts = parts;

            uploadRequest.rspClazz = rspClazz;



            uploadRequest.headers = headers;

            return uploadRequest;
        }
    }


    private ObservableTransformer<BaseOutPut<T>, T> apiTransformerMap() {
        return new ObservableTransformer<BaseOutPut<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseOutPut<T>> apiResultObservable) {
                return apiResultObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .map(new OutputFunction<T>())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    public void OK() {

        XinRequestObserver<T> observer = new XinRequestObserver<>(callback);

        ApiService apiService = HttpHelper.getInstance().getRetrofit(baseUrl).create(ApiService.class);

        apiService.upload(TextUtils.isEmpty(suffixUrl) ? "" : suffixUrl, parts, headers).
                map(new ResultFunction<T>(rspClazz)).
                compose(apiTransformerMap()).
                subscribe(observer);


    }
}

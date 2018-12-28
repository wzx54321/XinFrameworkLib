package xin.framework.http.request;

import android.text.TextUtils;

import com.trello.rxlifecycle2.LifecycleTransformer;

import org.greenrobot.essentials.io.IoUtils;
import org.reactivestreams.Publisher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import xin.framework.http.api.ApiService;
import xin.framework.http.callback.DownUpCallback;
import xin.framework.http.callback.XinRequestObserver;
import xin.framework.http.helper.HttpHelper;
import xin.framework.http.output.ProgressData;
import xin.framework.utils.common.utils.io.FileUtil;

/**
 * 作者：xin on 2018/7/31 17:40
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class DownloadRequest {


    String baseUrl;
    String suffixUrl;
    Map<String, String> queryParams;

    File file;
    LifecycleTransformer<ResponseBody> lifecycleTransformer;
    DownUpCallback callback;


    private DownloadRequest() {
    }

    public static class Builder {
        private String baseUrl;
        private File file;
        private Map<String, String> queryParams;
        private LifecycleTransformer<ResponseBody> lifecycleTransformer;
        private DownUpCallback callback;
        private String suffixUrl;


        public Builder setSuffixUrl(String suffixUrl) {
            this.suffixUrl = suffixUrl;
            return this;
        }

        public Builder setCallback(DownUpCallback callback) {
            this.callback = callback;
            return this;
        }

        public Builder setLifecycleTransformer(LifecycleTransformer<ResponseBody> transformer) {
            this.lifecycleTransformer = transformer;
            return this;
        }

        public Builder setUrl(String url) {
            this.baseUrl = url;
            return this;
        }


        public Builder setFile(File file) {
            this.file = file;
            return this;
        }


        public Builder setQueryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams;
            return this;
        }


        public DownloadRequest build() {
            DownloadRequest downloadInfo = new DownloadRequest();

            if (TextUtils.isEmpty(baseUrl)) {
                throw new NullPointerException("下载地址不能为空");
            }


            downloadInfo.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
            downloadInfo.suffixUrl = TextUtils.isEmpty(suffixUrl) ? "" : suffixUrl;
            if (file == null) {
                throw new NullPointerException("下载文件不能为空");
            }

            downloadInfo.file = file;
            if (queryParams == null) {
                queryParams = new HashMap<>();
            }
            downloadInfo.queryParams = queryParams;
            downloadInfo.lifecycleTransformer = lifecycleTransformer;
            downloadInfo.callback = callback;
            return downloadInfo;
        }
    }

    public void OK() {

        XinRequestObserver<ProgressData> observer = new XinRequestObserver<>(callback);

        ApiService apiService = HttpHelper.getInstance().getRetrofit(baseUrl).
                create(ApiService.class);

        Observable<ResponseBody> observable = apiService.download(suffixUrl, queryParams == null ? new HashMap<String, String>() : queryParams);
        if (lifecycleTransformer != null) {
            observable = observable.compose(lifecycleTransformer);
        }
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) {
                callback.onStart();
            }
        })
                .toFlowable(BackpressureStrategy.LATEST).
                flatMap(new Function<ResponseBody, Publisher<ProgressData>>() {
                    @Override
                    public Publisher<ProgressData> apply(final ResponseBody responseBody) {

                        return Flowable.create(new FlowableOnSubscribe<ProgressData>() {
                            @Override
                            public void subscribe(FlowableEmitter<ProgressData> emitter) {
                                // 文件保存，进度处理
                                saveFileDoProgress(responseBody, emitter);
                            }


                        }, BackpressureStrategy.LATEST);
                    }
                }).toObservable().subscribeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }


    private void saveFileDoProgress(ResponseBody responseBody, FlowableEmitter<ProgressData> emitter) {
        if (FileUtil.createParentDir(file)) {
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            int readLen;
            try {
                inputStream = responseBody.byteStream();
                fileOutputStream = new FileOutputStream(file);
                ProgressData downProgress = new ProgressData();
                downProgress.setTotalSize(responseBody.contentLength());

                byte[] buffer = new byte[1024];

                while ((readLen = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, readLen);
                    downProgress.setCurrentSize(file.length());
                    emitter.onNext(downProgress);
                }
                fileOutputStream.flush();
                emitter.onComplete();
            } catch (FileNotFoundException e) {
                emitter.onError(e);
            } catch (IOException e) {
                emitter.onError(e);
            } finally {
                IoUtils.safeClose(inputStream);
                IoUtils.safeClose(fileOutputStream);
            }
        }

    }


}

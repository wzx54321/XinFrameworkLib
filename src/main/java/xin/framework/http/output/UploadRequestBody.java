package xin.framework.http.output;

import android.support.annotation.NonNull;

import java.io.IOException;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import xin.framework.http.callback.DownUpCallback;

/**
 * 上传文件请求体
 * 作者：xin on 2018/8/1 13:41
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class UploadRequestBody extends RequestBody {


    private RequestBody requestBody;
    private DownUpCallback callback;
    private long lastTime;

    public UploadRequestBody(RequestBody requestBody, DownUpCallback callback) {
        this.requestBody = requestBody;
        this.callback = callback;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }


    @Override
    public long contentLength() throws IOException {
        try {
            return requestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {

        ProgressSkin progressSkin = new ProgressSkin(sink);
        BufferedSink bufferedSink = Okio.buffer(progressSkin);
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();

    }

    private class ProgressSkin extends ForwardingSink {


        ProgressData progressData;

        ProgressSkin(Sink delegate) {
            super(delegate);
            progressData = new ProgressData();
            try {
                progressData.setTotalSize(contentLength());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            if (progressData.getCurrentSize() == 0 && callback != null) {
                callback.onStart();
            }

            progressData.setCurrentSize(progressData.getCurrentSize() + byteCount);
            if (callback != null) {

                Observable<ProgressData> observable = Observable.just(progressData).filter(new Predicate<ProgressData>() {
                    @Override
                    public boolean test(ProgressData progressData) throws Exception {
                        long currentTime = System.currentTimeMillis();
                        boolean isCallback = currentTime - lastTime > 100;
                        lastTime = currentTime;
                        return isCallback;
                    }
                });
                observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ProgressData>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(ProgressData progress) {
                        callback.onSuccess(progress);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        callback.onError(-100, throwable.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });


            }

        }
    }
}

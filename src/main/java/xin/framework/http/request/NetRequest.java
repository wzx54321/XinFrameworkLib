package xin.framework.http.request;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import xin.framework.http.api.ApiService;
import xin.framework.http.cache.CacheManager;
import xin.framework.http.callback.XinReqCallback;
import xin.framework.http.callback.XinRequestObserver;
import xin.framework.http.func.OutputFunction;
import xin.framework.http.func.ResultFunction;
import xin.framework.http.func.RetryFunction;
import xin.framework.http.helper.HttpHelper;
import xin.framework.http.helper.MediaTypes;
import xin.framework.http.output.BaseOutPut;

/**
 * 维护一些数据
 * 作者：xin on 2018/7/27 18:29
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class NetRequest<T> {
    // url
    public String baseUrl;
    public String cachekey;
    // callback
    public XinReqCallback<T> reqCallback;
    public Class rspClazz;

    public Observable<BaseOutPut<T>> reqObservable;

    public int maxRetryCount;
    public int retryDelayMillis;

    private NetRequest() {
    }

    public static class Builder {

        private  Map<String, Object> fieldMap;
        private  Map<String, String> queryMap;
        private XinReqCallback xinReqCallback;
        private LifecycleTransformer<ResponseBody> lifecycleTransformer;
        private  String baseUrl;
        private String suffixUrl;
        private  String cacheKey;
        private String postContent;
        private MediaType mediaType;
        private Class aClass;
        private  Map<String, String> headers = new HashMap<>();

        int retryCount = 0;
        int retryDelayMillis = 1000;

        public Builder setBaseUrl(String mBaseUrl) {
            this.baseUrl = mBaseUrl;

            return this;
        }

        public Builder setSuffixUrl(String mSuffixUrl) {
            this.suffixUrl = mSuffixUrl;
            return this;
        }


        public Builder setPostContent(String postContent, MediaType mediaType) {
            this.postContent = postContent;
            this.mediaType = mediaType;
            return this;
        }

        public Builder setPostStringContent(String postContent) {
            this.postContent = postContent;
            this.mediaType = MediaTypes.TEXT_PLAIN_TYPE;
            return this;
        }


        public Builder setPostJsonContent(String postContent) {
            this.postContent = postContent;
            this.mediaType = MediaTypes.APPLICATION_JSON_TYPE;
            return this;
        }

        public Builder setPostJson(JsonObject json) {
            this.postContent = json.toString();
            this.mediaType = MediaTypes.APPLICATION_JSON_TYPE;
            return this;
        }

        public Builder setPostJson(JsonArray jsonArray) {
            this.postContent = jsonArray.toString();
            this.mediaType = MediaTypes.APPLICATION_JSON_TYPE;
            return this;
        }

        public Builder setRetryCount(int count) {
            this.retryCount = count;
            return this;
        }

        public Builder setRetryDelay(int millis) {
            this.retryDelayMillis = millis;
            return this;
        }

        public Builder setCacheKey(String cacheKey) {
            this.cacheKey = cacheKey;
            return this;
        }


        public Builder addQueryParam(String key, String value) {
            if (queryMap == null) {
                queryMap = new LinkedHashMap<>();
            }
            queryMap.put(key, value);
            return this;
        }


        public Builder setFieldMap(Map<String, Object> map) {
            fieldMap = map;
            return this;
        }


        public Builder setListener(Class clazz, XinReqCallback xinReqCallback) {
            this.xinReqCallback = xinReqCallback;
            aClass = clazz;
            return this;
        }


        public Builder setLifecycleTransformer(LifecycleTransformer<ResponseBody> transformer) {
            this.lifecycleTransformer = transformer;
            return this;
        }


        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }


        public NetRequest build() {
            NetRequest xinRequest = new NetRequest();
            xinRequest.reqCallback = xinReqCallback;
            if (TextUtils.isEmpty(baseUrl)) {
                if(xinReqCallback ==null){
                    throw new NullPointerException("baseUrl  不能为空");
                }else {
                    xinReqCallback.onError(-100,"baseUrl  不能为空");
                }
            }
            ApiService apiService = HttpHelper.getInstance().getRetrofit(baseUrl).create(ApiService.class);

            // url
            xinRequest.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
            String suffixUrl = TextUtils.isEmpty(this.suffixUrl) ? "" : this.suffixUrl;

            // callback
            xinRequest.reqCallback = xinReqCallback;
            xinRequest.rspClazz = aClass;

            xinRequest.cachekey = cacheKey;
            xinRequest.retryDelayMillis = retryDelayMillis;
            xinRequest.maxRetryCount = retryCount;


            Observable<ResponseBody> reqObservable;
            if (queryMap != null) {
                reqObservable = apiService.get(suffixUrl, queryMap);
            } else if (!TextUtils.isEmpty(postContent) && mediaType != null) {
                reqObservable = apiService.post(suffixUrl, RequestBody.create(mediaType, postContent), headers);
            } else if (fieldMap != null) {
                reqObservable = apiService.postForm(suffixUrl, fieldMap, headers);
            } else {
                reqObservable = apiService.get(suffixUrl, headers);
            }
            if (lifecycleTransformer != null) {
                xinRequest.reqObservable = reqObservable.compose(lifecycleTransformer).map(new ResultFunction<>(aClass));
            } else {
                xinRequest.reqObservable = reqObservable.map(new ResultFunction<>(aClass));
            }

            return xinRequest;

        }


    }


    public ObservableTransformer<BaseOutPut<T>, T> apiTransformerMap() {
        return new ObservableTransformer<BaseOutPut<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseOutPut<T>> apiResultObservable) {
                return apiResultObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .map(new OutputFunction<T>())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new RetryFunction(maxRetryCount, retryDelayMillis));
            }
        };
    }


    public ObservableTransformer<BaseOutPut<T>, BaseOutPut<T>> apiTransformer() {
        return new ObservableTransformer<BaseOutPut<T>, BaseOutPut<T>>() {
            @Override
            public ObservableSource<BaseOutPut<T>> apply(Observable<BaseOutPut<T>> apiResultObservable) {
                return apiResultObservable
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retryWhen(new RetryFunction(maxRetryCount, retryDelayMillis));
            }
        };
    }


    @SuppressWarnings("unchecked")
    public void OK() {
        XinRequestObserver<T> observer = new XinRequestObserver<>(reqCallback);

        if (TextUtils.isEmpty(cachekey)) {
            reqObservable.compose(apiTransformerMap()).subscribe(observer);
        } else {
            CacheManager.getInstance().load(this).subscribe(observer);
        }

    }
}

package xin.framework.http.helper;


import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import xin.framework.configs.HttpCustomConfig;

/**
 * 作者：xin on 2018/6/27 0027 16:06
 * <p>
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */
public class HttpHelper {


    private OkHttpClient mOkHttpClient;
    private RxJava2CallAdapterFactory mAdapterFactory;
    private GsonConverterFactory mGsonConverterFactory;
    private HttpCustomConfig customConfig;

    private HttpHelper() {
        mGsonConverterFactory = GsonConverterFactory.create();
        mAdapterFactory = RxJava2CallAdapterFactory.create();
        customConfig = new HttpCustomConfig();
    }


    public static HttpHelper getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Retrofit getRetrofit(String url) {
        mOkHttpClient = customConfig.build();
        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(mAdapterFactory)
                .addConverterFactory(mGsonConverterFactory)
                .client(mOkHttpClient)
                .build();
    }


    /**
     * 获取个人定制的Retrofit
     * @param interceptors Interceptor
     * @param netInterceptors NetworkInterceptor
     */
    public Retrofit getCustomRetrofit(String url, List<Interceptor> interceptors, List<Interceptor> netInterceptors) {
        HttpCustomConfig config = new HttpCustomConfig();

        OkHttpClient.Builder builder = config.getOkBuilder();
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors)
                builder.addInterceptor(interceptor);
        }

        if(netInterceptors!=null){
            for (Interceptor interceptor : netInterceptors)
                builder.addNetworkInterceptor(interceptor);
        }

        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(mAdapterFactory)
                .addConverterFactory(mGsonConverterFactory)
                .client(builder.build())
                .build();
    }

    private static final class LazyHolder {
        static final HttpHelper INSTANCE = new HttpHelper();
    }
}

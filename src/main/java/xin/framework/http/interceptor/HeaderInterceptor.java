package xin.framework.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 作者：xin on 2018/7/30 16:30
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder();
        //requestBuilder.addHeader( );

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}


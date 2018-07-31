package xin.framework.http.config;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import xin.framework.http.cookie.CookieJarImpl;
import xin.framework.http.cookie.store.MemoryCookieStore;
import xin.framework.http.https.HttpsUtils;
import xin.framework.http.interceptor.HttpLog;


/**
 * 网络请求配置抽象
 * Created by xin
 * <p>
 * 邮箱：ittfxin@126.com
 * <p>
 * https://github.com/wzx54321/XinFrameworkLib
 */

public abstract class HttpConfig {

    private OkHttpClient httpClient;
    private static final long DEFAULT_MILLISECONDS = 60000; // 默认时间


    /**
     * 获取默认的{@link OkHttpClient.Builder}
     * <p>
     */
    private OkHttpClient.Builder getDefaultBuilder() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // log相关
        HttpLog httpLog = new HttpLog();
        httpLog.setPrintLevel(HttpLog.Level.BODY); // log打印级别，决定了log显示的详细程度
        //  httpLog.setPrintBinaryBody(true);// 打印二进制Log ,默认不打印
        builder.addInterceptor(httpLog);


       /* HeaderInterceptor headerInterceptor = new HeaderInterceptor();
        builder.addInterceptor(headerInterceptor);*/


        // 超时时间设置，默认60秒
        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      // 全局的读取超时时间
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     // 全局的写入超时时间
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   // 全局的连接超时时间
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            // 使用内存保持cookie，app退出后，cookie消失
        //  builder.cookieJar(new CookieJarImpl(new SPCookieStore(app)));          // 使用sp保持cookie，如果cookie不过期，则一直有效
        //   builder.cookieJar(new CookieJarImpl(new DBCookieStore(ContextUtils.getContext())));             // 使用数据库保持cookie，如果cookie不过期，则一直有效

        // https相关设置，以下几种方案根据需要自己设置
        // 方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        // 方法二：自定义信任规则，校验服务端证书
        //  HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        // 方法三：使用预埋证书，校验服务端证书（自签名证书）
       /* try {
            HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(app.getAssets().open("srca.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        // 方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        // HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        // 配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        builder.hostnameVerifier(new SafeHostnameVerifier());
        return builder;

    }


    /**
     * 获取默认的{@link OkHttpClient.Builder}
     * <p>
     * 可以根据个人需要创建项目需求的Builder
     */

    @SuppressWarnings("SameReturnValue")
    protected abstract OkHttpClient.Builder getCustomBuilder();


    /**
     * 创建OkHttpClient
     */
    public OkHttpClient build() {

        OkHttpClient.Builder builder = getCustomBuilder();

        if (builder == null) {
            builder = getDefaultBuilder();
        }

        httpClient = builder.build();
        return httpClient;
    }


    public OkHttpClient.Builder getOkBuilder() {
        OkHttpClient.Builder builder = getCustomBuilder();

        if (builder == null) {
            builder = getDefaultBuilder();
        }

        return builder;
    }


    /**
     * 获取OKHttp
     * <p>
     * OkHttpClient
     */
    public OkHttpClient getHttpClient() {

        if (httpClient == null) {
            throw new NullPointerException("OkHttpClient is null,you must call HttpConfig.build(); or set a OkHttpClient  object  first");
        }

        return httpClient;
    }

    public void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }


    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private static class SafeHostnameVerifier implements HostnameVerifier {
        @SuppressLint("BadHostnameVerifier")
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // 验证主机名是否匹配
            // return hostname.equals(RestApiPath.REST_URI_HOST);
            return true;
        }
    }
}

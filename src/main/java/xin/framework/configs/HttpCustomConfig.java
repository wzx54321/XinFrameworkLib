package xin.framework.configs;

import okhttp3.OkHttpClient;
import xin.framework.http.config.HttpConfig;

/**
 * 作者：xin on 2018/6/7 0007 17:40
 * <p>
 * 邮箱：ittfxin@126.com
 */

public class HttpCustomConfig extends HttpConfig {
    @Override
    public OkHttpClient.Builder getCustomBuilder() {
        // 根据个人需要配置
        return null;
    }
}

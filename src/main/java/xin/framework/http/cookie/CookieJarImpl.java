
package xin.framework.http.cookie;


import android.support.annotation.NonNull;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import xin.framework.http.cookie.store.CookieStore;

/**
 * 作者：xin on 2018/6/27 0027 16:06
 * <p>
 * 邮箱：ittfxin@126.com
 * 描    述：CookieJar的实现类，默认管理了用户自己维护的cookie
 */
public class CookieJarImpl implements CookieJar {

    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore) {
        if (cookieStore == null) {
            throw new IllegalArgumentException("cookieStore can not be null!");
        }
        this.cookieStore = cookieStore;
    }

    @Override
    public synchronized void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        cookieStore.saveCookie(url, cookies);
    }

    @Override
    public synchronized List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        return cookieStore.loadCookie(url);
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }
}

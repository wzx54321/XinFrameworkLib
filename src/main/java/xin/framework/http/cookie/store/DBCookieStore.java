
package xin.framework.http.cookie.store;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import xin.framework.store.box.CookieBox;
import xin.framework.store.entity.EntityCookie;
import xin.framework.store.entity.EntityCookie_;

/**

 * 描    述：使用数据库 持久化存储 cookie
 * 作者：xin on 2018/6/27 0027 16:06
 * <p>
 * 邮箱：ittfxin@126.com
 */
public class DBCookieStore implements CookieStore {


    /**
     * 数据结构如下
     * Url.host -> cookie1.name@cookie1.domain,cookie2.name@cookie2.domain,cookie3.name@cookie3.domain
     * cookie_cookie1.name@cookie1.domain -> cookie1
     * cookie_cookie2.name@cookie2.domain -> cookie2
     */
    private final Map<String, ConcurrentHashMap<String, Cookie>> cookies;
    private final CookieBox mCookieBox;

    public DBCookieStore(Context context) {
        mCookieBox = new CookieBox();


        cookies = new HashMap<>();

        List<EntityCookie> cookieList = mCookieBox.queryAll();

        for (EntityCookie entityCookie : cookieList) {
            if (!cookies.containsKey(entityCookie.getHost())) {
                cookies.put(entityCookie.getHost(), new ConcurrentHashMap<String, Cookie>());
            }
            Cookie cookie = entityCookie.getCookie();
            cookies.get(entityCookie.getHost()).put(getCookieToken(cookie), cookie);
        }
    }

    private String getCookieToken(Cookie cookie) {
        return cookie.name() + "@" + cookie.domain();
    }

    /**
     * 当前cookie是否过期
     */
    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    /**
     * 将url的所有Cookie保存在本地
     */
    @Override
    public synchronized void saveCookie(HttpUrl url, List<Cookie> urlCookies) {
        for (Cookie cookie : urlCookies) {
            saveCookie(url, cookie);
        }
    }

    @Override
    public synchronized void saveCookie(HttpUrl url, Cookie cookie) {
        if (!cookies.containsKey(url.host())) {
            cookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
        }
        //当前cookie是否过期
        if (isCookieExpired(cookie)) {
            removeCookie(url, cookie);
        } else {
            //内存缓存
            cookies.get(url.host()).put(getCookieToken(cookie), cookie);



            EntityCookie entityCookie = mCookieBox.getQueryBuilder().equal(EntityCookie_.cookieToken, getCookieToken(cookie)).build().findFirst();

            if (entityCookie != null) {
                entityCookie.setCookie(cookie);
            } else {
                entityCookie = new EntityCookie(url.host(), cookie);

                entityCookie.setCookieByte();
            }

            mCookieBox.insert(entityCookie);

        }
    }

    /**
     * 根据当前url获取所有需要的cookie,只返回没有过期的cookie
     */
    @Override
    public synchronized List<Cookie> loadCookie(HttpUrl url) {
        List<Cookie> ret = new ArrayList<>();
        if (!cookies.containsKey(url.host())) return ret;


        List<EntityCookie> entityCookies=mCookieBox.getQueryBuilder().equal(EntityCookie_.host,url.host()).build().find();
        for (EntityCookie entityCookie : entityCookies) {
            Cookie cookie = entityCookie.getCookie();
            if (isCookieExpired(cookie)) {
                removeCookie(url, cookie);
            } else {
                ret.add(cookie);
            }
        }
        return ret;
    }

    /**
     * 根据url移除当前的cookie
     */
    @Override
    public synchronized boolean removeCookie(HttpUrl url, Cookie cookie) {
        if (!cookies.containsKey(url.host())) return false;
        String cookieToken = getCookieToken(cookie);
        if (!cookies.get(url.host()).containsKey(cookieToken)) return false;

        //内存移除
        cookies.get(url.host()).remove(cookieToken);

        EntityCookie entityCookie = mCookieBox.getQueryBuilder().equal(EntityCookie_.host, url.host()).and()
                .equal(EntityCookie_.name, cookie.name()).and().equal(EntityCookie_.domain, cookie.domain()).build().findFirst();

        if (entityCookie != null) {
            mCookieBox.delete(entityCookie);
        }

        return true;
    }

    @Override
    public synchronized boolean removeCookie(HttpUrl url) {
        if (!cookies.containsKey(url.host())) return false;

        //内存移除
        cookies.remove(url.host());

        //数据库移除
        List<EntityCookie> entityCookies=mCookieBox.getQueryBuilder().equal(EntityCookie_.host,url.host()).build().find();
        mCookieBox.deleteList(entityCookies);
        return true;
    }

    @Override
    public synchronized boolean removeAllCookie() {
        //内存移除
        cookies.clear();
        //数据库移除


        mCookieBox.deleteAll();
        return true;
    }

    /**
     * 获取所有的cookie
     */
    @Override
    public synchronized List<Cookie> getAllCookie() {
        List<Cookie> ret = new ArrayList<>();
        for (String key : cookies.keySet()) {
            ret.addAll(cookies.get(key).values());
        }
        return ret;
    }

    @Override
    public synchronized List<Cookie> getCookie(HttpUrl url) {
        List<Cookie> ret = new ArrayList<>();
        Map<String, Cookie> mapCookie = cookies.get(url.host());
        if (mapCookie != null) ret.addAll(mapCookie.values());
        return ret;
    }
}

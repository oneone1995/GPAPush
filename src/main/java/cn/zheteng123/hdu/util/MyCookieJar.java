package cn.zheteng123.hdu.util;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/6/20.
 */
public class MyCookieJar implements CookieJar {
    private final List<Cookie> allCookies = new ArrayList<Cookie>();
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> result = new ArrayList<Cookie>();
        for (Cookie cookie : allCookies) {
            if (cookie.matches(url)) {
                result.add(cookie);
            }
        }
        return result;
    }
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        allCookies.addAll(cookies);
    }

    public void deleteCookie() {
        allCookies.clear();
    }
}
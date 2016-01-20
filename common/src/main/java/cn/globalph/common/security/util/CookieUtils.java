package cn.globalph.common.security.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieUtils {

    public final static String CUSTOMER_COOKIE_NAME = "customerId";

    public abstract String getCookieValue(HttpServletRequest request, String cookieName);

    public abstract void setCookieValue(HttpServletResponse response, String cookieName, String cookieValue, String path, Integer maxAge, Boolean isSecure);

    public abstract void setCookieValue(HttpServletResponse response, String cookieName, String cookieValue);

    public abstract void invalidateCookie(HttpServletResponse response, String cookieName);

}

package cn.globalph.common.security.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("blCookieUtils")
public class GenericCookieUtilsImpl implements CookieUtils {

    /* (non-Javadoc)
     * @see cn.globalph.passport.web.CookieUtils#getCookieValue(javax.servlet.http.HttpServletRequest, java.lang.String)
     */
    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName()))
                    return (cookie.getValue());
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see cn.globalph.passport.web.CookieUtils#setCookieValue(javax.servlet.http.HttpServletResponse, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
     */
    public void setCookieValue(HttpServletResponse response, String cookieName, String cookieValue, String path, Integer maxAge, Boolean isSecure) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath(path);
        if (maxAge != null) {
            cookie.setMaxAge(maxAge);
        }
        cookie.setSecure(isSecure);

        final StringBuffer sb = new StringBuffer();
        ServerCookie.appendCookieValue
        (sb, cookie.getVersion(), cookie.getName(), cookie.getValue(),
                cookie.getPath(), cookie.getDomain(), cookie.getComment(),
                cookie.getMaxAge(), cookie.getSecure(), true);
        //if we reached here, no exception, cookie is valid
        // the header name is Set-Cookie for both "old" and v.1 ( RFC2109 )
        // RFC2965 is not supported by browsers and the Servlet spec
        // asks for 2109.
        response.addHeader("Set-Cookie", sb.toString());
    }

    /* (non-Javadoc)
     * @see cn.globalph.passport.web.CookieUtils#setCookieValue(javax.servlet.http.HttpServletResponse, java.lang.String, java.lang.String)
     */
    public void setCookieValue(HttpServletResponse response, String cookieName, String cookieValue) {
        setCookieValue(response, cookieName, cookieValue, "/", null, false);
    }

    /* (non-Javadoc)
     * @see cn.globalph.passport.web.CookieUtils#invalidateCookie(javax.servlet.http.HttpServletResponse, java.lang.String)
     */
    public void invalidateCookie(HttpServletResponse response, String cookieName) {
        setCookieValue(response, cookieName, "", "/", 0, false);
    }

}

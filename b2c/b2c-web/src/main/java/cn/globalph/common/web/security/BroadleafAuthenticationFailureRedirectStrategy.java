package cn.globalph.common.web.security;

import cn.globalph.common.web.controller.ControllerUtility;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Extends the Spring DefaultRedirectStrategy with support for ajax redirects.
 * 
 * Designed for use with SpringSecurity when errors are present.
 * 
 * Tacks on the BLC_AJAX_PARAMETER=true to the redirect request if the request is an ajax request.   This will cause the
 * resulting controller (e.g. LoginController) to treat the request as if it is coming from Ajax and 
 * return the related page fragment rather than returning the full view of the page.
 * 
 * @author bpolster
 *
 */
@Component("blAuthenticationFailureRedirectStrategy")
public class BroadleafAuthenticationFailureRedirectStrategy implements RedirectStrategy {
    
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        if (ControllerUtility.isAjaxRequest(request)) {
             url = updateUrlForAjax(url);
        }
        redirectStrategy.sendRedirect(request, response, url);
    }

    public String updateUrlForAjax(String url) {
        String blcAjax = ControllerUtility.BLC_AJAX_PARAMETER;
        if (url != null && url.indexOf("?") > 0) {
            url = url + "&" + blcAjax + "=true";
        } else {
            url = url + "?" + blcAjax + "=true";
        }
        return url;
    }
    
    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}

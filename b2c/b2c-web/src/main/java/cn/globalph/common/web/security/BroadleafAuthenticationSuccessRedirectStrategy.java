package cn.globalph.common.web.security;

import cn.globalph.common.web.controller.ControllerUtility;

import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/** 
 * If the incoming request is an ajax request, the system will add the desired redirect path to the session and
 * then redirect to the path configured for the redirectPath property.
 * 
 * It is assumed that the redirectPath will be picked up by the BroadleafRedirectController.
 * 
 * @author bpolster
 * 
 */
@Component("blAuthenticationSuccessRedirectStrategy")
public class BroadleafAuthenticationSuccessRedirectStrategy implements RedirectStrategy {
    
    private String redirectPath="/redirect";
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        if (ControllerUtility.isAjaxRequest(request)) {
            request.getSession().setAttribute("BLC_REDIRECT_URL", url);
            url = getRedirectPath();
        }
        redirectStrategy.sendRedirect(request, response, url);
    }

    public String updateLoginErrorUrlForAjax(String url) {
        String blcAjax = ControllerUtility.BLC_AJAX_PARAMETER;
        if (url != null && url.indexOf("?") > 0) {
            url = url + "&" + blcAjax + "=true";
        } else {
            url = url + "?" + blcAjax + "=true";
        }
        return url;
    }

    public String getRedirectPath() {
        return redirectPath;
    }

    public void setRedirectPath(String redirectPath) {
        this.redirectPath = redirectPath;
    }

    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}

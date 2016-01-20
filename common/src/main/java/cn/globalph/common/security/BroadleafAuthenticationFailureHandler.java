package cn.globalph.common.security;

import org.apache.commons.lang.StringUtils;

import cn.globalph.common.util.StringUtil;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BroadleafAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private String defaultFailureUrl;

    public BroadleafAuthenticationFailureHandler() {
        super();
    }

    public BroadleafAuthenticationFailureHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
        this.defaultFailureUrl = defaultFailureUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String failureUrlParam = StringUtil.cleanseUrlString(request.getParameter("failureUrl"));
        String successUrlParam = StringUtil.cleanseUrlString(request.getParameter("successUrl"));
        String username = StringUtil.cleanseUrlString(request.getParameter("j_username"));
        
        String failureUrl = StringUtils.trimToNull(failureUrlParam);

        // Verify that the url passed in is a servlet path and not a link redirecting away from the webapp.
        failureUrl = validateUrlParam(failureUrl);
        successUrlParam = validateUrlParam(successUrlParam);
        username = validateUrlParam(username);

        if (failureUrl == null) {
            failureUrl = StringUtils.trimToNull(defaultFailureUrl);
        }
        if (failureUrl != null) {
            if (StringUtils.isNotEmpty(successUrlParam)) {
                if (!failureUrl.contains("?")) {
                    failureUrl += "?successUrl=" + successUrlParam;
                } else {
                    failureUrl += "&successUrl=" + successUrlParam;
                }
            }
            if (StringUtils.isNotEmpty(username)) {
	            if (!failureUrl.contains("?")) {
	                failureUrl += "?username=" + username;
	            } else {
	                failureUrl += "&username=" + username;
	            }
            }
            saveException(request, exception);
            getRedirectStrategy().sendRedirect(request, response, failureUrl);
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }

    public String validateUrlParam(String url) {
        if (url != null) {
            if (url.contains("http") || url.contains("www")) {
                return null;
            }
        }
        return url;
    }

}

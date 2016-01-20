package cn.globalph.openadmin.security;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: jfischer
 * Date: 10/20/11
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class BroadleafAdminLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {

    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        String queryString = request.getQueryString();
        if (!StringUtils.isEmpty(queryString)) {
            targetUrl += "?" + queryString;
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}

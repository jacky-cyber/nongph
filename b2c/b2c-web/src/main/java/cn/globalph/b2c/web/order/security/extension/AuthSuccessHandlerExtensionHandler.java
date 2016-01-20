package cn.globalph.b2c.web.order.security.extension;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Extension handler for actions that should take place after a user has authenticated on the front-end site.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public interface AuthSuccessHandlerExtensionHandler extends ExtensionHandler {
    
    /**
     * Perform any necessary tasks before the merge cart processor executes.
     * 
     * @param request
     * @param response
     * @param authentication
     * @return
     */
    public ExtensionResultStatusType preMergeCartExecution(HttpServletRequest request, HttpServletResponse response, 
            Authentication authentication);

}

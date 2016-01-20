package cn.globalph.b2c.web.order.security.extension;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Abstract handler for {@link AuthSuccessHandlerExtensionHandler} so that actual implementations of this handler
 * do not need to implemenet every single method.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class AbstractAuthSuccessHandlerExtensionHandler extends AbstractExtensionHandler implements 
    AuthSuccessHandlerExtensionHandler {

    @Override
    public ExtensionResultStatusType preMergeCartExecution(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}

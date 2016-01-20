package cn.globalph.b2c.web.order.security.extension;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * Manager for the {@link AuthSuccessHandlerExtensionHandler}
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Service("blAuthSuccessHandlerExtensionManager")
public class AuthSuccessHandlerExtensionManager extends ExtensionManager<AuthSuccessHandlerExtensionHandler> {

    public AuthSuccessHandlerExtensionManager() {
        super(AuthSuccessHandlerExtensionHandler.class);
    }

    public boolean continueOnHandled() {
        return true;
    }

}

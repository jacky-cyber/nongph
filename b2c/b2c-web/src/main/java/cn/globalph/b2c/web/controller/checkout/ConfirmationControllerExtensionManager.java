package cn.globalph.b2c.web.controller.checkout;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * @author Andre Azzolini (apazzolini)
 */
@Service("blConfirmationControllerExtensionManager")
public class ConfirmationControllerExtensionManager extends ExtensionManager<ConfirmationControllerExtensionHandler> {

    public ConfirmationControllerExtensionManager() {
        super(ConfirmationControllerExtensionHandler.class);
    }

    /**
     * By default, this manager will allow other handlers to process the method when a handler returns
     * HANDLED.
     */
    @Override
    public boolean continueOnHandled() {
        return true;
    }
}

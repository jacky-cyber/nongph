package cn.globalph.b2c.web.controller.checkout;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

/**
 * Extension manager for the checkout controller
 *
 * @author Joshua Skorton (jskorton)
 */
@Service("blCheckoutControllerExtensionManager")
public class BroadleafCheckoutControllerExtensionManager extends ExtensionManager<BroadleafCheckoutControllerExtensionHandler> {

    public BroadleafCheckoutControllerExtensionManager() {
        super(BroadleafCheckoutControllerExtensionHandler.class);
    }

    @Override
    public boolean continueOnHandled() {
        return true;
    }

}

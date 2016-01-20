package cn.globalph.b2c.web.controller.checkout;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;

import org.springframework.ui.Model;

/**
 * Extension handler for the checkout controller
 *
 * @author Joshua Skorton (jskorton)
 */
public interface BroadleafCheckoutControllerExtensionHandler extends ExtensionHandler {

    /**
     *  Allow other modules to add properties to the checkout controller model
     * 
     * @param model
     * @return
     */
    public ExtensionResultStatusType addAdditionalModelVariables(Model model);

    /**
     * Allow other modules to execute additional logic in shipping methods
     * 
     * @return
     */
    public ExtensionResultStatusType performAdditionalShippingAction();
}


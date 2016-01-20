package cn.globalph.b2c.web.controller.checkout;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;

import org.springframework.ui.Model;

/**
 * @author felix.wu
 */
public abstract class AbstractCheckoutControllerExtensionHandler extends AbstractExtensionHandler
        implements BroadleafCheckoutControllerExtensionHandler {

    @Override
    public ExtensionResultStatusType addAdditionalModelVariables(Model model) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType performAdditionalShippingAction() {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}

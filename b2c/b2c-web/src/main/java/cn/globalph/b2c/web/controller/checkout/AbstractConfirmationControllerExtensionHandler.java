package cn.globalph.b2c.web.controller.checkout;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;


/**
 * @author Andre Azzolini (apazzolini)
 */
public class AbstractConfirmationControllerExtensionHandler extends AbstractExtensionHandler 
        implements ConfirmationControllerExtensionHandler {

    @Override
    public ExtensionResultStatusType processAdditionalConfirmationActions(Order order) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    

}

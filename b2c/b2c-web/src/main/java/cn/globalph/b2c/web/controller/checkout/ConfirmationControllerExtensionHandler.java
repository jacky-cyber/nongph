package cn.globalph.b2c.web.controller.checkout;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;


/**
 * @author Andre Azzolini (apazzolini)
 */
public interface ConfirmationControllerExtensionHandler extends ExtensionHandler {
    
    public ExtensionResultStatusType processAdditionalConfirmationActions(Order order);

}

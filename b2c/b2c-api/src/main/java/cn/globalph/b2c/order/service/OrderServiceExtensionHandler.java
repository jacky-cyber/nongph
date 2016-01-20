package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.passport.domain.Customer;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */
public interface OrderServiceExtensionHandler extends ExtensionHandler {
    
    public ExtensionResultStatusType attachAdditionalDataToNewNamedCart(Customer customer, Order cart);

    public ExtensionResultStatusType preValidateCartOperation(Order cart, ExtensionResultHolder erh);
    
}

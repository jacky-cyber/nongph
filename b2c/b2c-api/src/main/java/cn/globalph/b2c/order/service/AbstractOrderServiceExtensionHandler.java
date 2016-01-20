package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.passport.domain.Customer;


/**
 * @author bpolster
 */
public abstract class AbstractOrderServiceExtensionHandler extends AbstractExtensionHandler implements
        OrderServiceExtensionHandler {
    
    public ExtensionResultStatusType attachAdditionalDataToNewNamedCart(Customer customer, Order cart) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    public ExtensionResultStatusType preValidateCartOperation(Order cart, ExtensionResultHolder erh) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    
}

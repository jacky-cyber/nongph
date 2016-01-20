package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.passport.domain.Customer;

/**
 * @author Andre Azzolini (apazzolini)
 */
public abstract class AbstractMergeCartServiceExtensionHandler extends AbstractExtensionHandler implements
        MergeCartServiceExtensionHandler {
    
    public ExtensionResultStatusType setNewCartOwnership(Order cart, Customer customer) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    
    public ExtensionResultStatusType updateMergedOrder(Order cart, Customer customer) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}

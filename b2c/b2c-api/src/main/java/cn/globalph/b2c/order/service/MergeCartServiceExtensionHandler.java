package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.passport.domain.Customer;


/**
 * Extension handler for merge cart
 * 
 * @author Andre Azzolini (apazzolini)
 */
public interface MergeCartServiceExtensionHandler extends ExtensionHandler {
    
    ExtensionResultStatusType setNewCartOwnership(Order cart, Customer customer);

    ExtensionResultStatusType updateMergedOrder(Order cart, Customer customer);
    
}

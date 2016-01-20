
package cn.globalph.b2c.web.service;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */
public interface UpdateCartServiceExtensionHandler extends ExtensionHandler {
    
    /**
     * Throws an exception if cart is invalid.
     * 
     * @param cart
     * @param resultHolder
     * @return
     */
    public ExtensionResultStatusType updateAndValidateCart(Order cart, ExtensionResultHolder resultHolder);

}

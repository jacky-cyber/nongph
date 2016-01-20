package cn.globalph.b2c.order.service;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */
@Service("blOrderServiceExtensionManager")
public class OrderServiceExtensionManager extends ExtensionManager<OrderServiceExtensionHandler> {

    public OrderServiceExtensionManager() {
        super(OrderServiceExtensionHandler.class);
    }

    /**
     * By default,this extension manager will continue on handled allowing multiple handlers to interact with the order.
     */
    public boolean continueOnHandled() {
        return true;
    }
}

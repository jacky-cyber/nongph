package cn.globalph.b2c.order.dao;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */
@Service("blOrderDaoExtensionManager")
public class OrderDaoExtensionManager extends ExtensionManager<OrderDaoExtensionHandler> {

    public OrderDaoExtensionManager() {
        super(OrderDaoExtensionHandler.class);
    }

    /**
     * By default, this manager will allow other handlers to process the method when a handler returns
     * HANDLED.
     */
    @Override
    public boolean continueOnHandled() {
        return true;
    }
}

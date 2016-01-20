package cn.globalph.b2c.order.dao;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.passport.domain.Customer;

import java.util.List;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */
public class AbstractOrderDaoExtensionHandler extends AbstractExtensionHandler implements OrderDaoExtensionHandler {
    
    public ExtensionResultStatusType attachAdditionalDataToNewCart(Customer customer, Order cart) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    public ExtensionResultStatusType processPostSaveNewCart(Customer customer, Order cart) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    
    public ExtensionResultStatusType applyAdditionalOrderLookupFilter(Customer customer, String name, List<Order> orders) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}

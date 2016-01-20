package cn.globalph.b2c.order.dao;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.passport.domain.Customer;

import java.util.List;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */
public interface OrderDaoExtensionHandler extends ExtensionHandler {
    
    public ExtensionResultStatusType attachAdditionalDataToNewCart(Customer customer, Order cart);

    public ExtensionResultStatusType processPostSaveNewCart(Customer customer, Order cart);
    
    public ExtensionResultStatusType applyAdditionalOrderLookupFilter(Customer customer, String name, List<Order> orders);

}

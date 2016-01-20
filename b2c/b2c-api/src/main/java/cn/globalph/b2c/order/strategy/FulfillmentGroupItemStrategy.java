package cn.globalph.b2c.order.strategy;

import cn.globalph.b2c.order.service.workflow.CartOperationRequest;
import cn.globalph.b2c.pricing.service.exception.PricingException;

/**
 * The methods in this class are invoked by the add and update item to cart workflows.
 * Broadleaf provides two implementations, the default FulfillmentGroupItemStrategyImpl 
 * and also a strategy that does nothing to FulifllmentGroupItems, which can be configured
 * by injecting the NullFulfillmentGroupItemStrategyImpl class as the "blFulfillmentGroupItemStrategy"
 * bean.
 * 
 * The null strategy would be the approach taken prior to 2.0, where the user was required
 * to manage FulfillmentGroups and FulfillmentGroupItems by themselves. However, the new default
 * implmentation takes care of this for you by ensuring that FG Items and OrderItems stay in sync.
 * 
 * Note that even the null strategy <b>WILL</b> remove FulfillmentGroupItems if their corresponding
 * OrderItem is removed to prevent orphaned records.
 * 
 */
public interface FulfillmentGroupItemStrategy {

    public CartOperationRequest onItemAdded(CartOperationRequest request) throws PricingException;

    public CartOperationRequest onItemUpdated(CartOperationRequest request) throws PricingException;
    
    public CartOperationRequest onItemRemoved(CartOperationRequest request) throws PricingException;
    
    public CartOperationRequest verify(CartOperationRequest request) throws PricingException;

    public void setRemoveEmptyFulfillmentGroups(boolean removeEmptyFulfillmentGroups);
    
    public boolean isRemoveEmptyFulfillmentGroups();

}

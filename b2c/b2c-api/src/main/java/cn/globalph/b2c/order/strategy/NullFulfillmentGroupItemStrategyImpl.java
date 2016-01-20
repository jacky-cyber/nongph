package cn.globalph.b2c.order.strategy;

import cn.globalph.b2c.order.service.workflow.CartOperationRequest;

/**
 * This class provides the implementation of a strategy that does not touch 
 * FulfillmentGroupItems when cart add or update operations have been performed.
 * However, the remove operation must still remove the FulfillmentGroupItems, and this
 * strategy will delegate to the default Broadleaf FulfillmentGroupItemStrategy to perform
 * the removal.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class NullFulfillmentGroupItemStrategyImpl extends FulfillmentGroupItemStrategyImpl {
    
    protected boolean removeEmptyFulfillmentGroups = false;
    
    @Override
    public CartOperationRequest onItemAdded(CartOperationRequest request) {
        return request;
    }
    
    @Override
    public CartOperationRequest onItemUpdated(CartOperationRequest request) {
        return request;
    }
    
    /** 
     * When we remove an order item, we must also remove the associated fulfillment group
     * item to respsect the database constraints.
     */
    @Override
    public CartOperationRequest onItemRemoved(CartOperationRequest request) {
        return super.onItemRemoved(request);
    }
    
    @Override
    public CartOperationRequest verify(CartOperationRequest request) {
        return request;
    }
    
    @Override
    public boolean isRemoveEmptyFulfillmentGroups() {
        return removeEmptyFulfillmentGroups;
    }

}

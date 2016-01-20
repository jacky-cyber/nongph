package cn.globalph.b2c.order.domain.wrapper;

import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.wrap.FulfillmentGroupItemWrap;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.call.FulfillmentGroupItemRequest;
import cn.globalph.common.domain.wrapper.APIUnwrapper;
import cn.globalph.common.domain.wrapper.APIWrapper;

public class FulfillmentGroupItemWrapper implements APIWrapper<FulfillmentGroupItem, FulfillmentGroupItemWrap>, APIUnwrapper<FulfillmentGroupItemRequest, FulfillmentGroupItemWrap> {
	private OrderItemService orderItemService;
	
    @Override
    public FulfillmentGroupItemWrap wrapDetails(FulfillmentGroupItem model) {
    	FulfillmentGroupItemWrap wrap = new FulfillmentGroupItemWrap();
    	wrap.setId( model.getId() );

        if (model.getFulfillmentGroup() != null) {
        	wrap.setFulfillmentGroupId( model.getFulfillmentGroup().getId() );
        }
        if (model.getOrderItem() != null) {
        	wrap.setOrderItemId( model.getOrderItem().getId() );
        }

        wrap.setQuantity( model.getQuantity() );
        wrap.setTotalItemAmount( model.getTotalItemAmount() );
        
        return wrap;
    }

    @Override
    public FulfillmentGroupItemWrap wrapSummary(FulfillmentGroupItem model) {
        return wrapDetails(model);
    }

    @Override
    public FulfillmentGroupItemRequest unwrap( FulfillmentGroupItemWrap wrap ) {
        OrderItem orderItem = orderItemService.readOrderItemById( wrap.getOrderItemId() );
        if (orderItem != null) {
            FulfillmentGroupItemRequest fulfillmentGroupItemRequest = new FulfillmentGroupItemRequest();
            fulfillmentGroupItemRequest.setOrderItem(orderItem);
            fulfillmentGroupItemRequest.setQuantity(wrap.getQuantity());
            return fulfillmentGroupItemRequest;
        }
        return null;
    }
}

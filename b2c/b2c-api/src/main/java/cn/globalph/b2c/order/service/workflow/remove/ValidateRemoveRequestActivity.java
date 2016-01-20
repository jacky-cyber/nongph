package cn.globalph.b2c.order.service.workflow.remove;

import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.call.OrderItemRequestDTO;
import cn.globalph.b2c.order.service.workflow.CartOperationRequest;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import javax.annotation.Resource;

public class ValidateRemoveRequestActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;
    

    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();

        // Throw an exception if the user did not specify an orderItemId
        if (orderItemRequestDTO.getOrderItemId() == null) {
            throw new IllegalArgumentException("OrderItemId must be specified when removing from order");
        }

        // Throw an exception if the user did not specify an order to add the item to
        if (request.getOrder() == null) {
            throw new IllegalArgumentException("Order is required when updating item quantities");
        }
        
        // Throw an exception if the user is trying to remove an order item that is part of a bundle
        OrderItem orderItem = null;
        for (OrderItem oi : request.getOrder().getOrderItems()) {
            if (oi.getId().equals(orderItemRequestDTO.getOrderItemId())) {
                orderItem = oi;
            	}
           }
        
        if (orderItem == null) {
            throw new IllegalArgumentException("Could not find order item to remove");
           }
        
        request.setOrderItem(orderItem);
        
        return context;
    }
    
}

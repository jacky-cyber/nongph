package cn.globalph.b2c.order.service.workflow.update.options;

import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.call.OrderItemRequestDTO;
import cn.globalph.b2c.order.service.workflow.CartOperationRequest;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import javax.annotation.Resource;

public class ValidateUpdateProductOptionsRequestActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;
    
    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();
        
        // Throw an exception if the user did not specify an orderItemId
        if (orderItemRequestDTO.getOrderItemId() == null) {
            throw new IllegalArgumentException("OrderItemId must be specified to locate the order item");
           }

        // Throw an exception if the user did not specify an order to add the item to
        if (request.getOrder() == null) {
            throw new IllegalArgumentException("Order is required when updating items in the order");
           }
        
        return context;
    }
    
}

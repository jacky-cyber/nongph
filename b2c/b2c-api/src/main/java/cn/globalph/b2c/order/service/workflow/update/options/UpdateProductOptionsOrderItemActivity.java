package cn.globalph.b2c.order.service.workflow.update.options;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.call.DiscreteOrderItemRequest;
import cn.globalph.b2c.order.service.call.OrderItemRequestDTO;
import cn.globalph.b2c.order.service.workflow.CartOperationRequest;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import javax.annotation.Resource;

public class UpdateProductOptionsOrderItemActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    @Resource(name = "blOrderService")
    protected OrderService orderService;

    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;

    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        OrderItemRequestDTO orderItemRequestDTO = request.getItemRequest();
        Order order = request.getOrder();
        
        if (orderItemService.readOrderItemById(Long.valueOf(orderItemRequestDTO.getOrderItemId())) != null) {
            DiscreteOrderItemRequest itemRequest = new DiscreteOrderItemRequest();
            orderItemService.updateDiscreteOrderItem(orderItemService.readOrderItemById(Long.valueOf(orderItemRequestDTO.getOrderItemId())), itemRequest);
           }

        order = orderService.save(order, false);
        request.setOrder(order);

        return context;
    }

}

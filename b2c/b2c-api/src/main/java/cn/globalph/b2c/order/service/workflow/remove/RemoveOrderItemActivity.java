package cn.globalph.b2c.order.service.workflow.remove;

import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.workflow.CartOperationRequest;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import java.util.List;

import javax.annotation.Resource;

/**
 * This class is responsible for determining which OrderItems should be removed from the order, taking into account
 * the fact that removing an OrderItem should also remove all of its child order items.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class RemoveOrderItemActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {

    @Resource(name = "blOrderService")
    protected OrderService orderService;
    
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;
    
    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();

        OrderItem orderItem = request.getOrderItem();
        removeItem(request.getOisToDelete(), orderItem);
        
        return context;
    }
    
    protected void removeItem(List<OrderItem> oisToDelete, OrderItem orderItem) {
        oisToDelete.add(orderItem);
    }

}

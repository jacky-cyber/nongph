package cn.globalph.b2c.order.service.workflow.update;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.workflow.CartOperationRequest;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

public class UpdateOrderMultishipOptionActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;
    
    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        Long orderItemId = request.getItemRequest().getOrderItemId();
        
        Integer orderItemQuantityDelta = request.getOrderItemQuantityDelta();
        if (orderItemQuantityDelta < 0) {
            int numToDelete = -1 * orderItemQuantityDelta;
            //find the qty in the default fg
            OrderItem orderItem = request.getOrderItem();
            int qty = 0;
            if (!CollectionUtils.isEmpty(orderItem.getOrder().getFulfillmentGroups())) {
                FulfillmentGroup fg = orderItem.getOrder().getFulfillmentGroups().get(0);
                if (fg.getAddress() == null && fg.getFulfillmentOption() == null) {
                    for (FulfillmentGroupItem fgItem : fg.getFulfillmentGroupItems()) {
                        if (fgItem.getOrderItem().getId() == orderItemId) {
                            qty += fgItem.getQuantity();
                        }
                    }
                }
            }
            if (numToDelete >= qty) {
                request.getMultishipOptionsToDelete().add(new Long[] { orderItemId, (long) (numToDelete - qty) });
            }
        }
        
        return context;
    }

}

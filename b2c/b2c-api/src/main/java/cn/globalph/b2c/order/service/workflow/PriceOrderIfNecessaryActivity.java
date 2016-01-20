package cn.globalph.b2c.order.service.workflow;

import org.apache.commons.collections.CollectionUtils;

import cn.globalph.b2c.order.dao.FulfillmentGroupItemDao;
import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

/**
 * saves of individual aspects of an Order (such as OrderItems and FulfillmentGroupItems) no
 * longer happen in their respective activities. Instead, we will now handle these saves in this activity exclusively.
 * 
 * This provides the ability for an implementation to not require a transactional wrapper around the entire workflow and
 * instead only requires it around this particular activity. This is only recommended if there are long running steps in
 * the workflow, such as an external service call to check availability.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class PriceOrderIfNecessaryActivity extends BaseActivity<ProcessContext<CartOperationRequest>> {
    
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;

    @Resource(name = "blFulfillmentGroupItemDao")
    protected FulfillmentGroupItemDao fgItemDao;

    @Override
    public ProcessContext<CartOperationRequest> execute(ProcessContext<CartOperationRequest> context) throws Exception {
        CartOperationRequest request = context.getSeedData();
        Order order = request.getOrder();
        
        // We potentially have some FulfillmentGroupItems that were identified in the FulfillmentGroupItemStrategy as
        // ones that should be deleted. Delete them here.
        if (CollectionUtils.isNotEmpty(request.getFgisToDelete())) {
            for (FulfillmentGroupItem fgi : request.getFgisToDelete()) {
                for (FulfillmentGroup fg : order.getFulfillmentGroups()) {
                    ListIterator<FulfillmentGroupItem> fgItemIter = fg.getFulfillmentGroupItems().listIterator();
                    while (fgItemIter.hasNext()) {
                        FulfillmentGroupItem fgi2 = fgItemIter.next();
                        if (fgi2 == fgi) {
                            fgItemIter.remove();
                            fgItemDao.delete(fgi2);
                        }
                    }
                }
            }
        }
        if(request.isRealDelete()){
	        // We now need to delete any OrderItems that were marked as such, including their children, if any
	        for (OrderItem oi : request.getOisToDelete()) {
	            order.getOrderItems().remove(oi);
	            orderItemService.delete(oi);
	        }
        }else{
        	for (OrderItem oi : request.getOisToDelete()) {
        		order.getOrderItems().remove(oi);
        		oi.setIsUsed(false);
        		orderItemService.saveOrderItem(oi);
        		order.getOrderItems().add(oi);
        	}
        }
        
        // We need to build up a map of OrderItem to which FulfillmentGroupItems reference that particular OrderItem.
        // We'll also save the order item and build up a map of the unsaved items to their saved counterparts.
        Map<OrderItem, List<FulfillmentGroupItem>> oiFgiMap = new HashMap<OrderItem, List<FulfillmentGroupItem>>();
        Map<OrderItem, OrderItem> savedOrderItems = new HashMap<OrderItem, OrderItem>();
        for (OrderItem oi : order.getOrderItems()) {
        	if(!oi.isUsed()) continue;
            getOiFgiMap(order, oiFgiMap, oi);
            savedOrderItems.put(oi, orderItemService.saveOrderItem(oi));
        }
        
        // Now, we'll update the orderitems in the order to their saved counterparts
        ListIterator<OrderItem> li = order.getOrderItems().listIterator();
        List<OrderItem> oisToAdd = new ArrayList<OrderItem>();
        while (li.hasNext()) {
            OrderItem oi = li.next();
            if(!oi.isUsed()) continue;
            OrderItem savedOi = savedOrderItems.get(oi);
            oisToAdd.add(savedOi);
            li.remove();
        }
        order.getOrderItems().addAll(oisToAdd);

        for (Entry<OrderItem, List<FulfillmentGroupItem>> entry : oiFgiMap.entrySet()) {
            // Update any FulfillmentGroupItems that reference order items
            for (FulfillmentGroupItem fgi : entry.getValue()) {
                fgi.setOrderItem(savedOrderItems.get(entry.getKey()));
            }

            // We also need to update the orderItem on the request in case it's used by the caller of this workflow
            if (entry.getKey() == request.getOrderItem()) {
                request.setOrderItem(savedOrderItems.get(entry.getKey()));
            }
        }

        // If a custom implementation needs to handle additional saves before the parent Order is saved, this method
        // can be overridden to provide that functionality.
        preSaveOperation(request);
        
        // Now that our collection items in our Order have been saved and the state of our Order is in a place where we
        // won't get a transient save exception, we are able to go ahead and save the order with optional pricing.
        order = orderService.save(order, request.isPriceOrder());
        request.setOrder(order);
        
        return context;
    }

    protected void getOiFgiMap(Order order, Map<OrderItem, List<FulfillmentGroupItem>> oiFgiMap, OrderItem oi) {
        List<FulfillmentGroupItem> fgis = new ArrayList<FulfillmentGroupItem>();

        for (FulfillmentGroup fg : order.getFulfillmentGroups()) {
            for (FulfillmentGroupItem fgi : fg.getFulfillmentGroupItems()) {
                if (fgi.getOrderItem().equals(oi)) {
                    fgis.add(fgi);
                }
            }
        }

        oiFgiMap.put(oi, fgis);
    }
    
    /**
     * Intended to be overridden by a custom implementation if there is a requirement to perform additional logic or
     * saves before triggering the main Order save with pricing.
     * 
     * @param request
     */
    protected void preSaveOperation(CartOperationRequest request) {
        // Broadleaf implementation does nothing here
    }

}

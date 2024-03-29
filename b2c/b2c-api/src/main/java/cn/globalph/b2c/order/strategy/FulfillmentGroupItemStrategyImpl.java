package cn.globalph.b2c.order.strategy;

import cn.globalph.b2c.order.dao.FulfillmentGroupItemDao;
import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.FulfillmentGroupService;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.call.FulfillmentGroupItemRequest;
import cn.globalph.b2c.order.service.type.FulfillmentType;
import cn.globalph.b2c.order.service.workflow.CartOperationRequest;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.product.domain.Sku;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

/**
 * @author felix.wu
 */
@Service("blFulfillmentGroupItemStrategy")
public class FulfillmentGroupItemStrategyImpl implements FulfillmentGroupItemStrategy {
    
    @Resource(name = "blFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;
    
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;
    
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    
    @Resource(name = "blFulfillmentGroupItemDao")
    protected FulfillmentGroupItemDao fgItemDao;
    
    protected boolean removeEmptyFulfillmentGroups = true;
    
    @Override
    public CartOperationRequest onItemAdded(CartOperationRequest request) throws PricingException {
        Order order = request.getOrder();
        OrderItem orderItem = request.getOrderItem();
        Map<FulfillmentType, FulfillmentGroup> fulfillmentGroups = new HashMap<FulfillmentType, FulfillmentGroup>();
        FulfillmentGroup nullFulfillmentTypeGroup = null;
        
        //First, let's organize fulfillment groups according to their fulfillment type
        //We'll use the first of each type that we find. Implementors can choose to move groups / items around later.
        if (order.getFulfillmentGroups() != null) {
            for (FulfillmentGroup group : order.getFulfillmentGroups()) {
                if (group.getType() == null) {
                	nullFulfillmentTypeGroup = group;
                } else {
                    if (fulfillmentGroups.get(group.getType()) == null) {
                        fulfillmentGroups.put(group.getType(), group);
                    }	
                }
            }
        }
        
        FulfillmentGroup fulfillmentGroup = null;
        FulfillmentType type = resolveFulfillmentType(orderItem);
        if (type == null) {
            //Use the fulfillment group with a null type
            fulfillmentGroup = nullFulfillmentTypeGroup;
        } else {
            if (FulfillmentType.PHYSICAL_PICKUP_OR_SHIP.equals(type)) {
                //This is really a special case. "PICKUP_OR_SHIP" is convenient to allow a sku to be picked up or shipped.
                //However, it is ambiguous when actually trying to create a fulfillment group. So we default to "PHYSICAL_SHIP".
                type = FulfillmentType.PHYSICAL_SHIP;
            }
            //Use the fulfillment group with the specified type
            fulfillmentGroup = fulfillmentGroups.get(type);
         }
            
         //If the null type or specified type, above were null, then we need to create a new fulfillment group
         if (fulfillmentGroup == null) {
            fulfillmentGroup = fulfillmentGroupService.createEmptyFulfillmentGroup();
            //Set the type
            fulfillmentGroup.setType(type);
            fulfillmentGroup.setOrder(order);
            order.getFulfillmentGroups().add(fulfillmentGroup);
         }
            
         fulfillmentGroup = addItemToFulfillmentGroup(order, orderItem, fulfillmentGroup);
         order = fulfillmentGroup.getOrder();
         return request;
    }
    
    /**
     * Resolves the fulfillment type based on the order item. The OOB implementation uses the {@link DiscreteOrderItem#getSku()}
     * to then invoke {@link #resolveFulfillmentType(Sku)}.
     * 
     * @param discreteOrderItem
     * @return
     */
    protected FulfillmentType resolveFulfillmentType(OrderItem discreteOrderItem) {
        return resolveFulfillmentType(discreteOrderItem.getSku());
     }
    
    protected FulfillmentType resolveFulfillmentType(Sku sku) {
        if (sku.getFulfillmentType() != null) {
            return sku.getFulfillmentType();
        }
        return null;
    }
    
    protected FulfillmentGroup addItemToFulfillmentGroup(Order order, OrderItem orderItem, FulfillmentGroup fulfillmentGroup) throws PricingException {
        return this.addItemToFulfillmentGroup(order, orderItem, orderItem.getQuantity(), fulfillmentGroup);
    }

    protected FulfillmentGroup addItemToFulfillmentGroup(Order order, OrderItem orderItem, int quantity, FulfillmentGroup fulfillmentGroup) throws PricingException {
        FulfillmentGroupItemRequest fulfillmentGroupItemRequest = new FulfillmentGroupItemRequest();
        fulfillmentGroupItemRequest.setOrder(order);
        fulfillmentGroupItemRequest.setOrderItem(orderItem);
        fulfillmentGroupItemRequest.setQuantity(quantity);
        fulfillmentGroupItemRequest.setFulfillmentGroup(fulfillmentGroup);
        return fulfillmentGroupService.addItemToFulfillmentGroup(fulfillmentGroupItemRequest, false, false);
    }
    
    @Override
    public CartOperationRequest onItemUpdated(CartOperationRequest request) throws PricingException {
        Order order = request.getOrder();
        OrderItem orderItem = request.getOrderItem();
        Integer orderItemQuantityDelta = request.getOrderItemQuantityDelta();
        
        if (orderItemQuantityDelta == 0) {
            // If the quantity didn't change, nothing needs to happen
            return request;
        } else {
            List<FulfillmentGroupItem> fgisToDelete = new ArrayList<FulfillmentGroupItem>();
            fgisToDelete.addAll(updateItemQuantity(order, orderItem, orderItemQuantityDelta));
            request.setFgisToDelete(fgisToDelete);
          }
        
        return request;
    }
        
    protected List<FulfillmentGroupItem> updateItemQuantity(Order order, OrderItem orderItem, 
            Integer orderItemQuantityDelta) throws PricingException {
        List<FulfillmentGroupItem> fgisToDelete = new ArrayList<FulfillmentGroupItem>();
        boolean done = false;
        
        if (orderItemQuantityDelta > 0) {
            // If the quantity is now greater, we can simply add quantity to the first
            // fulfillment group we find that has that order item in it. 
            for (FulfillmentGroup fg : order.getFulfillmentGroups()) {
                for (FulfillmentGroupItem fgItem : fg.getFulfillmentGroupItems()) {
                    if (!done && fgItem.getOrderItem().equals(orderItem)) {
                        fgItem.setQuantity(fgItem.getQuantity() + orderItemQuantityDelta);
                        done = true;
                    }
                }
            }
        } else {
            // The quantity has been decremented. We must ensure that the appropriate number
            // of fulfillment group items are decremented as well.
            int remainingToDecrement = -1 * orderItemQuantityDelta;
            
            for (FulfillmentGroup fg : order.getFulfillmentGroups()) {
                for (FulfillmentGroupItem fgItem : fg.getFulfillmentGroupItems()) {
                    if (fgItem.getOrderItem().equals(orderItem)) {
                        if (!done &&fgItem.getQuantity() == remainingToDecrement) {
                            // Quantity matches exactly. Simply remove the item.
                            fgisToDelete.add(fgItem);
                            done = true;
                        } else if (!done && fgItem.getQuantity() > remainingToDecrement) {
                            // We have enough quantity in this fg item to facilitate the entire requsted update
                            fgItem.setQuantity(fgItem.getQuantity() - remainingToDecrement);
                            done = true;
                        } else if (!done) {
                            // We do not have enough quantity. We'll remove this item and continue searching
                            // for the remainder.
                            remainingToDecrement = remainingToDecrement - fgItem.getQuantity();
                            fgisToDelete.add(fgItem);
                        }
                    }
                }
            }
        }
        
        if (!done) {
            throw new IllegalStateException("Could not find matching fulfillment group item for the given order item");
        }
        
        return fgisToDelete;
    }

    @Override
    public CartOperationRequest onItemRemoved(CartOperationRequest request) {
        Order order = request.getOrder();
        OrderItem orderItem = request.getOrderItem();
        request.getFgisToDelete().addAll(fulfillmentGroupService.getFulfillmentGroupItemsForOrderItem(order, orderItem));
        return request;
    }
    
    @Override
    public CartOperationRequest verify(CartOperationRequest request) throws PricingException {
        Order order = request.getOrder();
        
        if (isRemoveEmptyFulfillmentGroups() && order.getFulfillmentGroups() != null) {
            ListIterator<FulfillmentGroup> fgIter = order.getFulfillmentGroups().listIterator();
            while (fgIter.hasNext()) {
                FulfillmentGroup fg = fgIter.next();
                if (fg.getFulfillmentGroupItems() == null || fg.getFulfillmentGroupItems().size() == 0) {
                    fgIter.remove();
                    fulfillmentGroupService.delete(fg);
                }
            }
        }
        
        Map<Long, Integer> oiQuantityMap = new HashMap<Long, Integer>();
        List<OrderItem> expandedOrderItems = new ArrayList<OrderItem>();
        
        for (OrderItem oi : order.getOrderItems()) {
        	if(!oi.isUsed()) continue;
            expandedOrderItems.add(oi);
           }
        
        for (OrderItem oi : expandedOrderItems) {
            Integer oiQuantity = oiQuantityMap.get(oi.getId());
            if (oiQuantity == null) {
                oiQuantity = 0;
                }
            oiQuantity += oi.getQuantity();
            oiQuantityMap.put(oi.getId(), oiQuantity);
        }
        
        for (FulfillmentGroup fg : order.getFulfillmentGroups()) {
            for (FulfillmentGroupItem fgi : fg.getFulfillmentGroupItems()) {
                Long oiId = fgi.getOrderItem().getId();
                Integer oiQuantity = oiQuantityMap.get(oiId);
                
                if (oiQuantity == null) {
                    throw new IllegalStateException("Fulfillment group items and discrete order items are not in sync. DiscreteOrderItem id: " + oiId);
                }
                
                oiQuantity -= fgi.getQuantity();
                oiQuantityMap.put(oiId, oiQuantity);
            }
        }
        
        for (Entry<Long, Integer> entry : oiQuantityMap.entrySet()) {
            if (!entry.getValue().equals(0)) {
                throw new IllegalStateException("Not enough fulfillment group items found for DiscreteOrderItem id: " + entry.getKey());
            }
        }
        
        return request;
    }

    @Override
    public boolean isRemoveEmptyFulfillmentGroups() {
        return removeEmptyFulfillmentGroups;
    }

    @Override
    public void setRemoveEmptyFulfillmentGroups(boolean removeEmptyFulfillmentGroups) {
        this.removeEmptyFulfillmentGroups = removeEmptyFulfillmentGroups;
    }

}

package cn.globalph.b2c.order.service.workflow;

import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.call.OrderItemRequestDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the basic context necessary for the execution
 * of a particular order process workflow operation.
 */
public class CartOperationRequest {

    protected OrderItemRequestDTO itemRequest;
    
    protected Order order;
    
    protected boolean priceOrder;
    
    protected boolean realDelete = true;
    
    // Set during the course of the workflow for use in subsequent workflow steps
    protected OrderItem orderItem;
    
    // Set during the course of the workflow for use in subsequent workflow steps
    protected Integer orderItemQuantityDelta;
    
    protected List<Long[]> multishipOptionsToDelete = new ArrayList<Long[]>();
    protected List<FulfillmentGroupItem> fgisToDelete = new ArrayList<FulfillmentGroupItem>();
    protected List<OrderItem> oisToDelete = new ArrayList<OrderItem>();
    
    public CartOperationRequest(Order order, OrderItemRequestDTO itemRequest, boolean priceOrder) {
        setOrder(order);
        setItemRequest(itemRequest);
        setPriceOrder(priceOrder);
        setRealDelete(true);
    }
    
    public CartOperationRequest(Order order, OrderItemRequestDTO itemRequest, boolean priceOrder, boolean realDelete) {
        setOrder(order);
        setItemRequest(itemRequest);
        setPriceOrder(priceOrder);
        setRealDelete(realDelete);
    }
    
    public OrderItemRequestDTO getItemRequest() {
        return itemRequest;
    }

    public void setItemRequest(OrderItemRequestDTO itemRequest) {
        this.itemRequest = itemRequest;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isPriceOrder() {
        return priceOrder;
    }

    public void setPriceOrder(boolean priceOrder) {
        this.priceOrder = priceOrder;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Integer getOrderItemQuantityDelta() {
        return orderItemQuantityDelta;
    }

    public void setOrderItemQuantityDelta(Integer orderItemQuantityDelta) {
        this.orderItemQuantityDelta = orderItemQuantityDelta;
    }
    
    public List<Long[]> getMultishipOptionsToDelete() {
        return multishipOptionsToDelete;
    }
    
    public void setMultishipOptionsToDelete(List<Long[]> multishipOptionsToDelete) {
        this.multishipOptionsToDelete = multishipOptionsToDelete;
    }

    public List<FulfillmentGroupItem> getFgisToDelete() {
        return fgisToDelete;
    }

    public void setFgisToDelete(List<FulfillmentGroupItem> fgisToDelete) {
        this.fgisToDelete = fgisToDelete;
    }

    public List<OrderItem> getOisToDelete() {
        return oisToDelete;
    }
    
    public void setOisToDelete(List<OrderItem> oisToDelete) {
        this.oisToDelete = oisToDelete;
    }

	public boolean isRealDelete() {
		return realDelete;
	}

	public void setRealDelete(boolean realDelete) {
		this.realDelete = realDelete;
	}    
}

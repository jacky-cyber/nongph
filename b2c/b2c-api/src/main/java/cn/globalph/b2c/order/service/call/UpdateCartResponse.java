package cn.globalph.b2c.order.service.call;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdateCartResponse implements Serializable {
    protected static final long serialVersionUID = 1L;

    protected Order order;

    protected List<OrderItem> removedItems = new ArrayList<OrderItem>();;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getRemovedItems() {
        return removedItems;
    }

    public void setRemovedItems(List<OrderItem> removedItems) {
        this.removedItems = removedItems;
    }

}

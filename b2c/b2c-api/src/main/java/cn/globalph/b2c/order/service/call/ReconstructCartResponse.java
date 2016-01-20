package cn.globalph.b2c.order.service.call;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class ReconstructCartResponse {

    private Order order;

    private List<OrderItem> removedItems = new ArrayList<OrderItem>();

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

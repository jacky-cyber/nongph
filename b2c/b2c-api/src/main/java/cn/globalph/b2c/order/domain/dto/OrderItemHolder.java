package cn.globalph.b2c.order.domain.dto;

import cn.globalph.b2c.order.domain.OrderItem;

/**
 * Class that contains a reference to an OrderItem
 * @author bpolster
 *
 */
public class OrderItemHolder {

    private OrderItem orderItem;

    public OrderItemHolder(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

}

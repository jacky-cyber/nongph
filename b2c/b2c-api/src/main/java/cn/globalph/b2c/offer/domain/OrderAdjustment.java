package cn.globalph.b2c.offer.domain;

import cn.globalph.b2c.order.domain.Order;

public interface OrderAdjustment extends Adjustment {

    public Order getOrder();

    public void init(Order order, Offer offer, String reason);

    public void setOrder(Order order);

}

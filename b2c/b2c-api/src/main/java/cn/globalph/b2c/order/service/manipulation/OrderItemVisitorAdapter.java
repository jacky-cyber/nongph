package cn.globalph.b2c.order.service.manipulation;

import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.pricing.service.exception.PricingException;

public class OrderItemVisitorAdapter implements OrderItemVisitor {

    public void visit(OrderItem orderItem) throws PricingException {
        //do nothing
    }

}

package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.order.domain.Order;

import java.util.Map;

public class CheckoutSeed implements CheckoutResponse {

    protected Order order;
    protected final Map<String, Object> userDefinedFields;

    public CheckoutSeed(Order order, Map<String, Object> userDefinedFields) {
        this.order = order;
        this.userDefinedFields = userDefinedFields;
    }

    @Override
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }

    public Map<String, Object> getUserDefinedFields() {
        return userDefinedFields;
    }
}

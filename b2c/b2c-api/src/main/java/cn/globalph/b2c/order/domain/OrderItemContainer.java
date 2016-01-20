package cn.globalph.b2c.order.domain;

import java.util.List;


public interface OrderItemContainer {

    List<? extends OrderItem> getOrderItems();

    /**
     * Returns true if the contained items can be discounted.
     * @return
     */
    boolean getAllowDiscountsOnChildItems();

    /**
     * Returns true if pricing operations are at the container level (as opposed to being
     * the sum of the contained items) 
     * @return
     */
    boolean isPricingAtContainerLevel();
}

package cn.globalph.b2c.offer.domain;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.money.Money;

import java.io.Serializable;

public interface CandidateOrderOffer extends Serializable {

    public Money getDiscountedPrice();
    
    public void setDiscountedPrice(Money discountedPrice);
    
    public Long getId();
    
    public void setId(Long id);

    public Order getOrder();

    public void setOrder(Order order);
    
    public void setOffer(Offer offer);

    public Offer getOffer();
    
    public int getPriority();
}

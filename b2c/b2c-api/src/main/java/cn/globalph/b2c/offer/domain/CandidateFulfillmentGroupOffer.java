package cn.globalph.b2c.offer.domain;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.common.money.Money;

import java.io.Serializable;

public interface CandidateFulfillmentGroupOffer extends Serializable {

    public Money getDiscountedPrice();

    public void setDiscountedPrice(Money discountedPrice);

    public FulfillmentGroup getFulfillmentGroup();

    public void setFulfillmentGroup(FulfillmentGroup fulfillmentGroup);
    
    public void setOffer(Offer offer);

    public Offer getOffer();
    
    public int getPriority();
    
}

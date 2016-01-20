package cn.globalph.b2c.offer.domain;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.common.money.Money;

public interface FulfillmentGroupAdjustment extends Adjustment {

    public FulfillmentGroup getFulfillmentGroup();

    public void init(FulfillmentGroup fulfillmentGroup, Offer offer, String reason);

    public void setValue(Money value);

    public void setFulfillmentGroup(FulfillmentGroup fulfillmentGroup);
    
}

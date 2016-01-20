package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.common.money.Money;

import java.io.Serializable;

public interface PromotableOrderAdjustment extends Serializable {

    /**
     * Returns the associated promotableOrder
     * @return
     */
    public PromotableOrder getPromotableOrder();

    /**
     * Returns the associated promotableCandidateOrderOffer
     * @return
     */
    public Offer getOffer();

    /**
     * Returns the value of this adjustment
     * @return
     */
    public Money getAdjustmentValue();

    /**
     * Returns true if this adjustment represents a combinable offer.
     */
    boolean isCombinable();

    /**
     * Returns true if this adjustment represents a totalitarian offer.
     */
    boolean isTotalitarian();
}

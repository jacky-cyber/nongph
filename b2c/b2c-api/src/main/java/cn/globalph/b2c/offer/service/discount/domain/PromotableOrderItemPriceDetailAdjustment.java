package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.common.money.Money;

import java.io.Serializable;

/**
 * This class holds adjustment records during the discount calculation 
 * processing.  This and other disposable objects avoid churn on the database while the 
 * offer engine determines the best offer(s) for the order being priced.
 * 
 * @author bpolster
 */
public interface PromotableOrderItemPriceDetailAdjustment extends Serializable {

    /**
     * Returns the associated promotableOrderItemPriceDetail
     * @return
     */
    PromotableOrderItemPriceDetail getPromotableOrderItemPriceDetail();

    /**
     * Returns the associated promotableCandidateItemOffer
     * @return
     */
    Offer getOffer();

    /**
     * Returns the value of this adjustment if only retail prices
     * can be used.
     * @return
     */
    Money getRetailAdjustmentValue();

    /**
     * Returns the value of this adjustment if sale prices
     * can be used.
     * @return
     */
    Money getSaleAdjustmentValue();

    /**
     * Returns the value of this adjustment.
     * can be used.
     * @return
     */
    Money getAdjustmentValue();

    /**
     * Returns true if the value was applied to the sale price.
     * @return
     */
    boolean isAppliedToSalePrice();

    /**
     * Returns true if this adjustment represents a combinable offer.
     */
    boolean isCombinable();

    /**
     * Returns true if this adjustment represents a totalitarian offer.   
     */
    boolean isTotalitarian();

    /**
     * Returns the id of the contained offer.
     * @return
     */
    Long getOfferId();

    /**
     * Sets the adjustment price based on the passed in parameter.
     */
    void finalizeAdjustment(boolean useSalePrice);

    /**
     * Copy this adjustment.   Used when a detail that contains this adjustment needs to be split.
     * @param discountQty
     * @param copy
     * @return
     */
    public PromotableOrderItemPriceDetailAdjustment copy();

}

package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.common.money.Money;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface PromotableFulfillmentGroup extends Serializable {

    /**
     * Adds a fulfillmentGroupAdjustment
     * @return
     */
    void addCandidateFulfillmentGroupAdjustment(PromotableFulfillmentGroupAdjustment adjustment);

    /**
     * Adds a fulfillmentGroupAdjustment
     * @return
     */
    List<PromotableFulfillmentGroupAdjustment> getCandidateFulfillmentGroupAdjustments();

    /**
     * Removes all candidate adjustments
     * @return
     */
    void removeAllCandidateAdjustments();

    /**
     * This method will check to see if the saleAdjustments or retail only adjustments are better
     * and finalize the set that achieves the best result for the customer.
     */
    void chooseSaleOrRetailAdjustments();

    /**
     * Returns the decorated FulfillmentGroup
     */
    public FulfillmentGroup getFulfillmentGroup();

    /**
     * Adds the underlying fulfillmentGroup to the rule variable map.
     */
    void updateRuleVariables(Map<String, Object> ruleVars);

    /**
     * Calculates the price with adjustments.   Uses the sale or retail adjustments
     * based on the passed in parameter.     
     * @param useSalePrice
     */
    public Money calculatePriceWithAdjustments(boolean useSalePrice);

    /**
     * Calculates the price with all adjustments.   May error in the case where adjustments have
     * not been finalized with a call to chooseSaleOrRetailAdjustments.
     * @param useSalePrice
     */
    public Money getFinalizedPriceWithAdjustments();

    /**
     * Return list of discountable discrete order items contained in this fulfillmentGroup.
     * @return
     */
    List<PromotableOrderItem> getDiscountableOrderItems();

    /**
     * Checks to see if the offer can be added to this fulfillmentGroup based on whether or not 
     * it is combinable or if this fulfillmentGroup already has a non-combinable offer applied.
     * @param fulfillmentGroupOffer
     * @return
     */
    boolean canApplyOffer(PromotableCandidateFulfillmentGroupOffer fulfillmentGroupOffer);

    /**
     * Returns the price of this fulfillment group if no adjustments were applied.
     * @return
     */
    Money calculatePriceWithoutAdjustments();
    
    /**
     * Returns true if totalitarian offer was applied to this promotable fulfillment group.
     * @return
     */
    boolean isTotalitarianOfferApplied();

}

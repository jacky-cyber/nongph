package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.OrderItemContainer;
import cn.globalph.common.money.Money;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PromotableFulfillmentGroupImpl implements PromotableFulfillmentGroup {

    private static final long serialVersionUID = 1L;
    protected FulfillmentGroup fulfillmentGroup;
    protected PromotableOrder promotableOrder;
    protected PromotableItemFactory itemFactory;
    protected List<PromotableOrderItem> discountableOrderItems;
    protected boolean useSaleAdjustments = false;
    protected Money adjustedPrice;
    
    public List<PromotableFulfillmentGroupAdjustment> candidateFulfillmentGroupAdjustments = new ArrayList<PromotableFulfillmentGroupAdjustment>();
    
    public PromotableFulfillmentGroupImpl(FulfillmentGroup fulfillmentGroup,
            PromotableOrder promotableOrder,
            PromotableItemFactory itemFactory) {
        this.fulfillmentGroup = fulfillmentGroup;
        this.promotableOrder = promotableOrder;
        this.itemFactory = itemFactory;
    }

    @Override
    public FulfillmentGroup getFulfillmentGroup() {
        return fulfillmentGroup;
    }

    @Override
    public void updateRuleVariables(Map<String, Object> ruleVars) {
        ruleVars.put("fulfillmentGroup", fulfillmentGroup);
    }

    @Override
    public List<PromotableOrderItem> getDiscountableOrderItems() {
        if (discountableOrderItems != null) {
            return discountableOrderItems;
        }
    
        discountableOrderItems = new ArrayList<PromotableOrderItem>();
        List<Long> discountableOrderItemIds = new ArrayList<Long>();
        for (FulfillmentGroupItem fgItem : fulfillmentGroup.getFulfillmentGroupItems()) {
            OrderItem orderItem = fgItem.getOrderItem();
            if (orderItem.isDiscountingAllowed()) {
                discountableOrderItemIds.add(fgItem.getOrderItem().getId());
            } else {
                if (orderItem instanceof OrderItemContainer) {
                    OrderItemContainer orderItemContainer = (OrderItemContainer) orderItem;
                    if (orderItemContainer.getAllowDiscountsOnChildItems()) {
                        for (OrderItem containedOrderItem : orderItemContainer.getOrderItems()) {
                            if (!containedOrderItem.isDiscountingAllowed()) {
                                discountableOrderItemIds.add(containedOrderItem.getId());
                            }
                        }
                    }
                }
            }
        }
            
        for (PromotableOrderItem item : promotableOrder.getDiscountableOrderItems()) {
            if (discountableOrderItemIds.contains(item.getOrderItemId())) {
                discountableOrderItems.add(item);
            }
        }
        return discountableOrderItems;
    }

    protected Money getSalePriceBeforeAdjustments() {
        Money salePrice = fulfillmentGroup.getSaleFulfillmentPrice();
        if (salePrice == null) {
            return fulfillmentGroup.getRetailFulfillmentPrice();
        } else {
            return salePrice;
        }
    }

    protected Money calculateSaleAdjustmentPrice() {
        Money returnPrice = getSalePriceBeforeAdjustments();
        for (PromotableFulfillmentGroupAdjustment adjustment : candidateFulfillmentGroupAdjustments) {
            returnPrice = returnPrice.subtract(adjustment.getSaleAdjustmentValue());
        }
        return returnPrice;
    }

    protected Money calculateRetailAdjustmentPrice() {
        Money returnPrice = fulfillmentGroup.getRetailFulfillmentPrice();
        for (PromotableFulfillmentGroupAdjustment adjustment : candidateFulfillmentGroupAdjustments) {
            returnPrice = returnPrice.subtract(adjustment.getRetailAdjustmentValue());
        }
        return returnPrice;
    }

    /**
     * This method will check to see if the salePriceAdjustments or retailPriceAdjustments are better
     * and remove those that should not apply.
     * @return 
     */
    public void chooseSaleOrRetailAdjustments() {
        this.useSaleAdjustments = Boolean.FALSE;
        Money saleAdjustmentPrice = calculateSaleAdjustmentPrice();
        Money retailAdjustmentPrice = calculateRetailAdjustmentPrice();

        if (saleAdjustmentPrice.lessThan(retailAdjustmentPrice)) {
            this.useSaleAdjustments = Boolean.TRUE;
            adjustedPrice = saleAdjustmentPrice;
        } else {
            adjustedPrice = retailAdjustmentPrice;
        }

        if (useSaleAdjustments) {
            removeRetailOnlyAdjustments();
        }

        removeZeroDollarAdjustments(useSaleAdjustments);

        finalizeAdjustments(useSaleAdjustments);

    }

    protected void finalizeAdjustments(boolean useSaleAdjustments) {
        for (PromotableFulfillmentGroupAdjustment adjustment : candidateFulfillmentGroupAdjustments) {
            adjustment.finalizeAdjustment(useSaleAdjustments);
        }
    }

    /**
     * Removes retail only adjustments.
     */
    protected void removeRetailOnlyAdjustments() {
        Iterator<PromotableFulfillmentGroupAdjustment> adjustments = candidateFulfillmentGroupAdjustments.iterator();
        while (adjustments.hasNext()) {
            PromotableFulfillmentGroupAdjustment adjustment = adjustments.next();
            if (adjustment.getPromotableCandidateFulfillmentGroupOffer().getOffer().getApplyDiscountToSalePrice() == false) {
                adjustments.remove();
            }
        }
    }

    /**
     * If removeUnusedAdjustments is s 
     * @param useSaleAdjustments
     */
    protected void removeZeroDollarAdjustments(boolean useSalePrice) {
        Iterator<PromotableFulfillmentGroupAdjustment> adjustments = candidateFulfillmentGroupAdjustments.iterator();
        while (adjustments.hasNext()) {
            PromotableFulfillmentGroupAdjustment adjustment = adjustments.next();
            if (useSalePrice) {
                if (adjustment.getSaleAdjustmentValue().isZero()) {
                    adjustments.remove();
                }
            } else {
                if (adjustment.getRetailAdjustmentValue().isZero()) {
                    adjustments.remove();
                }
            }
        }
    }

    @Override
    public Money getFinalizedPriceWithAdjustments() {
        chooseSaleOrRetailAdjustments();
        return adjustedPrice;
    }

    @Override
    public Money calculatePriceWithoutAdjustments() {
        if (fulfillmentGroup.getSaleFulfillmentPrice() != null) {
            return fulfillmentGroup.getSaleFulfillmentPrice();
        } else {
            return fulfillmentGroup.getRetailFulfillmentPrice();
        }
    }

    @Override
    public void addCandidateFulfillmentGroupAdjustment(PromotableFulfillmentGroupAdjustment adjustment) {
        candidateFulfillmentGroupAdjustments.add(adjustment);
    }

    @Override
    public List<PromotableFulfillmentGroupAdjustment> getCandidateFulfillmentGroupAdjustments() {
        return Collections.unmodifiableList(candidateFulfillmentGroupAdjustments);
    }

    @Override
    public boolean canApplyOffer(PromotableCandidateFulfillmentGroupOffer fulfillmentGroupOffer) {
        if (candidateFulfillmentGroupAdjustments.size() > 0) {
            if (!fulfillmentGroupOffer.getOffer().isCombinableWithOtherOffers()) {
                return false;
            }

            for (PromotableFulfillmentGroupAdjustment adjustment : candidateFulfillmentGroupAdjustments) {
                if (!adjustment.isCombinable()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Money calculatePriceWithAdjustments(boolean useSalePrice) {
        if (useSalePrice) {
            return calculateSaleAdjustmentPrice();
        } else {
            return calculateRetailAdjustmentPrice();
        }
    }

    @Override
    public boolean isTotalitarianOfferApplied() {
        for (PromotableFulfillmentGroupAdjustment adjustment : candidateFulfillmentGroupAdjustments) {
            if (adjustment.getPromotableCandidateFulfillmentGroupOffer().getOffer().isTotalitarianOffer()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeAllCandidateAdjustments() {
        candidateFulfillmentGroupAdjustments.clear();
    }
}

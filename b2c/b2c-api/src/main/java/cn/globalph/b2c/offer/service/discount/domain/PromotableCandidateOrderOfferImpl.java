package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferItemCriteria;
import cn.globalph.b2c.offer.service.type.OfferDiscountType;
import cn.globalph.common.money.Money;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class PromotableCandidateOrderOfferImpl implements PromotableCandidateOrderOffer {

    private static final long serialVersionUID = 1L;
    
    protected HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateQualifiersMap = new HashMap<OfferItemCriteria, List<PromotableOrderItem>>();
    protected Offer offer;
    protected PromotableOrder promotableOrder;
    protected Money potentialSavings;
    
    public PromotableCandidateOrderOfferImpl(PromotableOrder promotableOrder, Offer offer) {
        assert(offer != null);
        assert(promotableOrder != null);
        this.promotableOrder = promotableOrder;
        this.offer = offer;
        calculatePotentialSavings();
    }
    
    /**
     * Instead of calculating the potential savings, you can specify an override of this value.   
     * This is currently coded only to work if the promotableOrder's isIncludeOrderAndItemAdjustments flag
     * is true.
     *  
     * @param promotableOrder
     * @param offer
     * @param potentialSavings
     */
    public PromotableCandidateOrderOfferImpl(PromotableOrder promotableOrder, Offer offer, Money potentialSavings) {
        this(promotableOrder, offer);
        if (promotableOrder.isIncludeOrderAndItemAdjustments()) {
            this.potentialSavings = potentialSavings;
        }
    }

    @Override
    public HashMap<OfferItemCriteria, List<PromotableOrderItem>> getCandidateQualifiersMap() {
        return candidateQualifiersMap;
    }
    
    protected void calculatePotentialSavings() {
        Money amountBeforeAdjustments = promotableOrder.calculateSubtotalWithoutAdjustments();
        potentialSavings = Money.ZERO;
        if (getOffer().getDiscountType().equals(OfferDiscountType.AMOUNT_OFF)) {
            potentialSavings = new Money(getOffer().getValue());
        } else if (getOffer().getDiscountType().equals(OfferDiscountType.FIX_PRICE)) {
            potentialSavings = amountBeforeAdjustments.subtract(new Money(getOffer().getValue()));
        } else if (getOffer().getDiscountType().equals(OfferDiscountType.PERCENT_OFF)) {
            potentialSavings = amountBeforeAdjustments.multiply(getOffer().getValue().divide(new BigDecimal("100")));
           }

        if (potentialSavings.greaterThan(amountBeforeAdjustments)) {
            potentialSavings = amountBeforeAdjustments;
           }
    }
    
    @Override
    public Offer getOffer() {
        return this.offer;
    }
    
    @Override
    public PromotableOrder getPromotableOrder() {
        return this.promotableOrder;
    }
    
    @Override
    public Money getPotentialSavings() {
        return potentialSavings;
    }

    @Override
    public boolean isCombinable() {
        Boolean combinable = offer.isCombinableWithOtherOffers();
        return (combinable != null && combinable);
    }

    @Override
    public boolean isTotalitarian() {
        Boolean totalitarian = offer.isTotalitarianOffer();
        return (totalitarian != null && totalitarian.booleanValue());
    }

    @Override
    public int getPriority() {
        return offer.getPriority();
    }
}

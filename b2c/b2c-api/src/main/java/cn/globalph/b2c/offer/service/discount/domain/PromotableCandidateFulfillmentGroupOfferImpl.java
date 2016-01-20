package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferItemCriteria;
import cn.globalph.b2c.offer.service.type.OfferDiscountType;
import cn.globalph.common.money.Money;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class PromotableCandidateFulfillmentGroupOfferImpl implements PromotableCandidateFulfillmentGroupOffer {
    
    protected HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateQualifiersMap = new HashMap<OfferItemCriteria, List<PromotableOrderItem>>();
    protected Offer offer;
    protected PromotableFulfillmentGroup promotableFulfillmentGroup;
    
    public PromotableCandidateFulfillmentGroupOfferImpl(PromotableFulfillmentGroup promotableFulfillmentGroup, Offer offer) {
        assert(offer != null);
        assert(promotableFulfillmentGroup != null);
        this.offer = offer;
        this.promotableFulfillmentGroup = promotableFulfillmentGroup;
    }
    
    @Override
    public HashMap<OfferItemCriteria, List<PromotableOrderItem>> getCandidateQualifiersMap() {
        return candidateQualifiersMap;
    }

    @Override
    public void setCandidateQualifiersMap(HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateItemsMap) {
        this.candidateQualifiersMap = candidateItemsMap;
    }
    
    protected Money getBasePrice() {
        Money priceToUse = null;
        if (promotableFulfillmentGroup.getFulfillmentGroup().getRetailFulfillmentPrice() != null) {
            priceToUse = promotableFulfillmentGroup.getFulfillmentGroup().getRetailFulfillmentPrice();
            if ((offer.getApplyDiscountToSalePrice()) && (promotableFulfillmentGroup.getFulfillmentGroup().getSaleFulfillmentPrice() != null)) {
                priceToUse = promotableFulfillmentGroup.getFulfillmentGroup().getSaleFulfillmentPrice();
            }
        }
        return priceToUse;
    }
    
    @Override
    public Money computeDiscountedAmount() {
        Money discountedAmount = new Money(0);
        Money priceToUse = getBasePrice();
        if (priceToUse != null) {
            if (offer.getDiscountType().equals(OfferDiscountType.AMOUNT_OFF)) {
                discountedAmount = new Money(offer.getValue());
            } else if (offer.getDiscountType().equals(OfferDiscountType.FIX_PRICE)) {
                discountedAmount = priceToUse.subtract( new Money(offer.getValue()) );
            } else if (offer.getDiscountType().equals(OfferDiscountType.PERCENT_OFF)) {
                discountedAmount = priceToUse.multiply(offer.getValue().divide(new BigDecimal("100")));
            	}
            if (discountedAmount.greaterThan(priceToUse)) {
                discountedAmount = priceToUse;
            }
        }

        return discountedAmount;
    }
    
    @Override
    public Money getDiscountedPrice() {
        return getBasePrice().subtract(computeDiscountedAmount());
    }

    @Override
    public Money getDiscountedAmount() {
        return computeDiscountedAmount();
    }

    @Override
    public Offer getOffer() {
        return offer;
    }
    
    @Override
    public PromotableFulfillmentGroup getFulfillmentGroup() {
        return promotableFulfillmentGroup;
    }

    public int getPriority() {
        return offer.getPriority();
    }
}

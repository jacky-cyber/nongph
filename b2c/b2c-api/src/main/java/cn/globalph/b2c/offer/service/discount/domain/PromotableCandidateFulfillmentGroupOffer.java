package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferItemCriteria;
import cn.globalph.common.money.Money;

import java.util.HashMap;
import java.util.List;

public interface PromotableCandidateFulfillmentGroupOffer {

    public HashMap<OfferItemCriteria, List<PromotableOrderItem>> getCandidateQualifiersMap();

    public void setCandidateQualifiersMap(HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateItemsMap);

    public Money computeDiscountedAmount();

    public Money getDiscountedPrice();
    
    public Offer getOffer();
    
    public PromotableFulfillmentGroup getFulfillmentGroup();

    public Money getDiscountedAmount();
}

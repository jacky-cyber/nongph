package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferItemCriteria;
import cn.globalph.common.money.Money;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public interface PromotableCandidateOrderOffer extends Serializable {

    PromotableOrder getPromotableOrder();

    Offer getOffer();

    Money getPotentialSavings();

    HashMap<OfferItemCriteria, List<PromotableOrderItem>> getCandidateQualifiersMap();
    
    boolean isTotalitarian();

    boolean isCombinable();

    int getPriority();

}

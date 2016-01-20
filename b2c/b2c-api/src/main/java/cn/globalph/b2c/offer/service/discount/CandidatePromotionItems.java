package cn.globalph.b2c.offer.service.discount;

import cn.globalph.b2c.offer.domain.OfferItemCriteria;
import cn.globalph.b2c.offer.service.discount.domain.PromotableOrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CandidatePromotionItems {
    
    protected HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateQualifiersMap = new HashMap<OfferItemCriteria, List<PromotableOrderItem>>();
    protected HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateTargetsMap = new HashMap<OfferItemCriteria, List<PromotableOrderItem>>();
    protected boolean isMatchedQualifier = false;
    protected boolean isMatchedTarget = false;
    
    public void addQualifier(OfferItemCriteria criteria, PromotableOrderItem item) {
        List<PromotableOrderItem> itemList = candidateQualifiersMap.get(criteria);
        if (itemList == null) {
            itemList = new ArrayList<PromotableOrderItem>();
            candidateQualifiersMap.put(criteria, itemList);
        }
        itemList.add(item);
    }

    public void addTarget(OfferItemCriteria criteria, PromotableOrderItem item) {
        List<PromotableOrderItem> itemList = candidateTargetsMap.get(criteria);
        if (itemList == null) {
            itemList = new ArrayList<PromotableOrderItem>();
            candidateTargetsMap.put(criteria, itemList);
        }
        itemList.add(item);
    }

    public boolean isMatchedQualifier() {
        return isMatchedQualifier;
    }

    public void setMatchedQualifier(boolean isMatchedCandidate) {
        this.isMatchedQualifier = isMatchedCandidate;
    }

    public HashMap<OfferItemCriteria, List<PromotableOrderItem>> getCandidateQualifiersMap() {
        return candidateQualifiersMap;
    }
    
    public HashMap<OfferItemCriteria, List<PromotableOrderItem>> getCandidateTargetsMap() {
        return candidateTargetsMap;
    }

    public boolean isMatchedTarget() {
        return isMatchedTarget;
    }

    public void setMatchedTarget(boolean isMatchedCandidate) {
        this.isMatchedTarget = isMatchedCandidate;
    }

    public Set<PromotableOrderItem> getAllCandidateTargets() {
        Set<PromotableOrderItem> promotableOrderItemSet = new HashSet<PromotableOrderItem>();
        for (List<PromotableOrderItem> orderItems : getCandidateTargetsMap().values()) {
            promotableOrderItemSet.addAll(orderItems);
        }
        return promotableOrderItemSet;
    }
}

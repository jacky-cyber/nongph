package cn.globalph.b2c.offer.service.discount.domain;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferItemCriteria;
import cn.globalph.b2c.offer.service.processor.ItemOfferProcessor;
import cn.globalph.common.money.Money;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public interface PromotableCandidateItemOffer extends Serializable {

    public HashMap<OfferItemCriteria, List<PromotableOrderItem>> getCandidateQualifiersMap();

    public void setCandidateQualifiersMap(HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateItemsMap);

    public HashMap<OfferItemCriteria, List<PromotableOrderItem>> getCandidateTargetsMap();

    public void setCandidateTargetsMap(HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateItemsMap);

    public Money getPotentialSavings();

    public void setPotentialSavings(Money savings);

    public boolean hasQualifyingItemCriteria();

    /**
     * Public only for unit testing - not intended to be called
     */
    public Money calculateSavingsForOrderItem(PromotableOrderItem orderItem, int qtyToReceiveSavings);

    public int calculateMaximumNumberOfUses();
    
    /**
     * Returns the number of item quantities that qualified as targets for 
     * this promotion.
     */
    public int calculateTargetQuantityForTieredOffer();

    /**
     * Determines the max number of times this itemCriteria might apply.    This calculation does 
     * not take into account other promotions.   It is useful only to assist in prioritizing the order to process
     * the promotions. 
     * 
     * @param itemCriteria
     * @param promotion
     * @return
     */
    public int calculateMaxUsesForItemCriteria(OfferItemCriteria itemCriteria, Offer promotion);
    
    public int getPriority();
    
    public Offer getOffer();

    public int getUses();

    public void addUse();
    
    /**
     * Resets the uses for this candidate offer item. This is mainly used in the case where we want to calculate savings
     * and then actually apply the promotion to an item. Both scenarios run through the same logic that add uses in order
     * to determine if various quantities of items can be targeted for a particular promotion.
     * 
     * @see {@link ItemOfferProcessor#applyAndCompareOrderAndItemOffers(PromotableOrder, List, List)}
     */
    public void resetUses();
    
    public boolean isLegacyOffer();

    public List<PromotableOrderItem> getLegacyCandidateTargets();

    public void setLegacyCandidateTargets(List<PromotableOrderItem> candidateTargets);
}

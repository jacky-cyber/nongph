package cn.globalph.b2c.offer.service.discount;

import cn.globalph.b2c.offer.service.discount.domain.PromotableCandidateItemOffer;

import java.util.Comparator;

/**
 * 
 * @felix.wu
 *
 */
public class ItemOfferComparator implements Comparator<PromotableCandidateItemOffer> {
    
    public static ItemOfferComparator INSTANCE = new ItemOfferComparator();

    public int compare(PromotableCandidateItemOffer p1, PromotableCandidateItemOffer p2) {
        
        Integer priority1 = p1.getPriority();
        Integer priority2 = p2.getPriority();
        
        int result = priority1.compareTo(priority2);
        
        if (result == 0) {
            // highest potential savings wins
            return p2.getPotentialSavings().compareTo(p1.getPotentialSavings());
        }
        return result;
    }

}

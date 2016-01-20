package cn.globalph.b2c.offer.service.discount;

import cn.globalph.b2c.offer.service.discount.domain.PromotableCandidateOrderOffer;

import java.util.Comparator;

/**
 * 
 * @felix.wu
 *
 */
public class OrderOfferComparator implements Comparator<PromotableCandidateOrderOffer> {
    
    public static OrderOfferComparator INSTANCE = new OrderOfferComparator();

    public int compare(PromotableCandidateOrderOffer p1, PromotableCandidateOrderOffer p2) {
        
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

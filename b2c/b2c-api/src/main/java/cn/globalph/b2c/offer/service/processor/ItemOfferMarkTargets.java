package cn.globalph.b2c.offer.service.processor;

import cn.globalph.b2c.offer.service.discount.domain.PromotableCandidateItemOffer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableOrder;
import cn.globalph.b2c.order.domain.OrderItem;


/**
 * This interface is used as a part of a template pattern in ItemOfferProcessor that allows reuse to other BLC modules.
 * 
 * Changes here likely affect Subscription and AdvancedOffer modules.
 * @author bpolster
 *
 */
public interface ItemOfferMarkTargets {

    boolean markTargets(PromotableCandidateItemOffer itemOffer, PromotableOrder order, OrderItem relatedQualifier,
            boolean checkOnly);
}

package cn.globalph.b2c.offer.service;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableCandidateItemOffer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableOrder;
import cn.globalph.b2c.offer.service.discount.domain.PromotableOrderItem;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItemPriceDetail;
import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

import java.util.List;
import java.util.Map;


/**
 * 促销活动组件接口
 * @author felix.wu
 */
public interface OfferServiceExtensionHandler extends ExtensionHandler {
     
	 /**
	  *  对促销活动进行剔除过滤 
	  * @param offers
	  * @return
	  */
    public ExtensionResultStatusType applyAdditionalFilters(List<Offer> offers);

    /**
     * Modules may extend the calculatePotentialSavings method.   Once the handlers run, the 
     * contextMap will be checked for an entry with a key of "savings".    If that entry returns a 
     * non-null Money, that value will be returned from the calling method.
     * 
     * Otherwise, the map will be checked for an entry with a key of "quantity".   If a non-null Integer is
     * returned, that value will replace the quantity call in the normal call to calculatePotentialSavings.
     * 
     * This extension is utilized by one or more BLC enterprise modules including Subscription.
     * 
     * @param itemOffer
     * @param item
     * @param quantity
     * @param contextMap
     * @return
     */
    public ExtensionResultStatusType calculatePotentialSavings(PromotableCandidateItemOffer itemOffer,
            PromotableOrderItem item, int quantity, Map<String, Object> contextMap);

    /**
     * Modules may need to clear additional offer details when resetPriceDetails is called.
     * 
     * @param item
     * @return
     */
    public ExtensionResultStatusType resetPriceDetails(PromotableOrderItem item);

    /**
     * Modules may need to extend the applyItemOffer logic
     * 
     * For example, a subscription module might creates future payment adjustments.
     * 
     * The module add an attribute of type Boolean to the contextMap named "stopProcessing" indicating to 
     * the core offer engine that further adjustment processing is not needed. 
     * 
     * @param order
     * @param itemOffer
     * @param contextMap
     * @return
     */
    public ExtensionResultStatusType applyItemOffer(PromotableOrder order, PromotableCandidateItemOffer itemOffer,
            Map<String, Object> contextMap);

    /**
     * Allows a module to amend the data that synchronizes the {@link PromotableOrder} with the {@link Order}
     * @param order
     * @return
     */
    public ExtensionResultStatusType synchronizeAdjustmentsAndPrices(PromotableOrder order);

    /**
     * Allows a module to finalize adjustments.
     * @param order
     * @return
     */
    ExtensionResultStatusType chooseSaleOrRetailAdjustments(PromotableOrder order);

    /**
     * Allows module extensions to add a create a new instance of OrderItemPriceDetailAdjustment.  
     * The module should add the value to the resultHolder.getContextMap() with a key of "OrderItemPriceDetailAdjustment"
     * @param resultHolder
     * @return
     */
    ExtensionResultStatusType createOrderItemPriceDetailAdjustment(ExtensionResultHolder resultHolder,
            OrderItemPriceDetail itemDetail);
}

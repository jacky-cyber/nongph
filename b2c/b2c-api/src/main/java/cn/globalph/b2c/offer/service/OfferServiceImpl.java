package cn.globalph.b2c.offer.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.offer.dao.OfferDao;
import cn.globalph.b2c.offer.domain.Adjustment;
import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableCandidateFulfillmentGroupOffer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableCandidateItemOffer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableCandidateOrderOffer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableItemFactory;
import cn.globalph.b2c.offer.service.discount.domain.PromotableOrder;
import cn.globalph.b2c.offer.service.processor.FulfillmentGroupOfferProcessor;
import cn.globalph.b2c.offer.service.processor.ItemOfferProcessor;
import cn.globalph.b2c.offer.service.processor.OrderOfferProcessor;
import cn.globalph.b2c.offer.service.type.OfferType;
import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.OrderItemPriceDetail;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.passport.domain.Customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

/**
 * The Class OfferServiceImpl.
 */
@Service("blOfferService")
public class OfferServiceImpl implements OfferService {
    
    private static final Log LOG = LogFactory.getLog(OfferServiceImpl.class);
    
    @Resource(name="blOfferAuditService")
    protected OfferAuditService offerAuditService;

    @Resource(name="blOfferDao")
    protected OfferDao offerDao;
    
    @Resource(name="blOrderOfferProcessor")
    protected OrderOfferProcessor orderOfferProcessor;
    
    @Resource(name="blItemOfferProcessor")
    protected ItemOfferProcessor itemOfferProcessor;
    
    @Resource(name="blFulfillmentGroupOfferProcessor")
    protected FulfillmentGroupOfferProcessor fulfillmentGroupOfferProcessor;
    
    @Resource(name="blPromotableItemFactory")
    protected PromotableItemFactory promotableItemFactory;

    @Resource(name = "blOfferServiceExtensionManager")
    protected OfferServiceExtensionManager extensionManager;

    @Resource(name = "blOrderService")
    protected OrderService orderService;

    @Override
    public List<Offer> findAllOffers() {
        return offerDao.readAllOffers();
    }

    @Override
    @Transactional("blTransactionManager")
    public Offer save(Offer offer) {
        return offerDao.save(offer);
    }

    /**
     * Creates a list of offers that applies to this order.  All offers that are assigned to the customer,
     * entered during checkout, or has a delivery type of automatic are added to the list.  The same offer
     * cannot appear more than once in the list.
     *
     * @param order
     * @return a List of offers that may apply to this order
     */
    @Override
    public List<Offer> buildOfferListForOrder(Order order) {
        List<Offer> offers = new ArrayList<Offer>();
        /**
         * 查找用户专享促销活动
         */
        List<Offer> customerOffers = getOffersByCustomer(order.getCustomer());
        for (Offer customerOffer : customerOffers) {
            if (!offers.contains(customerOffer)) {
                offers.add(customerOffer);
           	}
        }
           
        /**
         * 查找自动生效促销活动(类型为自动生效，并且在活动有效期之内)
         */
        List<Offer> globalOffers = lookupAutomaticDeliveryOffers();
        for (Offer globalOffer : globalOffers) {
            if (!offers.contains(globalOffer) && verifyMaxCustomerUsageThreshold(order.getCustomer(), globalOffer)) {
                offers.add(globalOffer);
           	}
        }
        
        if (extensionManager != null) {
            extensionManager.getHandlerProxy().applyAdditionalFilters(offers);
        }
        
        return offers;
    }

    /**
    * Private method used to retrieve all offers with DeliveryType of AUTOMATIC
     *
    * @return a List of automatic delivery offers
     */
    protected List<Offer> lookupAutomaticDeliveryOffers() {
        List<Offer> globalOffers = offerDao.readOffersByAutomaticDeliveryType();
        return globalOffers;
     }

    /*
     *
     * Offers Logic:
     * 1) Remove all existing offers in the Order (order, item, and fulfillment)
     * 2) Check and remove offers
     *    a) Remove out of date offers
     *    b) Remove offers that do not apply to this customer
     * 3) Loop through offers
     *    a) Verifies type of offer (order, order item, fulfillment)
     *    b) Verifies if offer can be applies
     *    c) Assign offer to type (order, order item, or fulfillment)
     * 4) Sorts the order and item offers list by priority and then discount
     * 5) Identify the best offers to apply to order item and create adjustments for each item offer
     * 6) Compare order item adjustment price to sales price, and remove adjustments if sale price is better
     * 7) Identify the best offers to apply to the order and create adjustments for each order offer
     * 8) If item contains non-combinable offers remove either the item or order adjustments based on discount value
     * 9) Set final order item prices and reapply order offers
     *
     * Assumptions:
     * 1) % off all items will be created as an item offer with no expression
     * 2) $ off order will be created as an order offer
     * 3) Order offers applies to the best price for each item (not just retail price)
     * 4) Fulfillment offers apply to best price for each item (not just retail price)
     * 5) Stackable only applies to the same offer type (i.e. a not stackable order offer can be used with item offers)
     * 6) Fulfillment offers cannot be not combinable
     * 7) Order offers cannot be FIXED_PRICE
     * 8) FIXED_PRICE offers cannot be stackable
     * 9) Non-combinable offers only apply to the order and order items, fulfillment group offers will always apply
     *
     */
    @Override
    @Transactional("blTransactionManager")
    public void applyOffersToOrder(List<Offer> offers, Order order) throws PricingException {
        /*
         *TODO rather than a threadlocal, we should update the "shouldPrice" boolean on the service API to
         *use a richer object to describe the parameters of the pricing call. This object would include
         *the pricing boolean, but would also include a list of activities to include or exclude in the
         *call
         */
        OfferContext offerContext = OfferContext.getOfferContext();
        if( offerContext == null || offerContext.executePromotionCalculation ) {
            order.updatePrices();
            PromotableOrder promotableOrder = promotableItemFactory.createPromotableOrder(order, false);
            List<Offer> filteredOffers = orderOfferProcessor.filterOffers(offers, order.getCustomer());
            if ((filteredOffers == null) || (filteredOffers.isEmpty())) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("No offers applicable to this order.");
                }
            } else {
                List<PromotableCandidateOrderOffer> qualifiedOrderOffers = new ArrayList<PromotableCandidateOrderOffer>();
                List<PromotableCandidateItemOffer> qualifiedItemOffers = new ArrayList<PromotableCandidateItemOffer>();

                itemOfferProcessor.filterOffers(promotableOrder, filteredOffers, qualifiedOrderOffers, qualifiedItemOffers);

                if (! (qualifiedItemOffers.isEmpty() && qualifiedOrderOffers.isEmpty())) {                
                    // At this point, we should have a PromotableOrder that contains PromotableItems each of which
                    // has a list of candidatePromotions that might be applied.

                    // We also have a list of orderOffers that might apply and a list of itemOffers that might apply.
                    itemOfferProcessor.applyAndCompareOrderAndItemOffers(promotableOrder, qualifiedOrderOffers, qualifiedItemOffers);
                }
            }
            orderOfferProcessor.synchronizeAdjustmentsAndPrices(promotableOrder);
            order.setSubTotal(order.calculateSubTotal());
            order.finalizeItemPrices();

            orderService.save(order, false);
        }
    }

    @Override
    @Transactional("blTransactionManager")
    public void applyFulfillmentGroupOffersToOrder(List<Offer> offers, Order order) throws PricingException {
        OfferContext offerContext = OfferContext.getOfferContext();
        if (offerContext == null || offerContext.executePromotionCalculation) {
            PromotableOrder promotableOrder = promotableItemFactory.createPromotableOrder(order, true);
            List<Offer> possibleFGOffers = new ArrayList<Offer>();
            for (Offer offer : offers) {
                if (offer.getType().getType().equals(OfferType.FULFILLMENT_GROUP.getType())) {
                    possibleFGOffers.add(offer);
                }
            }
            List<Offer> filteredOffers = orderOfferProcessor.filterOffers(possibleFGOffers, order.getCustomer());
            List<PromotableCandidateFulfillmentGroupOffer> qualifiedFGOffers = new ArrayList<PromotableCandidateFulfillmentGroupOffer>();
            for (Offer offer : filteredOffers) {
                fulfillmentGroupOfferProcessor.filterFulfillmentGroupLevelOffer(promotableOrder, qualifiedFGOffers, offer);
            }
            if (!qualifiedFGOffers.isEmpty()) {
                fulfillmentGroupOfferProcessor.applyAllFulfillmentGroupOffers(qualifiedFGOffers, promotableOrder);
                fulfillmentGroupOfferProcessor.calculateFulfillmentGroupTotal(promotableOrder);
                orderOfferProcessor.synchronizeAdjustmentsAndPrices(promotableOrder);
            }

            orderService.save(order, false);
        }
    }
    
    @Override
    public boolean verifyMaxCustomerUsageThreshold(Customer customer, Offer offer) {
        if (offer.isLimitedUsePerCustomer()) {                
            Long currentUses = offerAuditService.countUsesByCustomer(customer.getId(), offer.getId());
            if (currentUses >= offer.getMaxUsesPerCustomer()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Offer> getUniqueOffersFromOrder(Order order) {
        HashSet<Offer> result = new HashSet<Offer>();
        
        Transformer adjustmentToOfferTransformer = new Transformer() {
            
            @Override
            public Object transform(Object input) {
                return ((Adjustment)input).getOffer();
            }
        };
        
        result.addAll(CollectionUtils.collect(order.getOrderAdjustments(), adjustmentToOfferTransformer));

        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
            	
                result.addAll(item.getAppliedOffers());
                
                //record usage for price details on the item as well
                if (item.getOrderItemPriceDetails() != null) {
                    for (OrderItemPriceDetail detail : item.getOrderItemPriceDetails()) {
                        result.addAll(CollectionUtils.collect(detail.getOrderItemPriceDetailAdjustments(), adjustmentToOfferTransformer));
                    }
                }
            }
        }

        if (order.getFulfillmentGroups() != null) {
            for (FulfillmentGroup fg : order.getFulfillmentGroups()) {
                result.addAll(CollectionUtils.collect(fg.getFulfillmentGroupAdjustments(), adjustmentToOfferTransformer));
            }
        }
        return result;
    }

    @Override
    public OfferDao getOfferDao() {
        return offerDao;
    }

    @Override
    public void setOfferDao(OfferDao offerDao) {
        this.offerDao = offerDao;
    }

    @Override
    public OrderOfferProcessor getOrderOfferProcessor() {
        return orderOfferProcessor;
    }

    @Override
    public void setOrderOfferProcessor(OrderOfferProcessor orderOfferProcessor) {
        this.orderOfferProcessor = orderOfferProcessor;
    }

    @Override
    public ItemOfferProcessor getItemOfferProcessor() {
        return itemOfferProcessor;
    }

    @Override
    public void setItemOfferProcessor(ItemOfferProcessor itemOfferProcessor) {
        this.itemOfferProcessor = itemOfferProcessor;
    }

    @Override
    public FulfillmentGroupOfferProcessor getFulfillmentGroupOfferProcessor() {
        return fulfillmentGroupOfferProcessor;
    }

    @Override
    public void setFulfillmentGroupOfferProcessor(FulfillmentGroupOfferProcessor fulfillmentGroupOfferProcessor) {
        this.fulfillmentGroupOfferProcessor = fulfillmentGroupOfferProcessor;
    }

    @Override
    public PromotableItemFactory getPromotableItemFactory() {
        return promotableItemFactory;
    }

    @Override
    public void setPromotableItemFactory(PromotableItemFactory promotableItemFactory) {
        this.promotableItemFactory = promotableItemFactory;
    }

    @Override
    public OrderService getOrderService() {
        return orderService;
    }

    @Override
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

	@Override
	public List<Offer> getOffersByCustomer(Customer customer) {
		// TODO 根据用户获得专属活动
		return new ArrayList<Offer>();
	}
}

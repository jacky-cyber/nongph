package cn.globalph.b2c.offer.service;

import cn.globalph.b2c.offer.dao.OfferDao;
import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableItemFactory;
import cn.globalph.b2c.offer.service.processor.FulfillmentGroupOfferProcessor;
import cn.globalph.b2c.offer.service.processor.ItemOfferProcessor;
import cn.globalph.b2c.offer.service.processor.OrderOfferProcessor;
import cn.globalph.b2c.offer.service.workflow.VerifyCustomerMaxOfferUsesActivity;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.passport.domain.Customer;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * The Interface OfferService.
 */
public interface OfferService {

    /**
     * Returns all offers
     * @return all offers
     */
    public List<Offer> findAllOffers();

    /**
     * Save a new offer or updates an existing offer
     * @param offer
     * @return the offer
     */
    public Offer save(Offer offer);

    /**
     * Apply offers to order.
     * @param offers the offers
     * @param order the order
     */
    public void applyOffersToOrder(List<Offer> offers, Order order) throws PricingException;

    /**
     * Create a list of offers that applies to this order
     * @param order
     * @return
     */
    public List<Offer> buildOfferListForOrder(Order order);

    public OfferDao getOfferDao();

    public void setOfferDao(OfferDao offerDao);

    public OrderOfferProcessor getOrderOfferProcessor();

    public void setOrderOfferProcessor(OrderOfferProcessor orderOfferProcessor);

    public ItemOfferProcessor getItemOfferProcessor();

    public void setItemOfferProcessor(ItemOfferProcessor itemOfferProcessor);

    public FulfillmentGroupOfferProcessor getFulfillmentGroupOfferProcessor();

    public void setFulfillmentGroupOfferProcessor(FulfillmentGroupOfferProcessor fulfillmentGroupOfferProcessor);
    
    public void applyFulfillmentGroupOffersToOrder(List<Offer> offers, Order order) throws PricingException;

    public PromotableItemFactory getPromotableItemFactory();

    public void setPromotableItemFactory(PromotableItemFactory promotableItemFactory);

    /**
     * <p>Validates that the passed in customer has not exceeded the max uses for the
     * passed in offer.</p>
     *
     * <p>This condition could pass if the system allows two concurrent carts for the same customer.
     * The condition will fail at order submission time when the {@link VerifyCustomerMaxOfferUsesActivity}
     * runs (if that activity is configured as part of the checkout workflow.)</p>
     *
     * <p>This method only checks offers who have a max_customer_uses value that is greater than zero.
     * By default offers can be used as many times as the customer's order qualifies.</p>
     *
     * <p>This method offers no protection against systems that allow customers to create
     * multiple ids in the system.</p>
     *
     * @param customer the customer attempting to use the offer
     * @param offer the offer to check
     * @return <b>true</b> if it is ok for the customer to use this offer with their current order, <b>false</b> if not.
     */
    public boolean verifyMaxCustomerUsageThreshold(@Nonnull Customer customer, @Nonnull Offer offer);
        
    /**
     * Returns a set of offers that have been used for this order by checking adjustments on the different levels like
     * FulfillmentGroups and OrderItems. This will return all of the unique offers used for instances where an offer can
     * apply to multiple OrderItems or multiple FulfillmentGroups (and show up as different adjustments on each)
     * 
     * @param order
     * @return
     */
    public Set<Offer> getUniqueOffersFromOrder(Order order);
    
    public OrderService getOrderService();

    public void setOrderService(OrderService orderService);
    
    /**
     *得到该用户专享活动 
    * @param customer
    * @return
     */
   public List<Offer> getOffersByCustomer(Customer customer);
}

package cn.globalph.b2c.order.domain;

import cn.globalph.b2c.coupon.domain.CouponCode;
import cn.globalph.b2c.offer.domain.CandidateOrderOffer;
import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferInfo;
import cn.globalph.b2c.offer.domain.OrderAdjustment;
import cn.globalph.b2c.order.service.call.ActivityMessageDTO;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.pricing.service.workflow.FulfillmentGroupPricingActivity;
import cn.globalph.b2c.pricing.service.workflow.TotalActivity;
import cn.globalph.b2c.product.domain.GroupOn;
import cn.globalph.b2c.product.domain.Provider;
import cn.globalph.common.audit.Auditable;
import cn.globalph.common.money.Money;
import cn.globalph.coupon.domain.CustomerCoupon;
import cn.globalph.passport.domain.Customer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Defines an order in Broadleaf.    There are several key items to be aware of with the BLC Order.
 * 
 * 1.  Carts are also Orders that are in a Pending status
 * 
 * 2.  Wishlists (and similar) are "NamedOrders"
 * 
 * 3.  Orders have several price related methods that are useful when displaying totals on the cart.
 * 3a.    getSubTotal() :  The total of all order items and their adjustments exclusive of adjustments
 * 3b.    getOrderAdjustmentsValue() :  The total of all order adjustments
 * 3C.    getTotal() : The order total (equivalent of getSubTotal() - getOrderAdjustmentsValue())
 * 
 * 4.  Order payments are represented with OrderPayment objects.
 * 
 * 5.  Order shipping (e.g. fulfillment) are represented with Fulfillment objects.
 */
public interface Order extends Serializable {

    Long getId();

    void setId(Long id);

    /**
     * Gets the name of the order, mainly in order to support wishlists.
     * 
     * @return the name of the order
     */
    String getName();

    /**
     * Sets the name of the order in the context of a wishlist. In this fashion, a {@link Customer} can have multiple
     * wishlists like "Christmas" or "Gaming Computer" etc.
     * 
     * @param name
     */
    void setName(String name);

    /**
     * Gets the auditable associated with this Order instance which tracks changes made to this Order (creation/update)
     * 
     * @return
     */
    Auditable getAuditable();

    void setAuditable(Auditable auditable);

    /**
     * Returns the subtotal price for the order.  The subtotal price is the price of all order items
     * with item offers applied.  The subtotal does not take into account the order promotions, shipping costs or any
     * taxes that apply to this order.
     *
     * @return the total item price with offers applied
     */
    Money getSubTotal();

    /**
     * Sets the subtotal price for the order.  The subtotal price is the price of all order items
     * with item offers applied.  The subtotal does not take into account the order offers or any
     * taxes that apply to this order.
     *
     * @param subTotal
     */
    void setSubTotal(Money subTotal);

    /**
     * Assigns a final price to all the order items
     */
    void assignOrderItemsFinalPrice();

    /**
     * Returns the sum of the item totals. 
     * 
     * @return
     */
    Money calculateSubTotal();

    /**
     * The grand total of this {@link Order} which includes all shipping costs and taxes, as well as any adjustments from
     * promotions.
     * 
     * @return the grand total price of this {@link Order}
     */
    Money getTotal();

    /**
     * This is getTotal() minus the sum of all the {@link OrderPayment} already applied to this order.
     * For example, Gift Cards, Account Credit, or any Third Party Order Payment that can be applied BEFORE
     * a final payment (e.g. most Credit Card Gateways) is applied. This {@link OrderPayment} does not
     * necessarily have to be confirmed (i.e. captured), as it will happen on callback of the final payment
     * and will be captured in the checkout workflow.
     *
     * This method is used in cases where you need to determine the final amount to send to the gateways
     * of what is left on the order minus what has already been applied.
     *
     * @return the total price minus any payments that have been applied to this order already.
     */
    Money getTotalAfterAppliedPayments();

    /**
     * Used in {@link TotalActivity} to set the grand total of this {@link Order}. This includes the prices of all of the
     * {@link OrderItem}s as well as any taxes, fees, shipping and adjustments for all 3.
     * 
     * @param orderTotal the total cost of this {@link Order}
     */
    void setTotal(Money orderTotal);

    /**
     * Gets the {@link Customer} for this {@link Order}.
     * 
     * @return
     */
    Customer getCustomer();

    /**
     * Sets the associated {@link Customer} for this Order.
     * 
     * @param customer
     */
    void setCustomer(Customer customer);

    OrderAddress getOrderAddress();

    void setOrderAddress(OrderAddress orderAddress);

    /**
     * Gets the status of the Order.
     * 
     * @return
     */
    OrderStatus getStatus();

    /**
     * Sets the status of the Order
     * 
     * @param status
     */
    void setStatus(OrderStatus status);

    /**
     * Gets all the {@link OrderItem}s included in this {@link Order}
     * 
     * @return
     */
    List<OrderItem> getOrderItems();

    void setOrderItems(List<OrderItem> orderItems);

    /**
     * Adds an {@link OrderItem} to the list of {@link OrderItem}s already associated with this {@link Order}
     * 
     * @param orderItem the {@link OrderItem} to add to this {@link Order}
     */
    void addOrderItem(OrderItem orderItem);

    /**
     * Gets the {@link FulfillmentGroup}s associated with this {@link Order}. An {@link Order} can have many
     * {@link FulfillmentGroup}s associated with it in order to support multi-address (and multi-type) shipping.
     * 
     * @return the {@link FulfillmentGroup}s associated with this {@link Order}
     */
    List<FulfillmentGroup> getFulfillmentGroups();

    void setFulfillmentGroups(List<FulfillmentGroup> fulfillmentGroups);

    /**
     * Sets the {@link Offer}s that could potentially apply to this {@link Order}
     * 
     * @param candidateOrderOffers
     */
    void setCandidateOrderOffers(List<CandidateOrderOffer> candidateOrderOffers);

    /**
     * Gets the {@link Offer}s that could potentially apply to this {@link Order}. Used in the promotion engine.
     * 
     * @return
     */
    List<CandidateOrderOffer> getCandidateOrderOffers();

    /**
     * Gets the date that this {@link Order} was submitted.  Note that if this date is non-null, then the following should
     * also be true:
     *  <ul>
     *      <li>{@link #getStatus()} should return {@link OrderStatus#SUBMITTED}</li>
     *      <li>{@link #getOrderNumber()} should return a non-null value</li>
     *  </ul>
     *  
     * @return
     */
    Date getSubmitDate();

    /**
     * Set the date that this {@link Order} was submitted. Used in the blCheckoutWorkflow as the last step after everything
     * else has been completed (payments charged, integration systems notified, etc).
     * 
     * @param submitDate the date that this {@link Order} was submitted.
     */
    void setSubmitDate(Date submitDate);

    Date getConfirmDate();

    void setConfirmDate(Date confirmDate);
    
    Date getCompleteDate();

    void setCompleteDate(Date completeDate);

    /**
     * Gets the total fulfillment costs that should be charged for this {@link Order}. This value should be equivalent to 
     * the summation of {@link FulfillmentGroup#getTotal()} for each {@link FulfillmentGroup} associated with this 
     * {@link Order}
     * 
     * @return the total fulfillment cost of this {@link Order}
     */
    Money getTotalFulfillmentCharges();

    /**
     * Set the total fulfillment cost of this {@link Order}. Used in the {@link FulfillmentGroupPricingActivity} after the cost
     * of each {@link FulfillmentGroup} has been calculated.
     * 
     * @param totalShipping
     */
    void setTotalFulfillmentCharges(Money totalFulfillmentCharges);

    /**
     * Gets all the {@link OrderPayment}s associated with this {@link Order}. An {@link Order} can have many
     * {@link OrderPayment}s associated with it to support things like paying with multiple cards or perhaps paying some of
     * this {@link Order} with a gift card and some with a credit card.
     * 
     * @return the {@link OrderPayment}s associated with this {@link Order}.
     */
    List<OrderPayment> getPayments();

    /**
     * Sets the various payment types associated with this {@link Order}
     * 
     * @param payments
     */
    void setPayments(List<OrderPayment> payments);

    /**
     * Returns a unmodifiable List of OrderAdjustment.  To modify the List of OrderAdjustment, please
     * use the addOrderAdjustments or removeAllOrderAdjustments methods.
     * 
     * @return a unmodifiable List of OrderItemAdjustment
     */
    List<OrderAdjustment> getOrderAdjustments();
    
    /**
     * Checks the DiscreteOrderItems in the cart and returns whether or not the given SKU was found.
     * The equality of the SKUs is based on the .equals() method in SkuImpl. This includes checking the
     * {@link DiscreteOrderItem}s from {link {@link BundleOrderItem#getDiscreteOrderItems()}
     * 
     * @param sku The sku to check for
     * @return whether or not the given SKU exists in the cart
     */
    boolean containsSku(Long skuId);

    String getFulfillmentStatus();

    /**
     * The unique number associated with this {@link Order}. Generally preferred to use instead of just using {@link #getId()}
     * since that exposes unwanted information about your database.
     * 
     * @return the unique order number for this {@link Order}
     */
    String getOrderNumber();

    /**
     * Set the unique order number for this {@link Order}
     * 
     * @param orderNumber
     */
    void setOrderNumber(String orderNumber);

    Map<Offer, OfferInfo> getAdditionalOfferInformation();

    void setAdditionalOfferInformation(Map<Offer, OfferInfo> additionalOfferInformation);

    /**
     * Returns the discount value of all the applied item offers for this order.  This value is already
     * deducted from the order subTotal.
     *
     * @return the discount value of all the applied item offers for this order
     */
    Money getItemAdjustmentsValue();

    /**
     * Returns the discount value of all the applied order offers.  The value returned from this
     * method should be subtracted from the getSubTotal() to get the order price with all item and
     * order offers applied.
     *
     * @return the discount value of all applied order offers.
     */
    Money getOrderAdjustmentsValue();

    /**
     * Returns the total discount value for all applied item and order offers in the order.  The return
     * value should not be used with getSubTotal() to calculate the final price, since getSubTotal()
     * already takes into account the applied item offers.
     *
     * @return the total discount of all applied item and order offers
     */
    Money getTotalAdjustmentsValue();

    /**
     * Updates all of the prices of the {@link OrderItem}s in this {@link Order}
     * @return <b>true</b> if at least 1 {@link OrderItem} returned true from {@link OrderItem#updatePrices}, <b>false</b>
     * otherwise.
     * @see {@link OrderItem#updatePrices()}
     */
    boolean updatePrices();
    
    /**
     * Updates the averagePriceField for all order items.
     * @return
     */
    boolean finalizeItemPrices();

    Money getFulfillmentGroupAdjustmentsValue();
        
    /**
     * This method returns the total number of items in this order. It iterates through all of the
     * discrete order items and sums up the quantity. This method is useful for display to the customer
     * the current number of "physical" items in the cart
     * 
     * @return the number of items in the order
     */
    int getItemCount();

     /**
     * Returns true if this item has order adjustments.
     * @return
       */
    boolean getHasOrderAdjustments();

    /*
     * transient field to hold order messages
     */
    List<ActivityMessageDTO> getOrderMessages();

    /*
     * transient field to hold order messages
     */
    void setOrderMessages(List<ActivityMessageDTO> orderMessages);
    
    int getUsedItemsNum();
    
    CustomerCoupon getApplyCoupon();
    
    void setApplyCoupon(CustomerCoupon coupon);
    
    List<OrderItem> getUsedOrderItems();
    
    Money getCouponDiscount();
    
    void setCouponDiscount(Money couponDiscount);
    
    Money getCouponCodeDiscount();
    
    void setCouponCodeDiscount(Money couponCodeDiscount);
    
    boolean isReview();

    void setReview(boolean isReview);

    Boolean isDeliveryInfoSent();

    void setDeliveryInfoSent(Boolean isDeliveryInfoSent);

    Boolean isShipped();

    void setShipped(Boolean isShipped);

    Order getOrder();

    void setOrder(Order order);

    List<Order> getSuborderList();

    void setSuborderList(List<Order> suborderList);

    List<OrderLog> getOrderLogs();

    void setOrderLogs(List<OrderLog> orderLogs);

    String getDeliveryType();

    void setDeliveryType(String deliveryType);

    Money getPickupOff();

    void setPickupOff(Money money);

    Provider getProvider();

    void setProvider(Provider provider);

    Boolean isRefundAvailable();
    
    Boolean isRefundOrderAvailable();
    
    Boolean getHasRefundItem();
    
    Boolean getIsGroupOn();
    
    void setIsGroupOn(Boolean isGroupOn);
    
    GroupOn getGroupOn();
    
    void setGroupOn(GroupOn groupOn);
    
    boolean isCouponAvailable();
    
    boolean isCouponCodeAvailable();
    
    boolean getNeedAddress();
    
    CouponCode getCouponCode();
    
    void setCouponCode(CouponCode couponCode);
    
    void setRefund(Refund refund);
    
    Refund getRefund();

    /*add by jenny start*/
    void setRemark(String remark);

    String getRemark();
    /*end*/

    void setDeductionBonusPoint(Integer deductionBonusPoint);

    Integer getDeductionBonusPoint();


}

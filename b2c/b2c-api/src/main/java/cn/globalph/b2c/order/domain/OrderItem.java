package cn.globalph.b2c.order.domain;

import cn.globalph.b2c.offer.domain.CandidateItemOffer;
import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.order.service.type.OrderItemType;
import cn.globalph.b2c.product.domain.PickupAddress;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.rating.domain.ReviewDetail;
import cn.globalph.common.money.Money;

import java.io.Serializable;
import java.util.List;

public interface OrderItem extends Serializable, SkuAccessor, Cloneable, Comparable<OrderItem> {

    /**
     * The unique identifier of this OrderItem
     * @return
     */
    Long getId();

    /**
     * Sets the unique id of the OrderItem.   Typically left null for new items and Broadleaf will
     * set using the next sequence number.
     * @param id
     */
    void setId(Long id);

    /**
     * Reference back to the containing order.
     * @return
     */
    Order getOrder();

    /**
     * Sets the order for this orderItem.
     * @param order
     */
    void setOrder(Order order);

    /**
     * The retail price of the item that was added to the {@link Order} at the time that this was added. This is preferable
     * to use as opposed to checking the price of the item that was added from the catalog domain (like in
     * {@link DiscreteOrderItem}, using {@link DiscreteOrderItem#getSku()}'s retail price) since the price in the catalog
     * domain could have changed since the item was added to the {@link Order}.
     * 
     * @return
     */
    Money getRetailPrice();

    /**
     * Calling this method will manually set the retailPrice.   To avoid the pricing engine
     * resetting this price, you should also make a call to 
     * {@link #setRetailPriceOverride(true)}
     * 
     * Consider also calling {@link #setDiscountingAllowed(boolean)} with a value of false to restrict
     * discounts after manually setting the retail price.    
     * 
     * @param retailPrice
     */
    void setRetailPrice(Money retailPrice);

    /**
     * Indicates that the retail price was manually set.  A typical usage might be for a 
     * CSR who has override privileges to control setting the price.   This will automatically be set
     * by calling {@link #setRetailPrice(Money)}
     */
    void setRetailPriceOverride(boolean override);

    /**
     * Returns true if the retail price was manually set.   If the retail price is manually set,
     * calls to updatePrices() will not do anything.
     * @return
     */
    boolean isRetailPriceOverride();

    /**
     * Returns the salePrice for this item.    Note this method will return the lower of the retailPrice
     * or salePrice.    It will return the retailPrice instead of null.
     * 
     * For SKU based pricing, a call to {@link #updateSaleAndRetailPrices()} will ensure that the 
     * retailPrice being used is current.
     *
     * @return
     */
    Money getSalePrice();

    /**
     * Calling this method will manually set the salePrice.    It will also make a call to 
     * {@link #setSalePriceSetManually(true)}
     * 
     *  To avoid the pricing engine resetting this price, you should also make a call to 
     *  {@link #setSalePriceOverride(true)}
     *      
     * Typically for {@link DiscreteOrderItem}s, the prices will be set with a call to {@link #updateSaleAndRetailPrices()}
     * which will use the Broadleaf dynamic pricing engine or the values directly tied to the SKU.
     * 
     * @param salePrice
     */
    void setSalePrice(Money salePrice);

    /**
     * Indicates that the sale price was manually set.  A typical usage might be for a 
     * CSR who has override privileges to control setting the price.
     * 
     * Consider also calling {@link #setDiscountingAllowed(boolean)} with a value of false to restrict
     * discounts after manually setting the retail price.   
     * 
     * If the salePrice is not lower than the retailPrice, the system will return the retailPrice when
     * a call to {@link #getSalePrice()} is made.
     */
    void setSalePriceOverride(boolean override);

    /**
     * Returns true if the sale price was manually set.   If the retail price is manually set,
     * calls to updatePrices() will not do anything.
     * @return
     */
    boolean isSalePriceOverride();

    /**
     * @deprecated 
     * Delegates to {@link #getAverageAdjustmentValue()} instead but this method is of little
     * or no value in most practical applications unless only simple promotions are being used.
     * 
     * @return
     */
    @Deprecated
    Money getAdjustmentValue();

    /**
     * @deprecated
     * Delegates to {@link #getAveragePrice()}
     * 
     * @return
     */
    @Deprecated
    Money getPrice();

    /**
     * Calling this method is the same as calling the following:
     * 
     * {@link #setRetailPrice(Money)}
     * {@link #setSalePrice(Money)}
     * {@link #setRetailPriceOverride(true)}
     * {@link #setSalePriceOverride(true)}      
     * {@link #setDiscountingAllowed(false)}          
     * 
     * This has the effect of setting the price in a way that no discounts or adjustments can be made.
     * 
     * @param price
     */
    void setPrice(Money price);

    /**
     * The quantity of this {@link OrderItem}.
     * 
     * @return
     */
    int getQuantity();

    /**
     * Sets the quantity of this item
     */
    void setQuantity(int quantity);
    
    /**
     * Collection of priceDetails for this orderItem.    
     * 
     * Without discounts, an orderItem would have exactly 1 ItemPriceDetail.   When orderItem discounting or 
     * tax-calculations result in an orderItem having multiple prices like in a buy-one-get-one free example, 
     * the orderItem will get an additional ItemPriceDetail.  
     * 
     * Generally, an OrderItem will have 1 ItemPriceDetail record for each uniquely priced version of the item.
     */
    List<OrderItemPriceDetail> getOrderItemPriceDetails();

    /**
     * Returns the list of orderItem price details.
     * @see {@link #getOrderItemPriceDetails()}
     * @param orderItemPriceDetails
     */
    void setOrderItemPriceDetails(List<OrderItemPriceDetail> orderItemPriceDetails);

    List<CandidateItemOffer> getCandidateItemOffers();

    void setCandidateItemOffers(List<CandidateItemOffer> candidateItemOffers);

    /**    
     * If any quantity of this item was used to qualify for an offer, then this returned list
     * will indicate the offer and the relevant quantity.   
     * 
     * As an example, a BuyOneGetOneFree offer would have 1 qualifier and 1 adjustment.
     * 
     * @return a List of OrderItemAdjustment
     */
    List<OrderItemQualifier> getOrderItemQualifiers();

    /**
     * Sets the list of OrderItemQualifiers
     * 
     */
    void setOrderItemQualifiers(List<OrderItemQualifier> orderItemQualifiers);

    OrderItemType getOrderItemType();

    void setOrderItemType(OrderItemType orderItemType);

    /**
     * Default implementation uses {@link #getSalePrice()} &lt; {@link #getRetailPrice()}
     * 
     * @return
     */
    boolean getIsOnSale();
    
    /**
     * If true, this item can be discounted..
     */
    boolean isDiscountingAllowed();
    
    /**
     * Turns off discount processing for this line item.
     * @param disableDiscounts
     */
    void setDiscountingAllowed(boolean discountingAllowed);

    /**
     * Returns true if this item received a discount. 
     * @return
     */
    boolean getIsDiscounted();
    
    /**
     * Generally copied from the Sku.getName()
     * @return
     */
    String getName();

    /**
     * Used to reset the base price of the item that the pricing engine uses. 
     * 
     * Generally, this will update the retailPrice and salePrice based on the 
     * corresponding value in the SKU.  
     * 
     * If the retail or sale price was manually set, this method will not change
     * those prices.
     * 
     * For non-manually set prices, prices can change based on system activities such as 
     * locale changes and customer authentication, this method is used to 
     * ensure that all cart items reflect the current base price before
     * executing other pricing / adjustment operations. 
     * 
     * Other known scenarios that can effect the base prices include the automatic bundling 
     * or loading a stale cart from the database.
     * 
     * See notes in subclasses for specific behavior of this method.
     *      
     * @return true if the base prices changed as a result of this call
     */
    boolean updateSaleAndRetailPrices();

    /**
     * Called by the pricing engine after prices have been computed.   Allows the
     * system to set the averagePrice that is stored for the item.
     */
    void finalizePrice();

    /**
     * Sets the name of this order item. 
     * @param name
     */
    void setName(String name);

    OrderItem clone();

    /**
     * Used to set the final price of the item and corresponding details.
     * @param useSalePrice
     */
    void assignFinalPrice();
    
    /**
     * Returns the unit price of this item.   If the parameter allowSalesPrice is true, will 
     * return the sale price if one exists.
     * 
     * @param allowSalesPrice
     * @return
     */
    Money getPriceBeforeAdjustments(boolean allowSalesPrice);
    
    /**
     * Used by the promotion engine to add offers that might apply to this orderItem.
     * @param candidateItemOffer
     */
    void addCandidateItemOffer(CandidateItemOffer candidateItemOffer);
    
    /**
     * Removes all candidate offers.   Used by the promotion engine which subsequently adds 
     * the candidate offers that might apply back to this item.
     */
    void removeAllCandidateItemOffers();
    
    /**
     * Returns the average unit display price for the item.  
     * 
     * Some implementations may choose to show this on a view cart screen.    Due to fractional discounts,
     * it the display could represent a unit price that is off.  
     * 
     * For example, if this OrderItem had 3 items at $1 each and also received a $1 discount.   The net 
     * effect under the default rounding scenario would be an average price of $0.666666
     * 
     * Most systems would represent this as $0.67 as the discounted unit price; however, this amount times 3 ($2.01)
     * would not equal the getTotalPrice() which would be $2.00.    For this reason, it is not recommended
     * that implementations utilize this field.    Instead, they may choose not to show the unit price or show 
     * multiple prices by looping through the OrderItemPriceDetails. 
     * 
     * @return
     */
    Money getAveragePrice();

    /**
     * Returns the average unit item adjustments.
     * 
     * For example, if this item has a quantity of 2 at a base-price of $10 and a 10% discount applies to both
     * then this method would return $1. 
     * 
     * Some implementations may choose to show this on a view cart screen.    Due to fractional discounts,
     * the display could represent a unit adjustment value that is off due to rounding.    See {@link #getAveragePrice()}
     * for an example of this.
     * 
     * Implementations wishing to show unit prices may choose instead to show the individual OrderItemPriceDetails 
     * instead of this value to avoid the rounding problem.    This would result in multiple cart item 
     * display rows for each OrderItem.
     * 
     * Alternatively, the cart display should use {@link #getTotalAdjustmentValue()}.
     * 
     * @return
     */
    Money getAverageAdjustmentValue();

    /**
     * Returns the total for all item level adjustments.
     * 
     * For example, if the item has a 2 items priced at $10 a piece and a 10% discount applies to both
     * quantities.    This method would return $2. 
     * 
     * @return
     */
    Money getTotalAdjustmentValue();

    /**
     * Returns the total price to be paid for this order item including item-level adjustments.
     * 
     * It does not include the effect of order level adjustments.   Calculated by looping through
     * the orderItemPriceDetails
     * 
     * @return
     */
    Money getTotalPrice();
    
    /**
     * Returns the total price to be paid before adjustments.
     * 
     * @return
     */
    Money getTotalPriceBeforeAdjustments(boolean allowSalesPrice);

    /**
     * Returns a boolean indicating whether this sku is active.  This is used to determine whether a user
     * the sku can add the sku to their cart.
     */
    public boolean isSkuActive();
    
    public Sku getSku();

    public void setSku(Sku sku);
    
    public List<Offer> getAppliedOffers();
	public Boolean isUsed();
	public void setIsUsed(Boolean isUsed); 
	public Boolean isReview();
	public void setIsReview(Boolean isReview);
	void setReviewDetail(ReviewDetail reviewDetial);
	ReviewDetail getReviewDetail();

    String getDeliveryType();

    void setDeliveryType(String deliveryType);

    PickupAddress getPickupAddress();

    void setPickupAddress(PickupAddress pickupAddress);
    
    public Refund getRefund();
    
    public void setRefund(Refund refund);
}

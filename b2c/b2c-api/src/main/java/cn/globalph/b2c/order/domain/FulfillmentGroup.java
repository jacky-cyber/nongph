package cn.globalph.b2c.order.domain;

import cn.globalph.b2c.offer.domain.CandidateFulfillmentGroupOffer;
import cn.globalph.b2c.offer.domain.FulfillmentGroupAdjustment;
import cn.globalph.b2c.order.service.type.FulfillmentGroupStatusType;
import cn.globalph.b2c.order.service.type.FulfillmentType;
import cn.globalph.common.money.Money;
import cn.globalph.passport.domain.Address;

import java.io.Serializable;
import java.util.List;

/**
 * This is the main entity used to hold fulfillment information about an Order. An Order can have
 * multiple FulfillmentGroups to support shipping items to multiple addresses along with fulfilling
 * items multiple ways (ship some overnight, deliver some with digital download). This constraint means
 * that a FulfillmentGroup is unique based on an Address and FulfillmentOption combination. This
 * also means that in the common case for Orders that are being delivered to a single Address and
 * a single way (shipping everything express; ie a single FulfillmentOption) then there will be
 * only 1 FulfillmentGroup for that Order.
 * 
 * @author felix.wu
 * @see {@link Order}, {@link FulfillmentOption}, {@link Address}, {@link FulfillmentGroupItem}
 */
public interface FulfillmentGroup extends Serializable {

    public Long getId();

    public void setId(Long id);

    public Order getOrder();

    public void setOrder(Order order);
    
    public void setSequence(Integer sequence);

    public Integer getSequence();

    public FulfillmentOption getFulfillmentOption();

    public void setFulfillmentOption(FulfillmentOption fulfillmentOption);

    public Address getAddress();

    public void setAddress(Address address);

    public List<FulfillmentGroupItem> getFulfillmentGroupItems();

    public void setFulfillmentGroupItems(List<FulfillmentGroupItem> fulfillmentGroupItems);

    public void addFulfillmentGroupItem(FulfillmentGroupItem fulfillmentGroupItem);

    /**
     * Returns the retail price for this fulfillmentGroup.   The retail and sale concepts used 
     * for item pricing are not generally used with fulfillmentPricing but supported 
     * nonetheless.    Typically only a retail price would be set on a fulfillment group.
     * @return
     */
    public Money getRetailFulfillmentPrice();

    /**
     * Sets the retail price for this fulfillmentGroup.   
     * @param fulfillmentPrice
     */
    public void setRetailFulfillmentPrice(Money fulfillmentPrice);

    /**
     * Returns the sale price for this fulfillmentGroup.    
     * Typically this will be null or equal to the retailFulfillmentPrice
     * @return
     */
    public Money getSaleFulfillmentPrice();

    /**
     * Sets the sale price for this fulfillmentGroup.  Typically not used.
     * @see #setRetailFulfillmentPrice(Money)
     * @param fulfillmentPrice
     */
    public void setSaleFulfillmentPrice(Money fulfillmentPrice);

    /**
     * Gets the price to charge for this fulfillmentGroup.   Includes the effects of any adjustments such as those that 
     * might have been applied by the promotion engine (e.g. free shipping)
     * @return
     */
    public Money getFulfillmentPrice();

    /**
     * Sets the price to charge for this fulfillmentGroup.  Typically set internally by the Broadleaf pricing and
     * promotion engines.
     * @return
     */
    public void setFulfillmentPrice(Money fulfillmentPrice);

    public String getReferenceNumber();

    public void setReferenceNumber(String referenceNumber);

    public FulfillmentType getType();

    void setType(FulfillmentType type);

    public List<CandidateFulfillmentGroupOffer> getCandidateFulfillmentGroupOffers();

    public void setCandidateFulfillmentGroupOffer(List<CandidateFulfillmentGroupOffer> candidateOffers);

    public void addCandidateFulfillmentGroupOffer(CandidateFulfillmentGroupOffer candidateOffer);

    public void removeAllCandidateOffers();

    public List<FulfillmentGroupAdjustment> getFulfillmentGroupAdjustments();

    public void setFulfillmentGroupAdjustments(List<FulfillmentGroupAdjustment> fulfillmentGroupAdjustments);

    public void removeAllAdjustments();

    public Money getMerchandiseTotal();

    public void setMerchandiseTotal(Money merchandiseTotal);

    public Money getTotal();

    public void setTotal(Money orderTotal);

    public FulfillmentGroupStatusType getStatus();

    public void setStatus(FulfillmentGroupStatusType status);
    
    public List<FulfillmentGroupFee> getFulfillmentGroupFees();

    public void setFulfillmentGroupFees(List<FulfillmentGroupFee> fulfillmentGroupFees);

    public void addFulfillmentGroupFee(FulfillmentGroupFee fulfillmentGroupFee);

    public void removeAllFulfillmentGroupFees();
    
    public List<OrderItem> getOrderItems();
    
    public Money getFulfillmentGroupAdjustmentsValue();

    /**
     * @return whether or not to override the shipping calculation
     */
    public Boolean getShippingOverride();

    /**
     * Sets whether or not to override the shipping calculation
     * 
     * @param shippingOverride
     */
    public void setShippingOverride(Boolean shippingOverride);
    
}

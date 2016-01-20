package cn.globalph.b2c.offer.domain;

import cn.globalph.b2c.offer.service.type.OfferDiscountType;
import cn.globalph.b2c.offer.service.type.OfferItemRestrictionRuleType;
import cn.globalph.b2c.offer.service.type.OfferType;
import cn.globalph.common.money.Money;
import cn.globalph.common.persistence.Status;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 大促，促销
 */
public interface Offer extends Status, Serializable {

    public void setId(Long id);

    public Long getId();

    public String getName();

    public void setName(String name);

    public String getDescription();

    public void setDescription(String description);

    public OfferType getType();

    public void setType(OfferType offerType);

    public OfferDiscountType getDiscountType();

    public void setDiscountType(OfferDiscountType type);

    public BigDecimal getValue();

    public void setValue(BigDecimal value);

    public int getPriority();

    public void setPriority(int priority);

    public Date getStartDate();

    public void setStartDate(Date startDate);

    public Date getEndDate();

    public void setEndDate(Date endDate);

    public String getTargetSystem();

    public void setTargetSystem(String targetSystem);

    public boolean getApplyDiscountToSalePrice();

    public void setApplyDiscountToSalePrice(boolean applyToSalePrice);
    
    public OfferItemRestrictionRuleType getOfferItemQualifierRuleType();

    public void setOfferItemQualifierRuleType(OfferItemRestrictionRuleType restrictionRuleType);
    
    public OfferItemRestrictionRuleType getOfferItemTargetRuleType();

    public void setOfferItemTargetRuleType(OfferItemRestrictionRuleType restrictionRuleType);

    /**
     * Returns false if this offer is not combinable with other offers of the same type.
     * For example, if this is an Item offer it could be combined with other Order or FG offers
     * but it cannot be combined with other Item offers.
     * 
     * @return
     */
    public boolean isCombinableWithOtherOffers();

    public void setCombinableWithOtherOffers(boolean combinableWithOtherOffers);

    /**
     * Returns true if the offer system should automatically add this offer for consideration (versus requiring a code or 
     * other delivery mechanism).    This does not guarantee that the offer will qualify.   All rules associated with this
     * offer must still pass.   A true value here just means that the offer will be considered.
     * 
     * For backwards compatibility, if the underlying property is null, this method will check the 
     * {@link #getDeliveryType()} method and return true if that value is set to AUTOMATIC.    
     * 
     * If still null, this value will return false.
     * 
     * @return
     */
    public boolean isAutomaticallyApplied();

    /**
     * Sets whether or not this offer should be automatically considered for consideration (versus requiring a code or 
     * other delivery mechanism).
     * @see #isAutomaticallyAdded()
     */
    public void setAutomaticallyApplied(boolean automaticallyAdded);

    /**
     * Returns the maximum number of times that this offer
     * can be used by the same customer.   This field
     * tracks the number of times the offer can be used
     * and not how many times it is applied.
     *
     * 0 or null indicates unlimited usage per customer.
     *
     * @return
     */
    public Long getMaxUsesPerCustomer();

    /**
     * Sets the maximum number of times that this offer
     * can be used by the same customer.  Intended as a transient
     * field that gets derived from the other persisted max uses fields
     * including maxUsesPerOrder and maxUsesPerCustomer.
     *
     * 0 or null indicates unlimited usage.
     *
     * @param maxUses
     */
    public void setMaxUsesPerCustomer(Long maxUses);

    /**
     * Indicates that there is no limit to how many times a customer can use this offer. By default this is true if
     * {@link #getMaxUsesPerCustomer()} == 0
     */
    public boolean isUnlimitedUsePerCustomer();
    
    /**
     * Whether or not this offer has limited use in an order. By default this is true if {@link #getMaxUsesPerCustomer()} > 0
     */
    public boolean isLimitedUsePerCustomer();

    /**
     * Returns the maximum number of times that this offer
     * can be used in the current order.
     *
     * 0 indicates unlimited usage.
     *
     * @deprecated use {@link #getMaxUsesPerOrder()} directly instead
     */
    @Deprecated
    public int getMaxUses() ;

    /**
     * Sets the maximum number of times that this offer
     * can be used in the current order.
     *
     * 0 indicates unlimited usage.
     *
     * @deprecated use {@link #setMaxUsesPerOrder(int)} directly instead
     */
    @Deprecated
    public void setMaxUses(int maxUses) ;

    /**
     * Returns the maximum number of times that this offer
     * can be used in the current order.
     *
     * 0 indicates unlimited usage.
     */
    public int getMaxUsesPerOrder();

    /**
     * Sets the maximum number of times that this offer
     * can be used in the current order.
     *
     * 0 indicates unlimited usage.
     *
     * @param maxUses
     */
    public void setMaxUsesPerOrder(int maxUsesPerOrder);
    
    /**
     * Indicates that there is no limit to how many times this offer can be applied to the order. By default this is true if
     * {@link #getMaxUsesPerOrder()} == 0
     */
    public boolean isUnlimitedUsePerOrder();
    
    /**
     * Whether or not this offer has limited use in an order. By default this is true if {@link #getMaxUsesPerOrder()} > 0
     */
    public boolean isLimitedUsePerOrder();

    public Set<OfferItemCriteria> getQualifyingItemCriteria();

    public void setQualifyingItemCriteria(Set<OfferItemCriteria> qualifyingItemCriteria);

    public Set<OfferItemCriteria> getTargetItemCriteria();

    public void setTargetItemCriteria(Set<OfferItemCriteria> targetItemCriteria);
    
    public Boolean isTotalitarianOffer();

    public void setTotalitarianOffer(Boolean totalitarianOffer);
    
    public Map<String, OfferRule> getOfferMatchRules();

    public void setOfferMatchRules(Map<String, OfferRule> offerMatchRules);
    
    public Boolean getTreatAsNewFormat();

    public void setTreatAsNewFormat(Boolean treatAsNewFormat);
    
    /**
     * Indicates the amount of items that must be purchased for this offer to
     * be considered for this order.
     * 
     * The system will find all qualifying items for the given offer and sum their prices before
     * any discounts are applied to make the determination.  
     * 
     * If the sum of the qualifying items is not greater than this value the offer is 
     * not considered by the offer processing algorithm.
     * @return
     */
    public Money getQualifyingItemSubTotal();
    
    public void setQualifyingItemSubTotal(Money qualifyingItemSubtotal);

    public void setMarketingMessage(String marketingMessage);

    public String getMarketingMessage();

    public Boolean getRequiresRelatedTargetAndQualifiers();

    public void setRequiresRelatedTargetAndQualifiers(Boolean requiresRelatedTargetAndQualifiers);

}

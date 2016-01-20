package cn.globalph.b2c.offer.domain;


import java.math.BigDecimal;


/**
 * Represents a tier and amount combination for an offer.   For example, an offer might allow a
 * 10% off if a user purchases 1 -5 but then allow 15% off if they purchase more than 5.    
 * @author bpolster
 *
 */
public interface OfferTier extends Comparable<OfferTier>{

    /**
     * Returns the unique id of the offer tier.
     * @return
     */
    Long getId();

    /**
     * Sets the id of the offer tier.
     * @param id
     */
    void setId(Long id);

    /**
     * Returns the amount of the offer.   The amount could be a percentage, fixed price, or amount-off
     * depending on the parent {@link Offer#getDiscountType()}
     * @return
     */
    BigDecimal getAmount();

    /**
     * Sets the amount of the tier.
     * @param amount
     */
    void setAmount(BigDecimal amount);

    /**
     * The minimum number needed to qualify for this tier.
     * @return
     */
    Long getMinQuantity();


    /**
     * Sets the minimum number need to qualify for this tier.
     * @param minQuantity
     */
    void setMinQuantity(Long minQuantity);

    /**
     * Returns the associated offer.
     * @return
     */
    Offer getOffer();

    /**
     * Sets the associated offer.
     * @param offer
     */
    void setOffer(Offer offer);

}

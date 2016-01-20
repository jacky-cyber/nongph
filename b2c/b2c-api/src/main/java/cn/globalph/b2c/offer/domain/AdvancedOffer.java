package cn.globalph.b2c.offer.domain;

import cn.globalph.b2c.offer.service.type.OfferTimeZoneType;

import java.util.List;

/**
 * Add advanced offer support to an Offer
 */
public interface AdvancedOffer {

    /**
     * List of Tiers an offer supports.   Implemented in external module.
     * @return
     */
    List<OfferTier> getOfferTiers();

    /**
     * Sets the list of Tiers.
     * @param offerTiers
     */
    void setOfferTiers(List<OfferTier> offerTiers);

    /**
     * Returns true if this is a tiered offer meaning that the amount depends on the
     * quantity being purchased.
     * @return
     */
    boolean isTieredOffer();

    /**
     * Sets whether or not this is a tiered offer.
     * @param isTieredOffer
     */
    void setTieredOffer(boolean isTieredOffer);
    
    /**
     * Sets the {@link OfferTimeZoneType} 
     * @return
     */
    public OfferTimeZoneType getOfferTimeZoneType();

    /**
     * Returns the {@link OfferTimeZoneType}
     * @param offerTimeZoneType
     */
    public void setOfferTimeZoneType(OfferTimeZoneType offerTimeZoneType);


}

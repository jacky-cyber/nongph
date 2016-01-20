package cn.globalph.b2c.offer.service.discount.domain;

import java.math.RoundingMode;

/**
 * Interface to centralize the promotion rounding functionality found in several of the Promotable classes.
 * @author bpolster
 *
 */
public interface PromotionRounding {
    
    /**
     * It is sometimes problematic to offer percentage-off offers with regards to rounding. For example,
     * consider an item that costs 9.99 and has a 50% promotion. To be precise, the offer value is 4.995,
     * but this may be a strange value to display to the user depending on the currency being used.
     */
    boolean isRoundOfferValues();

    /**
     * Returns the rounding mode to use for rounding operations. 
     * @return
     */
    RoundingMode getRoundingMode();

    /**
     * Returns the scale to use when rounding.
     * @return
     */
    int getRoundingScale();
    
}

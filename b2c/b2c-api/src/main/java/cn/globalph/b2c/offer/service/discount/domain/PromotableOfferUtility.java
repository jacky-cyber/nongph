package cn.globalph.b2c.offer.service.discount.domain;

import org.apache.commons.beanutils.BeanComparator;

import cn.globalph.b2c.offer.domain.AdvancedOffer;
import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferTier;
import cn.globalph.b2c.offer.service.type.OfferDiscountType;
import cn.globalph.common.money.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

/**
 * Provides shared code for the default implementations of PromotableOrderItemPriceDetailAdjustmentImpl and
 * PromotableCandidateItemOfferImpl
 * @author bpolster
 *
 */
public class PromotableOfferUtility {
    
    public static Money computeAdjustmentValue(Money currentPriceDetailValue, BigDecimal offerUnitValue, OfferHolder offerHolder, PromotionRounding rounding) {
        Offer offer = offerHolder.getOffer();

        OfferDiscountType discountType = offer.getDiscountType();
        Money adjustmentValue=Money.ZERO;
        
        if (OfferDiscountType.AMOUNT_OFF.equals(discountType)) {
            adjustmentValue = new Money(offerUnitValue);
           }
        
        if (OfferDiscountType.FIX_PRICE.equals(discountType)) {
            adjustmentValue = currentPriceDetailValue.subtract(new Money(offerUnitValue));
           }
        
        if (OfferDiscountType.PERCENT_OFF.equals(discountType)) {
            BigDecimal offerValue = currentPriceDetailValue.getAmount().multiply(offerUnitValue.divide(new BigDecimal("100"), 5, RoundingMode.HALF_EVEN));
            
            if (rounding.isRoundOfferValues()) {
                offerValue = offerValue.setScale(rounding.getRoundingScale(), rounding.getRoundingMode());
            	}
            adjustmentValue = new Money(offerValue);
           }

        if (currentPriceDetailValue.lessThan(adjustmentValue)) {
            adjustmentValue = currentPriceDetailValue;
           }
        return adjustmentValue;
    }


    
    @SuppressWarnings("unchecked")
    public static BigDecimal determineOfferUnitValue(Offer offer, PromotableCandidateItemOffer promotableCandidateItemOffer) {
        if (offer instanceof AdvancedOffer) {
            AdvancedOffer advancedOffer = (AdvancedOffer) offer;
            if (advancedOffer.isTieredOffer()) {
                int quantity = promotableCandidateItemOffer.calculateTargetQuantityForTieredOffer();
                List<OfferTier> offerTiers = advancedOffer.getOfferTiers();

                Collections.sort(offerTiers, new BeanComparator("minQuantity"));

                OfferTier maxTier = null;
                //assuming that promotableOffer.getOffer()).getOfferTiers() is sorted already
                for (OfferTier currentTier : offerTiers) {

                    if (quantity >= currentTier.getMinQuantity()) {
                        maxTier = currentTier;
                    } else {
                        break;
                    }
                }

                if (maxTier != null) {
                    return maxTier.getAmount();
                }

                if (OfferDiscountType.FIX_PRICE.equals(offer.getDiscountType())) {
                    // Choosing an arbitrary large value.    The retail / sale price will be less than this, 
                    // so the offer will not get selected.
                    return BigDecimal.valueOf(Integer.MAX_VALUE);
                } else {
                    return BigDecimal.ZERO;
                }
            }
        }
        return offer.getValue();
    }

}

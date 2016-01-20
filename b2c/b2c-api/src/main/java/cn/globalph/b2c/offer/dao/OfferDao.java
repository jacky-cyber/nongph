package cn.globalph.b2c.offer.dao;

import cn.globalph.b2c.offer.domain.CandidateFulfillmentGroupOffer;
import cn.globalph.b2c.offer.domain.CandidateItemOffer;
import cn.globalph.b2c.offer.domain.CandidateOrderOffer;
import cn.globalph.b2c.offer.domain.FulfillmentGroupAdjustment;
import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferInfo;
import cn.globalph.b2c.offer.domain.OrderAdjustment;
import cn.globalph.b2c.offer.domain.OrderItemPriceDetailAdjustment;

import java.util.List;

/**
 * 
 * @author felix.wu
 *
 */
public interface OfferDao {

    List<Offer> readAllOffers();

    Offer readOfferById(Long offerId);

    List<Offer> readOffersByAutomaticDeliveryType();

    Offer save(Offer offer);

    void delete(Offer offer);

    Offer create();

    CandidateOrderOffer createCandidateOrderOffer();
    
    CandidateItemOffer createCandidateItemOffer();

    CandidateFulfillmentGroupOffer createCandidateFulfillmentGroupOffer();

    OrderItemPriceDetailAdjustment createOrderItemPriceDetailAdjustment();

    OrderAdjustment createOrderAdjustment();

    FulfillmentGroupAdjustment createFulfillmentGroupAdjustment();

    OfferInfo createOfferInfo();

    OfferInfo save(OfferInfo offerInfo);

    void delete(OfferInfo offerInfo);

    /**
     * Returns the number of milliseconds that the current date/time will be cached for queries before refreshing.
     * This aids in query caching, otherwise every query that utilized current date would be different and caching
     * would be ineffective.
     *
     * @return the milliseconds to cache the current date/time
     */
    public Long getCurrentDateResolution();

    /**
     * Sets the number of milliseconds that the current date/time will be cached for queries before refreshing.
     * This aids in query caching, otherwise every query that utilized current date would be different and caching
     * would be ineffective.
     *
     * @param currentDateResolution the milliseconds to cache the current date/time
     */
    public void setCurrentDateResolution(Long currentDateResolution);
}

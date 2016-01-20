package cn.globalph.b2c.offer.service.processor;

import cn.globalph.b2c.offer.domain.Offer;

import java.util.TimeZone;

/**
 * 
 * 
 *
 */
public interface OfferTimeZoneProcessor {

    public TimeZone getTimeZone(Offer offer);
}

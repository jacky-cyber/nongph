package cn.globalph.b2c.offer.service.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.offer.domain.Offer;

import org.springframework.stereotype.Service;

import java.util.TimeZone;

/**
 */
@Service("blOfferTimeZoneProcessor")
public class OfferTimeZoneProcessorImpl implements OfferTimeZoneProcessor {

    private static final Log LOG = LogFactory.getLog(OfferTimeZoneProcessorImpl.class);

    public TimeZone getTimeZone(Offer offer) {
        return TimeZone.getDefault();
    }
}

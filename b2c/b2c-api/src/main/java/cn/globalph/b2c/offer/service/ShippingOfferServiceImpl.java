package cn.globalph.b2c.offer.service;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.pricing.service.exception.PricingException;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * 
 * @author felix.wu
 *
 */
@Service("blShippingOfferService")
public class ShippingOfferServiceImpl implements ShippingOfferService {
    
    @Resource(name="blOfferService")
    protected OfferService offerService;

    public void reviewOffers(Order order) throws PricingException {
        List<Offer> offers = offerService.buildOfferListForOrder(order);
        offerService.applyFulfillmentGroupOffersToOrder(offers, order);
    }

}

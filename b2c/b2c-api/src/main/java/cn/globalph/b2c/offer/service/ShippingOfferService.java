package cn.globalph.b2c.offer.service;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.pricing.service.exception.PricingException;

/**
 * 
 * @felix.wu
 *
 */
public interface ShippingOfferService {
    
    public void reviewOffers(Order order) throws PricingException;
    
}

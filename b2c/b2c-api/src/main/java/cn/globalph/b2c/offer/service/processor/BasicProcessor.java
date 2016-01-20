package cn.globalph.b2c.offer.service.processor;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.passport.domain.Customer;

import java.util.List;

public interface BasicProcessor {
    
	/**
	 * 过滤到无效促销
	 * @param offers
	 * @param customer
	 * @return
	 */
    public List<Offer> filterOffers(List<Offer> offers, Customer customer);
    
}

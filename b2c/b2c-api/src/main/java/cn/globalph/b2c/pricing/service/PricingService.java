package cn.globalph.b2c.pricing.service;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.pricing.service.exception.PricingException;

/**
 * 价格服务，提供价格动态计算支持。
 * 
 * @author felix.wu
 *
 */
public interface PricingService {

    public Order executePricing(Order order) throws PricingException;

}

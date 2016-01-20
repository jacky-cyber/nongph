package cn.globalph.b2c.pricing.service;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.common.vendor.service.exception.FulfillmentPriceException;

/**
 * @deprecated Should use the {@link FulfillmentOption} paradigm, implemented in {@link FulfillmentPricingService}
 * @see {@link FulfillmentPricingService}, {@link FulfillmentOption}
 */
@Deprecated
public interface ShippingService {
    
    public FulfillmentGroup calculateShippingForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException;
    
}

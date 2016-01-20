package cn.globalph.b2c.pricing.service;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.pricing.service.module.ShippingModule;
import cn.globalph.common.vendor.service.exception.FulfillmentPriceException;

/**
 * @deprecated Should use the {@link FulfillmentOption} paradigm, implemented in {@link FulfillmentPricingService}
 * @see {@link FulfillmentPricingService}, {@link FulfillmentOption}
 */
@Deprecated
public class ShippingServiceImpl implements ShippingService {

    protected ShippingModule shippingModule;

    @Override
    public FulfillmentGroup calculateShippingForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException {
        FulfillmentGroup group = shippingModule.calculateShippingForFulfillmentGroup(fulfillmentGroup);
        return group;
    }

    public ShippingModule getShippingModule() {
        return shippingModule;
    }

    public void setShippingModule(ShippingModule shippingModule) {
        this.shippingModule = shippingModule;
    }

}

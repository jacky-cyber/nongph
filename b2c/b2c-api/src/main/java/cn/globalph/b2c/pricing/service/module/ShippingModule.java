package cn.globalph.b2c.pricing.service.module;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.pricing.service.fulfillment.provider.FulfillmentPricingProvider;
import cn.globalph.common.vendor.service.exception.FulfillmentPriceException;

/**
 * @deprecated Superceded by functionality given by {@link FulfillmentOption} and {@link FulfillmentPricingProvider}
 * @see {@link FulfillmentPricingProvider}, {@link FulfillmentOption}
 */
@Deprecated
public interface ShippingModule {

    public String getName();

    public void setName(String name);

    public FulfillmentGroup calculateShippingForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException;
    
    public String getServiceName();
    
    public Boolean isValidModuleForService(String serviceName);
    
    public void setDefaultModule(Boolean isDefaultModule);
    
    public Boolean isDefaultModule();

}

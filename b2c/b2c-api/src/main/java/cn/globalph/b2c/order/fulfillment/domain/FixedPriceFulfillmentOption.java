package cn.globalph.b2c.order.fulfillment.domain;

import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.pricing.service.fulfillment.provider.FixedPriceFulfillmentPricingProvider;
import cn.globalph.common.money.Money;

import java.io.Serializable;

/**
 * Used in conjunction with the {@link FixedPriceFulfillmentPricingProvider} to allow for a single price
 * for fulfilling an order (e.g. $5 shipping)
 * 
 * @see {@link FixedPriceFulfillmentPricingProvider}
 */
public interface FixedPriceFulfillmentOption extends FulfillmentOption, Serializable {
    
    public Money getPrice();
    
    public void setPrice(Money price);
    
}

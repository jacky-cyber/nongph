package cn.globalph.b2c.pricing.service.fulfillment.provider;

import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.pricing.service.FulfillmentPricingService;
import cn.globalph.common.money.Money;

import java.util.Map;

/**
 * DTO to allow FulfillmentProcessors to respond to estimation requests for a particular FulfillmentGroup
 * for a particular FulfillmentOptions
 * 
 * @author Phillip Verheyden
 * @see {@link FulfillmentPricingProvider}, {@link FulfillmentPricingService}
 */
public class FulfillmentEstimationResponse {

    protected Map<? extends FulfillmentOption, Money> fulfillmentOptionPrices;

    public Map<? extends FulfillmentOption, Money> getFulfillmentOptionPrices() {
        return fulfillmentOptionPrices;
    }

    public void setFulfillmentOptionPrices(Map<? extends FulfillmentOption, Money> fulfillmentOptionPrices) {
        this.fulfillmentOptionPrices = fulfillmentOptionPrices;
    }
}

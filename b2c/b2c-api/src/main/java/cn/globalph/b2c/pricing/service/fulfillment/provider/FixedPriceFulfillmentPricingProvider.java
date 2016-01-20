package cn.globalph.b2c.pricing.service.fulfillment.provider;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.order.fulfillment.domain.FixedPriceFulfillmentOption;
import cn.globalph.common.money.Money;
import cn.globalph.common.vendor.service.exception.FulfillmentPriceException;

import java.util.HashMap;
import java.util.Set;

/**
 * Processor used in conjunction with {@link FixedPriceFulfillmentOption}. Simply takes the
 * flat rate defined on the option and sets that to the total shipping price of the {@link FulfillmentGroup}
 * 
 * @author felix.wu
 * @see {@link FixedPriceFulfillmentOption}
 */
public class FixedPriceFulfillmentPricingProvider implements FulfillmentPricingProvider {

    @Override
    public boolean canCalculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, FulfillmentOption option) {
        return (option instanceof FixedPriceFulfillmentOption);
    }

    @Override
    public FulfillmentGroup calculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException {
        if (canCalculateCostForFulfillmentGroup(fulfillmentGroup, fulfillmentGroup.getFulfillmentOption())) {
            Money price = ((FixedPriceFulfillmentOption)fulfillmentGroup.getFulfillmentOption()).getPrice();
            fulfillmentGroup.setRetailFulfillmentPrice(price);
            fulfillmentGroup.setSaleFulfillmentPrice(price);
            fulfillmentGroup.setFulfillmentPrice(price);
            return fulfillmentGroup;
        }

        throw new IllegalArgumentException("Cannot estimate shipping cost for the fulfillment option: "
                + fulfillmentGroup.getFulfillmentOption().getClass().getName());
    }

    @Override
    public FulfillmentEstimationResponse estimateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, Set<FulfillmentOption> options) throws FulfillmentPriceException {

        FulfillmentEstimationResponse response = new FulfillmentEstimationResponse();
        HashMap<FulfillmentOption, Money> shippingPrices = new HashMap<FulfillmentOption, Money>();
        response.setFulfillmentOptionPrices(shippingPrices);

        for (FulfillmentOption option : options) {
            if (canCalculateCostForFulfillmentGroup(fulfillmentGroup, option)) {
                Money price = ((FixedPriceFulfillmentOption) option).getPrice();
                shippingPrices.put(option, price);
            }
        }

        return response;
    }

}

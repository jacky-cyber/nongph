package cn.globalph.b2c.pricing.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.order.service.FulfillmentGroupService;
import cn.globalph.b2c.pricing.service.fulfillment.provider.FulfillmentEstimationResponse;
import cn.globalph.b2c.pricing.service.fulfillment.provider.FulfillmentPricingProvider;
import cn.globalph.common.money.Money;
import cn.globalph.common.vendor.service.exception.FulfillmentPriceException;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service("blFulfillmentPricingService")
public class FulfillmentPricingServiceImpl implements FulfillmentPricingService {
    
    protected static final Log LOG  = LogFactory.getLog(FulfillmentPricingServiceImpl.class);

    @Resource(name = "blFulfillmentPricingProviders")
    protected List<FulfillmentPricingProvider> providers;
    
    @Resource(name = "blFulfillmentGroupService")
    protected FulfillmentGroupService fulfillmentGroupService;

    @Override
    public FulfillmentGroup calculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException {

        if (fulfillmentGroup.getFulfillmentOption() == null) {
            //There is no shipping option yet. We'll simply set the shipping price to zero for now, and continue.
            fulfillmentGroup.setRetailFulfillmentPrice(Money.ZERO);
            fulfillmentGroup.setFulfillmentPrice(Money.ZERO);
            fulfillmentGroup.setSaleFulfillmentPrice(Money.ZERO);
            return fulfillmentGroup;
        }
        
        for (FulfillmentPricingProvider provider : providers) {
            if (provider.canCalculateCostForFulfillmentGroup(fulfillmentGroup, fulfillmentGroup.getFulfillmentOption())) {
                return provider.calculateCostForFulfillmentGroup(fulfillmentGroup);
            }
        }

        throw new FulfillmentPriceException("No valid processor was found to calculate the FulfillmentGroup cost with " +
                "FulfillmentOption id: " + fulfillmentGroup.getFulfillmentOption().getId() + 
                        " and name: " + fulfillmentGroup.getFulfillmentOption().getName());
    }
    
    @Override
    public FulfillmentEstimationResponse estimateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, Set<FulfillmentOption> options) throws FulfillmentPriceException {
        FulfillmentEstimationResponse response = new FulfillmentEstimationResponse();
        HashMap<FulfillmentOption, Money> prices = new HashMap<FulfillmentOption, Money>();
        response.setFulfillmentOptionPrices(prices);
        for (FulfillmentPricingProvider provider : providers) {
            //Leave it up to the providers to determine if they can respond to a pricing estimate.  If they can't, or if one or more of the options that are passed in can't be responded
            //to, then the response from the pricing provider should not include the options that it could not respond to.
            try {
                FulfillmentEstimationResponse processorResponse = provider.estimateCostForFulfillmentGroup(fulfillmentGroup, options);
                if (processorResponse != null
                        && processorResponse.getFulfillmentOptionPrices() != null
                        && processorResponse.getFulfillmentOptionPrices().size() > 0) {
                    prices.putAll(processorResponse.getFulfillmentOptionPrices());
                }
            } catch (FulfillmentPriceException e) {
                //Shouldn't completely fail the rest of the estimation on a pricing exception. Another provider might still
                //be able to respond
                String errorMessage = "FulfillmentPriceException thrown when trying to estimate fulfillment costs from ";
                errorMessage += provider.getClass().getName();
                errorMessage += ". Underlying message was: " + e.getMessage();
                LOG.error(errorMessage);
            }
        }

        return response;
    }

    @Override
    public List<FulfillmentPricingProvider> getProviders() {
        return providers;
    }

    public void setProviders(List<FulfillmentPricingProvider> providers) {
        this.providers = providers;
    }

}

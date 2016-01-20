package cn.globalph.b2c.pricing.service;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.pricing.service.fulfillment.provider.FulfillmentEstimationResponse;
import cn.globalph.b2c.pricing.service.fulfillment.provider.FulfillmentPricingProvider;
import cn.globalph.b2c.pricing.service.workflow.FulfillmentGroupMerchandiseTotalActivity;
import cn.globalph.common.vendor.service.exception.FulfillmentPriceException;

import java.util.List;
import java.util.Set;

/**
 * This service can be used in a couple of different ways. First, this is used in the pricing workflow and specifically
 * {@link FulfillmentGroupMerchandiseTotalActivity} to calculate costs for {@link FulfillmentGroup}s in an {@link Order}. This can
 * also be injected in a controller to provide estimations for various {@link FulfillmentOption}s to display to the user
 * before an option is actually selected.
 * 
 * @author Phillip Verheyden
 */
public interface FulfillmentPricingService {

    /**
     * Called during the Pricing workflow to determine the cost for the {@link FulfillmentGroup}. This will loop through
     * {@link #getProcessors()} and call {@link FulfillmentPricingProvider#calculateCostForFulfillmentGroup(FulfillmentGroup)}
     * on the first processor that returns true from {@link FulfillmentPricingProvider#canCalculateCostForFulfillmentGroup(FulfillmentGroup)}
     * 
     * @param fulfillmentGroup
     * @return the updated </b>fulfillmentGroup</b> with its shippingPrice set
     * @throws FulfillmentPriceException if <b>fulfillmentGroup</b> does not have a FulfillmentOption associated to it or
     * if there was no processor found to calculate costs for <b>fulfillmentGroup</b>
     * @see {@link FulfillmentPricingProvider}
     */
    public FulfillmentGroup calculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException;

    /**
     * This provides an estimation for a {@link FulfillmentGroup} with a {@link FulfillmentOption}. The main use case for this method
     * is in a view cart controller that wants to provide estimations for different {@link FulfillmentOption}s before the user
     * actually selects one. This uses {@link #getProviders()} to allow third-party integrations to respond to
     * estimations, and returns the first processor that returns true from {@link FulfillmentPricingProvider#canCalculateCostForFulfillmentGroup(FulfillmentGroup, FulfillmentOption)}.
     * 
     * @param fulfillmentGroup
     * @param options
     * @return the price estimation for a particular {@link FulfillmentGroup} with a candidate {@link FulfillmentOption}
     * @throws FulfillmentPriceException if no processor was found to estimate costs for <b>fulfillmentGroup</b> with the given <b>option</b>
     * @see {@link FulfillmentPricingProvider}
     */
    public FulfillmentEstimationResponse estimateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, Set<FulfillmentOption> options) throws FulfillmentPriceException;
    
    public List<FulfillmentPricingProvider> getProviders();

}

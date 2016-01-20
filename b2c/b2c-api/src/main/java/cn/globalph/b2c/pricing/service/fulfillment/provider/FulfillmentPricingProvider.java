package cn.globalph.b2c.pricing.service.fulfillment.provider;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.pricing.service.FulfillmentPricingService;
import cn.globalph.b2c.pricing.service.workflow.FulfillmentGroupPricingActivity;
import cn.globalph.common.vendor.service.exception.FulfillmentPriceException;

import java.util.Set;

/**
 * Main extension interface to allow third-party integrations to respond to fulfillment pricing
 * 
 * @author Phillip Verheyden
 * @see {@link FulfillmentPricingService}, {@link FulfillmentGroupPricingActivity}
 */
public interface FulfillmentPricingProvider {

    /**
     * Calculates the total cost for this FulfillmentGroup. Specific configurations for calculating
     * this cost can come from {@link FulfillmentGroup#getFulfillmentOption()}. This method is invoked
     * during the pricing workflow and will only be called if {@link #canCalculateCostForFulfillmentGroup(FulfillmentGroup, FulfillmentOption)}
     * returns true. This should call {@link FulfillmentGroup#setShippingPrice(cn.globalph.common.money.Money)} to
     * set the shipping price on <b>fulfillmentGroup</b>
     * 
     * @param fulfillmentGroup - the {@link FulfillmentGroup} to calculate costs for
     * @return the modified {@link FulfillmentGroup} with correct pricing. This is typically <b>fulfillmentGroup</b> after it
     * has been modified
     */
    public FulfillmentGroup calculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup) throws FulfillmentPriceException;

    /**
     * Whether or not this processor can provide a cost calculate for the given FulfillmentGroup and the given
     * FulfillmentOption. This is not invoked directly by any workflow, but could instead be invoked via a controller
     * that wants to display pricing to a user before the user actually picks a FulfillmentOption. The controller would
     * inject an instance of FulfillmentPricingService  and thus indirectly invoke this method for a particular option.
     *
     * @param fulfillmentGroup
     * @param option - the candidate option a user might select based on the estimate
     * @return <b>true</b> if this processor can estimate the costs, <b>false</b> otherwise
     * @see {@link FulfillmentPricingService}, {@link FulfillmentOption}
     */
    public boolean canCalculateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, FulfillmentOption option);

    /**
     * Estimates the cost for the fulfilling the given fulfillment group
     * Estimates the cost for the fulfilling the given fulfillment group with the given options. The response should not include prices that the implementor of this interface
     * cannot respond to.  So, if the invoker of this method passes in several types of fulfillment options, the response should only contain prices for the fulfillment options
     * that will would cause a call to
     * {@link #canCalculateCostForFulfillmentGroup(cn.globalph.b2c.order.domain.FulfillmentGroup, cn.globalph.b2c.order.domain.FulfillmentOption)}
     * to return true.  This method may return null or it may return a non-null response with an empty map, indicating that no price estimate was available for the options given. This
     * method SHOULD NOT throw an exception if it encounters a FulfillmentOption that it can not price. It should simply ignore that option.
     * 
     * @param fulfillmentGroup - the group to estimate fulfillment costs for
     * @param options - the candidate options that a user might select
     * @return a DTO that represents pricing information that might be added to the fulfillment cost of <b>fulfillmentGroup</b> when
     * {@link #calculateCostForFulfillmentGroup(FulfillmentGroup)} is invoked during the pricing workflow
     * @see {@link FulfillmentPricingService}, {@link FulfillmentOption}
     */
    public FulfillmentEstimationResponse estimateCostForFulfillmentGroup(FulfillmentGroup fulfillmentGroup, Set<FulfillmentOption> options) throws FulfillmentPriceException;
    
}

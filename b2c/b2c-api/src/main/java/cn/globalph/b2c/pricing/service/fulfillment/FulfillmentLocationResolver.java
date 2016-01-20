package cn.globalph.b2c.pricing.service.fulfillment;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.passport.domain.Address;

/**
 * This can be used by various third-party fulfillment pricing services in order to
 * resolve a location that items will be shipped from in order to properly calculate the
 * cost of fulfilling that particular fulfillment group.
 * 
 * <p>Note: the bean name in XML should be blFulfillmentLocationResolver
 * 
 * @author Phillip Verheyden
 * @see {@link SimpleFulfillmentLocationResolver}
 */
public interface FulfillmentLocationResolver {

    /**
     * This method should give an {@link Address} that a particular {@link FulfillmentGroup} will
     * be fulfilled from. Implementations could store this information in the database or integrate
     * with an existing warehouse solution.
     * 
     * @param group
     * @return the {@link Address} that <b>group</b> should be fulfilled from
     */
    public Address resolveLocationForFulfillmentGroup(FulfillmentGroup group);

}

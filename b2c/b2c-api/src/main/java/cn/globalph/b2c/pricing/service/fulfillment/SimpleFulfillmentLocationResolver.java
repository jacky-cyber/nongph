package cn.globalph.b2c.pricing.service.fulfillment;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.passport.domain.Address;

/**
 * Default implementation of {@link FulfillmentLocationResolver} that stores a
 * single Address. Useful for businesses that do not have a complicated warehouse solution
 * and fulfill from a single location.
 * 
 * @author Phillip Verheyden
 */
public class SimpleFulfillmentLocationResolver implements FulfillmentLocationResolver {

    protected Address address;

    @Override
    public Address resolveLocationForFulfillmentGroup(FulfillmentGroup group) {
        return address;
    }

    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }

}

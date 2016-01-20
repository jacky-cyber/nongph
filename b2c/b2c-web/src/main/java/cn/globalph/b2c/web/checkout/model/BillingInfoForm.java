package cn.globalph.b2c.web.checkout.model;

import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.AddressImpl;

import java.io.Serializable;

/**
 * <p>A form to model adding the Billing Address to the Order</p>
 */
public class BillingInfoForm implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Address address = new AddressImpl();
    protected boolean useShippingAddress;

    public BillingInfoForm() {
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isUseShippingAddress() {
        return useShippingAddress;
    }

    public void setUseShippingAddress(boolean useShippingAddress) {
        this.useShippingAddress = useShippingAddress;
    }
}

package cn.globalph.b2c.web.checkout.model;

import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.AddressImpl;

import java.io.Serializable;

/**
 * A form to model adding a shipping address with shipping options.
 */
public class ShippingInfoForm implements Serializable {

    private static final long serialVersionUID = 1L;

	protected Address address = new AddressImpl();
    protected String addressName;
    protected FulfillmentOption fulfillmentOption;
    protected Long fulfillmentOptionId;

    public ShippingInfoForm() {
    }
    
    public Long getFulfillmentOptionId() {
        return fulfillmentOptionId;
    }
    
    public void setFulfillmentOptionId(Long fulfillmentOptionId) {
        this.fulfillmentOptionId = fulfillmentOptionId;
    }
    
    public FulfillmentOption getFulfillmentOption() {
        return fulfillmentOption;
    }

    public void setFulfillmentOption(FulfillmentOption fulfillmentOption) {
        this.fulfillmentOption = fulfillmentOption;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    } 
}

package cn.globalph.b2c.web.checkout.model;

import java.io.Serializable;

/**
 * This form is used to bind multiship options in a way that doesn't require
 * the actual objects to be instantiated -- we handle that at the controller
 * level.
 * 
 * 
 */
public class MultiShipInstructionForm implements Serializable {

    private static final long serialVersionUID = 1L;
    
    protected String deliveryMessage;
    protected Long fulfillmentGroupId;
    
    public String getDeliveryMessage() {
        return deliveryMessage;
    }
    
    public void setDeliveryMessage(String deliveryMessage) {
        this.deliveryMessage = deliveryMessage;
    }
    
    public Long getFulfillmentGroupId() {
        return fulfillmentGroupId;
    }

    public void setFulfillmentGroupId(Long id) {
        this.fulfillmentGroupId = id;
    }
    
}

package cn.globalph.b2c.order.service.call;

/**
 * This DTO is used to bind multiship options in a way that doesn't require
 * the actual objects to be instantiated -- we handle that at the controller
 * level.
 * 
 * @see OrderMultishipOptionForm
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class OrderMultishipOptionDTO {

    protected Long id;
    protected Long orderItemId;
    protected Long addressId;
    protected Long fulfillmentOptionId;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderItemId() {
        return orderItemId;
    }
    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }
    public Long getAddressId() {
        return addressId;
    }
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
    public Long getFulfillmentOptionId() {
        return fulfillmentOptionId;
    }
    public void setFulfillmentOptionId(Long fulfillmentOptionId) {
        this.fulfillmentOptionId = fulfillmentOptionId;
    }

}

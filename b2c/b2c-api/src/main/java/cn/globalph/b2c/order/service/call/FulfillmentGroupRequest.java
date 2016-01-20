package cn.globalph.b2c.order.service.call;

import cn.globalph.b2c.order.domain.FulfillmentOption;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.type.FulfillmentType;
import cn.globalph.passport.domain.Address;

import java.util.ArrayList;
import java.util.List;

public class FulfillmentGroupRequest {

    protected Address address;
    protected Order order;
    
    /**
     * Both of these fields uses are superceded by the FulfillmentOption paradigm
     */
    @Deprecated
    protected String method;
    @Deprecated
    protected String service;
    
    protected FulfillmentOption option;
    
    protected List<FulfillmentGroupItemRequest> fulfillmentGroupItemRequests = new ArrayList<FulfillmentGroupItemRequest>();

    protected FulfillmentType fulfillmentType;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    
    public FulfillmentOption getOption() {
        return option;
    }
    
    public void setOption(FulfillmentOption option) {
        this.option = option;
    }

    public List<FulfillmentGroupItemRequest> getFulfillmentGroupItemRequests() {
        return fulfillmentGroupItemRequests;
    }

    public void setFulfillmentGroupItemRequests(List<FulfillmentGroupItemRequest> fulfillmentGroupItemRequests) {
        this.fulfillmentGroupItemRequests = fulfillmentGroupItemRequests;
    }

    public FulfillmentType getFulfillmentType() {
        return fulfillmentType;
    }

    public void setFulfillmentType(FulfillmentType fulfillmentType) {
        this.fulfillmentType = fulfillmentType;
    }

    /**
     * Deprecated in favor of {@link #getOption()}
     * @see {@link FulfillmentOption}
     */
    @Deprecated
    public String getMethod() {
        return method;
    }

    /**
     * Deprecated in favor of {@link #setOption(FulfillmentOption)}
     * @see {@link FulfillmentOption}
     */    
    @Deprecated
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Deprecated in favor of {@link #getOption()}
     * @see {@link FulfillmentOption}
     */
    @Deprecated
    public String getService() {
        return service;
    }

    /**
     * Deprecated in favor of {@link #setOption(FulfillmentOption)}
     * @see {@link FulfillmentOption}
     */    
    @Deprecated
    public void setService(String service) {
        this.service = service;
    }

}

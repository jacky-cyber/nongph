package cn.globalph.b2c.order.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of fulfillment group status types.
 * 
 * @author aangus
 *
 */
public class FulfillmentGroupStatusType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, FulfillmentGroupStatusType> TYPES = new LinkedHashMap<String, FulfillmentGroupStatusType>();
    
    /**
     * Use FULFILLED, PARTIALLY_FULFILLED, DELIVERED, or PARTIALLY_DELIVERED
     * @deprecated
     */
    @Deprecated
    public static final FulfillmentGroupStatusType SHIPPED = new FulfillmentGroupStatusType("SHIPPED", "卖家已发货");
    
    /**
     * PROCESSING: Used to indicate that the fulfillment group is being processed. For example, during pick or pack processes 
     * in a warehouse.
     */
    public static final FulfillmentGroupStatusType PROCESSING = new FulfillmentGroupStatusType("PROCESSING", "处理中");
    
    /**
     * FULFILLED: Used to indicate that the Fulfillment Group is completely fulfilled (e.g. shipped, downloaded, etc.). For some systems, 
     * this will be the final status on a fulfillment group. For others that want to differentiate between FULFILLED and DELIVERED, usually 
     * to differentiate between items that have been shipped vs. items that have been received by the customer.
     */
    public static final FulfillmentGroupStatusType FULFILLED = new FulfillmentGroupStatusType("FULFILLED", "配送中");
    
    /**
     * PARTIALLY_FULFILLED: Used to indicate that one or more items has been fulfilled or partially fulfilled, but that there 
     * are some items in the fulfillment group that are not fulfilled.
     */
    public static final FulfillmentGroupStatusType PARTIALLY_FULFILLED = new FulfillmentGroupStatusType("PARTIALLY_FULFILLED", "部分配送中");
    
    /**
     * DELIVERED: Used to indicate that all items in the fulfillment group have been delivered. This will generally only be used when there is some 
     * integration with a shipping or fulfillment system to indicate that an item has actually been received by the customer.
     */
    public static final FulfillmentGroupStatusType DELIVERED = new FulfillmentGroupStatusType("DELIVERED", "买家已收货");
    
    /**
     * PARTIALLY_DELIVERED: Indicates that an item or a FulfillemntGroup has been partially received by the customer.
     */
    public static final FulfillmentGroupStatusType PARTIALLY_DELIVERED = new FulfillmentGroupStatusType("PARTIALLY_DELIVERED", "部分买家已收货");
    
    public static FulfillmentGroupStatusType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public FulfillmentGroupStatusType() {
        //do nothing
    }

    public FulfillmentGroupStatusType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getFriendlyType() {
        return friendlyType;
    }

    private void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FulfillmentGroupStatusType other = (FulfillmentGroupStatusType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

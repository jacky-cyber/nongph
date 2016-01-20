package cn.globalph.b2c.order.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of order item types.
 * 
 * @felix.wu
 */
public class OrderItemType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, OrderItemType> TYPES = new LinkedHashMap<String, OrderItemType>();

    public static final OrderItemType BASIC  = new OrderItemType("cn.globalph.b2c.order.domain.OrderItem", "Basic Order Item");
    public static final OrderItemType EXTERNALLY_PRICED  = new OrderItemType("cn.globalph.b2c.order.domain.DynamicPriceDiscreteOrderItem", "Externally Priced Order Item");
    public static final OrderItemType GIFTWRAP = new OrderItemType("cn.globalph.b2c.order.domain.GiftWrapOrderItem", "Gift Wrap Order Item");

    public static OrderItemType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public OrderItemType() {
        //do nothing
    }

    public OrderItemType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    public String getType() {
        return type;
    }

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
        OrderItemType other = (OrderItemType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

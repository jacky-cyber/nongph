package cn.globalph.b2c.order.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of fulfillment group types.
 */
public class FulfillmentType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, FulfillmentType> TYPES = new LinkedHashMap<String, FulfillmentType>();

    public static final FulfillmentType DIGITAL = new FulfillmentType("DIGITAL", "数字产品");
    public static final FulfillmentType PHYSICAL_SHIP = new FulfillmentType("PHYSICAL_SHIP", "送货上门");
    public static final FulfillmentType PHYSICAL_PICKUP = new FulfillmentType("PHYSICAL_PICKUP", "网点自提");
    public static final FulfillmentType PHYSICAL_PICKUP_OR_SHIP = new FulfillmentType("PHYSICAL_PICKUP_OR_SHIP", "送货上门或网点自提");

    public static FulfillmentType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public FulfillmentType() {
        //do nothing
    }

    public FulfillmentType(final String type, final String friendlyType) {
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
        FulfillmentType other = (FulfillmentType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

package cn.globalph.b2c.delivery;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of container permission types.
 *
 * @felix.wu
 */
public class DeliveryType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, DeliveryType> TYPES = new LinkedHashMap<String, DeliveryType>();

    public static final DeliveryType LOGISTICS = new DeliveryType("LOGISTICS", "物流");
    public static final DeliveryType EXPRESS = new DeliveryType("EXPRESS", "快递");
    public static final DeliveryType PICKUP = new DeliveryType("PICKUP", "自提");

    public static DeliveryType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public DeliveryType() {
        //do nothing
    }

    public DeliveryType(final String type, final String friendlyType) {
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
        } else {
            throw new RuntimeException("Cannot add the type: (" + type + "). It already exists as a type via " + getInstance(type).getClass().getName());
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
        DeliveryType other = (DeliveryType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

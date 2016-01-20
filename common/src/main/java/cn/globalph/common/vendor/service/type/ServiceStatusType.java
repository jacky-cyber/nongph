package cn.globalph.common.vendor.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of service status types.
 * 
 * @felix.wu
 *
 */
public class ServiceStatusType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, ServiceStatusType> TYPES = new LinkedHashMap<String, ServiceStatusType>();

    public static final ServiceStatusType UP  = new ServiceStatusType("UP", "Up");
    public static final ServiceStatusType DOWN  = new ServiceStatusType("DOWN", "Down");
    public static final ServiceStatusType PAUSED  = new ServiceStatusType("PAUSED", "Paused");

    public static ServiceStatusType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public ServiceStatusType() {
        //do nothing
    }

    public ServiceStatusType(final String type, final String friendlyType) {
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
        if (!TYPES.containsKey(type)){
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
        ServiceStatusType other = (ServiceStatusType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

package cn.globalph.openadmin.server.service.persistence.module.criteria;

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
public class RestrictionType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, RestrictionType> TYPES = new LinkedHashMap<String, RestrictionType>();

    public static final RestrictionType STRING_LIKE  = new RestrictionType("STRING_LIKE", "STRING_LIKE");
    public static final RestrictionType BOOLEAN  = new RestrictionType("BOOLEAN", "BOOLEAN");
    public static final RestrictionType CHARACTER  = new RestrictionType("CHARACTER", "CHARACTER");
    public static final RestrictionType DATE  = new RestrictionType("DATE", "DATE");
    public static final RestrictionType DECIMAL  = new RestrictionType("DECIMAL", "DECIMAL");
    public static final RestrictionType LONG  = new RestrictionType("LONG", "LONG");
    public static final RestrictionType COLLECTION_SIZE_EQUAL  = new RestrictionType("COLLECTION_SIZE_EQUAL", "COLLECTION_SIZE_EQUAL");
    public static final RestrictionType IS_NULL_LONG  = new RestrictionType("IS_NULL_LONG", "IS_NULL_LONG");
    public static final RestrictionType STRING_EQUAL  = new RestrictionType("STRING_EQUAL", "STRING_EQUAL");
    public static final RestrictionType LONG_EQUAL  = new RestrictionType("LONG_EQUAL", "LONG_EQUAL");

    public static RestrictionType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public RestrictionType() {
        //do nothing
    }

    public RestrictionType(final String type, final String friendlyType) {
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
        RestrictionType other = (RestrictionType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

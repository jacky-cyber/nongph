package cn.globalph.openadmin.server.security.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of container permission types.
 * 
 * @felix.wu
 */
public class PermissionType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, PermissionType> TYPES = new LinkedHashMap<String, PermissionType>();

    public static final PermissionType READ  = new PermissionType("READ", "Read");
    public static final PermissionType CREATE  = new PermissionType("CREATE", "Create");
    public static final PermissionType UPDATE  = new PermissionType("UPDATE", "Update");
    public static final PermissionType DELETE  = new PermissionType("DELETE", "Delete");
    public static final PermissionType ALL  = new PermissionType("ALL", "All");
    public static final PermissionType OTHER  = new PermissionType("OTHER", "Other");

    public static PermissionType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public PermissionType() {
        //do nothing
    }

    public PermissionType(final String type, final String friendlyType) {
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
        PermissionType other = (PermissionType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

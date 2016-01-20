package cn.globalph.openadmin.server.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extensible enumeration for a Security Context Type.
 * @see cn.globalph.openadmin.server.security.domain.AdminSecurityContext
 *
 * @author elbertbautista
 *
 */
public class ContextType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, ContextType> TYPES = new LinkedHashMap<String, ContextType>();

    public static final ContextType GLOBAL = new ContextType("GLOBAL", "Global");
    public static final ContextType SITE = new ContextType("SITE", "Site");
    public static final ContextType CATALOG = new ContextType("CATALOG", "Catalog");
    public static final ContextType TEMPLATE = new ContextType("TEMPLATE", "Template");

    public static ContextType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public ContextType() {
        //do nothing
    }

    public ContextType(final String type, final String friendlyType) {
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
        ContextType other = (ContextType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

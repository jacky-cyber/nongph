package cn.globalph.common.sitemap.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reasonsable set of SiteMap URL priorities
 * 
 */
public class SiteMapPriorityType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, SiteMapPriorityType> TYPES = new LinkedHashMap<String, SiteMapPriorityType>();

    public static final SiteMapPriorityType ZERO = new SiteMapPriorityType("0.0", "0.0");
    public static final SiteMapPriorityType POINT1 = new SiteMapPriorityType("0.1", "0.1");
    public static final SiteMapPriorityType POINT2 = new SiteMapPriorityType("0.2", "0.2");
    public static final SiteMapPriorityType POINT3 = new SiteMapPriorityType("0.3", "0.3");
    public static final SiteMapPriorityType POINT4 = new SiteMapPriorityType("0.4", "0.4");
    public static final SiteMapPriorityType POINT5 = new SiteMapPriorityType("0.5", "0.5");
    public static final SiteMapPriorityType POINT6 = new SiteMapPriorityType("0.6", "0.6");
    public static final SiteMapPriorityType POINT7 = new SiteMapPriorityType("0.7", "0.7");
    public static final SiteMapPriorityType POINT8 = new SiteMapPriorityType("0.8", "0.8");
    public static final SiteMapPriorityType POINT9 = new SiteMapPriorityType("0.9", "0.9");
    public static final SiteMapPriorityType ONE = new SiteMapPriorityType("1.0", "1.0");

    public static SiteMapPriorityType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public SiteMapPriorityType() {
        //do nothing
    }

    public SiteMapPriorityType(final String type, final String friendlyType) {
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
        SiteMapPriorityType other = (SiteMapPriorityType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}

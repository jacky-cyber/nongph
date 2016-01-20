package cn.globalph.common.sitemap.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An enumeration of Sitemap change frequency values
 * 
 */
public class SiteMapChangeFreqType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, SiteMapChangeFreqType> TYPES = new LinkedHashMap<String, SiteMapChangeFreqType>();

    public static final SiteMapChangeFreqType ALWAYS = new SiteMapChangeFreqType("ALWAYS", "always");
    public static final SiteMapChangeFreqType HOURLY = new SiteMapChangeFreqType("HOURLY", "hourly");
    public static final SiteMapChangeFreqType DAILY = new SiteMapChangeFreqType("DAILY", "daily");
    public static final SiteMapChangeFreqType WEEKLY = new SiteMapChangeFreqType("WEEKLY", "weekly");
    public static final SiteMapChangeFreqType MONTHLY = new SiteMapChangeFreqType("MONTHLY", "monthly");
    public static final SiteMapChangeFreqType YEARLY = new SiteMapChangeFreqType("YEARLY", "yearly");
    public static final SiteMapChangeFreqType NEVER = new SiteMapChangeFreqType("NEVER", "never");

    public static SiteMapChangeFreqType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public SiteMapChangeFreqType() {
        //do nothing
    }

    public SiteMapChangeFreqType(final String type, final String friendlyType) {
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
        SiteMapChangeFreqType other = (SiteMapChangeFreqType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}

package cn.globalph.b2c.offer.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of offer types.
 *
 */
public class OfferTimeZoneType implements Serializable, EnumerationType {
    
    private static final long serialVersionUID = 1L;

    private static final Map<String, OfferTimeZoneType> TYPES = new LinkedHashMap<String, OfferTimeZoneType>();

    public static final OfferTimeZoneType SERVER = new OfferTimeZoneType("SERVER", "Server");
    public static final OfferTimeZoneType APPLICATION = new OfferTimeZoneType("APPLICATION", "Application Supplied");
    public static final OfferTimeZoneType CST = new OfferTimeZoneType("CST", "CST", true);
    public static final OfferTimeZoneType UTC = new OfferTimeZoneType("UTC", "UTC", true);
    public static OfferTimeZoneType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;
    private Boolean javaStandardTimeZone;


    public OfferTimeZoneType() {
        //do nothing
    }

    public OfferTimeZoneType(final String type, final String friendlyType) {
        this(type, friendlyType, false);
    }

    public OfferTimeZoneType(final String type, final String friendlyType, Boolean javaStandardTimeZone) {
        this.friendlyType = friendlyType;
        setType(type);
        setJavaStandardTimeZone(javaStandardTimeZone);
    }
    public void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }

    public Boolean getJavaStandardTimeZone() {
        return javaStandardTimeZone;
    }

    public void setJavaStandardTimeZone(Boolean javaStandardTimeZone) {
        this.javaStandardTimeZone = javaStandardTimeZone;
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
        OfferTimeZoneType other = (OfferTimeZoneType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}

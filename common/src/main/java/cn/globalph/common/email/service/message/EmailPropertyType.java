package cn.globalph.common.email.service.message;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of email property types.
 */
public class EmailPropertyType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, EmailPropertyType> TYPES = new LinkedHashMap<String, EmailPropertyType>();

    public static final EmailPropertyType USER = new EmailPropertyType("user", "User");
    public static final EmailPropertyType INFO = new EmailPropertyType("info", "Info");
    public static final EmailPropertyType SERVERINFO = new EmailPropertyType("serverInfo", "Server Info");

    public static EmailPropertyType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public EmailPropertyType() {
        //do nothing
    }

    public EmailPropertyType(final String type, final String friendlyType) {
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
        EmailPropertyType other = (EmailPropertyType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

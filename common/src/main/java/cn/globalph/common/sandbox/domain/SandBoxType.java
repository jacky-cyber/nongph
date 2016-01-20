package cn.globalph.common.sandbox.domain;


import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by bpolster.
 */
public class SandBoxType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, SandBoxType> TYPES = new LinkedHashMap<String, SandBoxType>();

    public static final SandBoxType USER = new SandBoxType("USER", "User", 3);
    public static final SandBoxType APPROVAL = new SandBoxType("APPROVAL", "Approval", 2);
    public static final SandBoxType DEFAULT = new SandBoxType("DEFAULT", "Default", 2);
    public static final SandBoxType PRODUCTION = new SandBoxType("PRODUCTION", "Production", 1);


    public static SandBoxType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;
    private Integer priority;

    public SandBoxType() {
        //do nothing
    }

    public SandBoxType(final String type, final String friendlyType, final Integer priority) {
        this.friendlyType = friendlyType;
        this.priority = priority;
        setType(type);
    }

    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }

    public Integer getPriority() {
        return priority;
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
        SandBoxType other = (SandBoxType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}

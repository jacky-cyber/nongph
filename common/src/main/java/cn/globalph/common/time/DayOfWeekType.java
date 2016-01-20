package cn.globalph.common.time;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of container shape types.
 * 
 * @felix.wu
 */
public class DayOfWeekType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, DayOfWeekType> TYPES = new LinkedHashMap<String, DayOfWeekType>();

    public static final DayOfWeekType SUNDAY  = new DayOfWeekType("1", "Sunday");
    public static final DayOfWeekType MONDAY  = new DayOfWeekType("2", "Monday");
    public static final DayOfWeekType TUESDAY  = new DayOfWeekType("3", "Tuesday");
    public static final DayOfWeekType WEDNESDAY  = new DayOfWeekType("4", "Wednesday");
    public static final DayOfWeekType THURSDAY  = new DayOfWeekType("5", "Thursday");
    public static final DayOfWeekType FRIDAY  = new DayOfWeekType("6", "Friday");
    public static final DayOfWeekType SATURDAY  = new DayOfWeekType("7", "Saturday");

    public static DayOfWeekType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public DayOfWeekType() {
        //do nothing
    }

    public DayOfWeekType(final String type, final String friendlyType) {
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
        DayOfWeekType other = (DayOfWeekType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

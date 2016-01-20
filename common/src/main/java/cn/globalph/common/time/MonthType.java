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
public class MonthType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, MonthType> TYPES = new LinkedHashMap<String, MonthType>();

    public static final MonthType JANUARY  = new MonthType("1", "January");
    public static final MonthType FEBRUARY  = new MonthType("2", "February");
    public static final MonthType MARCH  = new MonthType("3", "March");
    public static final MonthType APRIL  = new MonthType("4", "April");
    public static final MonthType MAY  = new MonthType("5", "May");
    public static final MonthType JUNE  = new MonthType("6", "June");
    public static final MonthType JULY  = new MonthType("7", "July");
    public static final MonthType AUGUST  = new MonthType("8", "August");
    public static final MonthType SEPTEMBER  = new MonthType("9", "September");
    public static final MonthType OCTOBER  = new MonthType("10", "October");
    public static final MonthType NOVEMBER  = new MonthType("11", "November");
    public static final MonthType DECEMBER  = new MonthType("12", "December");

    public static MonthType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public MonthType() {
        //do nothing
    }

    public MonthType(final String type, final String friendlyType) {
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
        MonthType other = (MonthType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

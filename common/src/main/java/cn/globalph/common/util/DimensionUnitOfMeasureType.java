package cn.globalph.common.util;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of units of measure types.
 * 
 * @felix.wu
 *
 */
public class DimensionUnitOfMeasureType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, DimensionUnitOfMeasureType> TYPES = new LinkedHashMap<String, DimensionUnitOfMeasureType>();

    public static final DimensionUnitOfMeasureType CENTIMETERS  = new DimensionUnitOfMeasureType("CENTIMETERS", "Centimeters");
    public static final DimensionUnitOfMeasureType METERS  = new DimensionUnitOfMeasureType("METERS", "Meters");
    public static final DimensionUnitOfMeasureType INCHES  = new DimensionUnitOfMeasureType("INCHES", "Inches");
    public static final DimensionUnitOfMeasureType FEET  = new DimensionUnitOfMeasureType("FEET", "Feet");

    public static DimensionUnitOfMeasureType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public DimensionUnitOfMeasureType() {
        //do nothing
    }

    public DimensionUnitOfMeasureType(final String type, final String friendlyType) {
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
        DimensionUnitOfMeasureType other = (DimensionUnitOfMeasureType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

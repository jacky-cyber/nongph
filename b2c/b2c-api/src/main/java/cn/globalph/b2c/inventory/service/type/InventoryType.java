package cn.globalph.b2c.inventory.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Enumeration to specify whether inventory should be checked or not.
 * 
 * @author Kelly Tisdell
 */
public class InventoryType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, InventoryType> TYPES = new LinkedHashMap<String, InventoryType>();
    
//    @Deprecated
//    public static final InventoryType NONE = new InventoryType("NONE", "None");
//    @Deprecated
//    public static final InventoryType BASIC = new InventoryType("BASIC", "Basic");
    
    public static final InventoryType ALWAYS_AVAILABLE  = new InventoryType("ALWAYS_AVAILABLE", "总有现货");
    public static final InventoryType UNAVAILABLE  = new InventoryType("UNAVAILABLE", "无现货");
    public static final InventoryType CHECK_QUANTITY  = new InventoryType("CHECK_QUANTITY", "检查数量");

    public static InventoryType getInstance(final String type) {
        return TYPES.get(type);
     }

    private String type;
    private String friendlyType;

    public InventoryType() {
        //do nothing
     }

    public InventoryType(final String type, final String friendlyType) {
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
        InventoryType other = (InventoryType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
     }
}

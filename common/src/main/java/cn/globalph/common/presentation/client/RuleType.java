package cn.globalph.common.presentation.client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author ppatel/bpolster
 *
 */
public class RuleType implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Map<String, RuleType> TYPES = new HashMap<String, RuleType>();

    public static final RuleType CUSTOMER  = new RuleType("1", "Customer");
    public static final RuleType REQUEST  = new RuleType("2", "Request");
    public static final RuleType TIME  = new RuleType("3", "Time");
    public static final RuleType PRODUCT  = new RuleType("4", "Product");
    public static final RuleType ORDER_ITEM  = new RuleType("5", "OrderItem");
    public static final RuleType LOCALE  = new RuleType("6", "Locale");
    public static final RuleType ORDER_ITEM_HISTORY  = new RuleType("7", "OrderItemHistory");
   

    public static RuleType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public RuleType() {
        //do nothing
    }

    public RuleType(final String type, final String friendlyType) {
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
        RuleType other = (RuleType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

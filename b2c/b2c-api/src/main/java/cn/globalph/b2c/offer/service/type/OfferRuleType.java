package cn.globalph.b2c.offer.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of offer rule types.
 * 促销规则类型
 */
public class OfferRuleType implements Serializable, EnumerationType {
    
    private static final long serialVersionUID = 1L;

    private static final Map<String, OfferRuleType> TYPES = new LinkedHashMap<String, OfferRuleType>();

    public static final OfferRuleType ORDER = new OfferRuleType("ORDER", "Order");
    public static final OfferRuleType FULFILLMENT_GROUP = new OfferRuleType("FULFILLMENT_GROUP", "Fulfillment Group");
    public static final OfferRuleType CUSTOMER = new OfferRuleType("CUSTOMER", "Customer");
    public static final OfferRuleType TIME = new OfferRuleType("TIME", "Time");
    public static final OfferRuleType REQUEST = new OfferRuleType("REQUEST", "Request");
    
    public static OfferRuleType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public OfferRuleType() {
        //do nothing
    }

    public OfferRuleType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
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
        OfferRuleType other = (OfferRuleType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}

package cn.globalph.b2c.offer.service.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An extendible enumeration of discount types.
 *
 */
public class OfferDiscountType implements Serializable, EnumerationType {
    
    private static final long serialVersionUID = 1L;

    private static final Map<String, OfferDiscountType> TYPES = new LinkedHashMap<String, OfferDiscountType>();

    public static final OfferDiscountType PERCENT_OFF = new OfferDiscountType("PERCENT_OFF", "下降折扣");
    public static final OfferDiscountType AMOUNT_OFF = new OfferDiscountType("AMOUNT_OFF", "下降金额");
    public static final OfferDiscountType FIX_PRICE = new OfferDiscountType("FIX_PRICE", "固定价格");

    public static OfferDiscountType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public OfferDiscountType() {
        //do nothing
    }

    public OfferDiscountType(final String type, final String friendlyType) {
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
        OfferDiscountType other = (OfferDiscountType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}

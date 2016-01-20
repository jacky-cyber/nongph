package cn.globalph.coupon.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class CouponAttributeNameType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, CouponAttributeNameType> TYPES = new LinkedHashMap<String, CouponAttributeNameType>();

    public static final CouponAttributeNameType PAY_AMOUNT  = new CouponAttributeNameType("PayAmount", "订单金额限制");
    public static final CouponAttributeNameType PRODUCT  = new CouponAttributeNameType("Product", "产品限制");
    public static final CouponAttributeNameType PROVIDER  = new CouponAttributeNameType("Provider", "供应商限制");
    public static final CouponAttributeNameType CATEGORY  = new CouponAttributeNameType("Category", "类别限制");

    public static CouponAttributeNameType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public CouponAttributeNameType() {
        //do nothing
    }

    public CouponAttributeNameType(final String type, final String friendlyType) {
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
        CouponAttributeNameType other = (CouponAttributeNameType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

package cn.globalph.b2c.coupon.type;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class CouponCodeType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, CouponCodeType> TYPES = new LinkedHashMap<String, CouponCodeType>();
    
    public static final CouponCodeType ACTIVE  = new CouponCodeType("ACTIVE", "可用");
    public static final CouponCodeType OVER_TIME  = new CouponCodeType("OVER_TIME", "过期不可用");
    public static final CouponCodeType HAS_USED  = new CouponCodeType("HAS_USED", "已被使用");

    public static CouponCodeType getInstance(final String type) {
        return TYPES.get(type);
     }

    private String type;
    private String friendlyType;

    public CouponCodeType() {
        //do nothing
     }

    public CouponCodeType(final String type, final String friendlyType) {
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
        CouponCodeType other = (CouponCodeType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
     }
}

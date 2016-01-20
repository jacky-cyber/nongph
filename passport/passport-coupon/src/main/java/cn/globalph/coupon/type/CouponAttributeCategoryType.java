package cn.globalph.coupon.type;

import cn.globalph.common.EnumerationType;
import cn.globalph.coupon.issue.event.CouponIssueEventType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class CouponAttributeCategoryType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, CouponAttributeCategoryType> TYPES = new LinkedHashMap<String, CouponAttributeCategoryType>();

    public static final CouponAttributeCategoryType APPLY_TO_ORDER_LMT  = new CouponAttributeCategoryType("ApplyToOrderLimitation", "适用于订单限制");
    public static final CouponAttributeCategoryType ORDER_COUPON_EVENT  = new CouponAttributeCategoryType("OrderCouponEvent", "订单完成事件限制");
    public static final CouponAttributeCategoryType REGISTER_COUPON_EVENT  = new CouponAttributeCategoryType("RegisterCouponEvent", "注册事件限制");

    public static CouponAttributeCategoryType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public CouponAttributeCategoryType() {
        //do nothing
    }

    public CouponAttributeCategoryType(final String type, final String friendlyType) {
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
        CouponAttributeCategoryType other = (CouponAttributeCategoryType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

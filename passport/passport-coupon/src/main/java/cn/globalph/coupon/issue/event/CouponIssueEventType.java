package cn.globalph.coupon.issue.event;

import cn.globalph.common.EnumerationType;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class CouponIssueEventType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final Map<String, CouponIssueEventType> TYPES = new LinkedHashMap<String, CouponIssueEventType>();

    public static final CouponIssueEventType ORDER_COUPON_EVENT  = new CouponIssueEventType("OrderCouponEvent", "订单完成时发送优惠券");
    public static final CouponIssueEventType REGISTER_COUPON_EVENT  = new CouponIssueEventType("RegisterCouponEvent", "注册成功发送优惠券");
    public static final CouponIssueEventType SEND_COUPON_EVENT  = new CouponIssueEventType("SendCouponEvent", "主动发送优惠券");


    public static CouponIssueEventType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public CouponIssueEventType() {
        //do nothing
    }

    public CouponIssueEventType(final String type, final String friendlyType) {
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
        CouponIssueEventType other = (CouponIssueEventType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}

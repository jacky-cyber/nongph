package cn.globalph.coupon.strategy.type;

import java.io.Serializable;
import java.util.LinkedHashMap;

import cn.globalph.common.EnumerationType;

public class CouponStrategyType implements Serializable, EnumerationType {

    private static final long serialVersionUID = 1L;

    private static final LinkedHashMap<String, CouponStrategyType> TYPES = new LinkedHashMap<String, CouponStrategyType>();

    public static final CouponStrategyType REGISTER_SUCCESS_COUPON_STRATEGY = new CouponStrategyType("cn.globalph.coupon.strategy.RegisterSuccessCouponStrategy", "注册成功送优惠券");
    public static final CouponStrategyType ORDER_COMPLETED_COUPON_STRATEGY = new CouponStrategyType("cn.globalph.coupon.strategy.OrderCompletedCouponStrategy", "完成订单送优惠券");


    public static CouponStrategyType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public CouponStrategyType() {
        //do nothing
    }

    public CouponStrategyType(final String type, final String friendlyType) {
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
        CouponStrategyType other = (CouponStrategyType) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}

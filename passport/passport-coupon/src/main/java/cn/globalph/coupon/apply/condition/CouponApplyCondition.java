package cn.globalph.coupon.apply.condition;

import cn.globalph.coupon.common.condition.CouponCondition;
import cn.globalph.coupon.type.CouponAttributeCategoryType;

public abstract class CouponApplyCondition <T> extends CouponCondition<T>{
    protected CouponApplyCondition(String name, T value) {
        super(name, value);
    }

    @Override
    public String getCategory() {
        return CouponAttributeCategoryType.APPLY_TO_ORDER_LMT.getType();
    }

}

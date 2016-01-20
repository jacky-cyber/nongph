package cn.globalph.coupon.apply.condition.impl;

import cn.globalph.coupon.apply.condition.CouponApplyCondition;
import cn.globalph.coupon.type.CouponAttributeNameType;

import java.math.BigDecimal;

public class OrderPayAmountApplyCondition extends CouponApplyCondition<BigDecimal> {
    public OrderPayAmountApplyCondition(BigDecimal value) {
        super(CouponAttributeNameType.PAY_AMOUNT.getType(), value);
    }

    @Override
    public boolean doVerify(String configMinPayAmount) {
        return new BigDecimal(configMinPayAmount).compareTo(getValue()) <= 0;
    }
}

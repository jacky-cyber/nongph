package cn.globalph.coupon.issue.event.condition.impl;

import cn.globalph.coupon.issue.event.condition.CouponIssueCondition;
import cn.globalph.coupon.type.CouponAttributeNameType;

import java.math.BigDecimal;

public class OrderPayAmountIssueCondition extends CouponIssueCondition<BigDecimal> {
    public OrderPayAmountIssueCondition(BigDecimal value) {
        super(CouponAttributeNameType.PAY_AMOUNT.getType(), value);
    }

    @Override
    public boolean doVerify(String configMinPayAmount) {
        return new BigDecimal(configMinPayAmount).compareTo(getValue()) <= 0;
    }
}

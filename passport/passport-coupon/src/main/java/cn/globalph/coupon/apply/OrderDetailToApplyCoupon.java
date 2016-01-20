package cn.globalph.coupon.apply;

import cn.globalph.coupon.apply.condition.CouponApplyCondition;
import cn.globalph.coupon.apply.condition.impl.OrderPayAmountApplyCondition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailToApplyCoupon {
    private BigDecimal payAmount;

    public OrderDetailToApplyCoupon(BigDecimal payAmount) {
        this.payAmount = payAmount;
        addCondition(new OrderPayAmountApplyCondition(payAmount));
    }

    private List<CouponApplyCondition> conditions = new ArrayList<CouponApplyCondition>();

    public List<CouponApplyCondition> getConditions() {
        return conditions;
    }

    public OrderDetailToApplyCoupon addCondition(CouponApplyCondition condition) {
        this.conditions.add(condition);
        return this;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }
}

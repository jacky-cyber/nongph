package cn.globalph.coupon.strategy.impl;

import cn.globalph.coupon.apply.OrderDetailToApplyCoupon;
import cn.globalph.coupon.domain.Coupon;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component(value = "phCashCouponStrategy")
public class CashCouponStrategy extends AbstractCouponStrategy {

    @Override
    public BigDecimal getDiscount(Coupon coupon, OrderDetailToApplyCoupon order) {
        return coupon.getValue();
    }
}

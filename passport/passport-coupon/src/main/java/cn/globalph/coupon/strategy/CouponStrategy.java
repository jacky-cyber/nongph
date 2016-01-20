package cn.globalph.coupon.strategy;

import cn.globalph.coupon.apply.OrderDetailToApplyCoupon;
import cn.globalph.coupon.domain.Coupon;
import cn.globalph.coupon.issue.event.CouponIssueEventSource;

import java.math.BigDecimal;

public interface CouponStrategy {
	void issue(Coupon coupon,CouponIssueEventSource source);
	Boolean isAvailable(Coupon coupon, OrderDetailToApplyCoupon order);
    BigDecimal getDiscount(Coupon coupon, OrderDetailToApplyCoupon order);
}

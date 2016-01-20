package cn.globalph.coupon.service;

import java.math.BigDecimal;
import java.util.List;

import cn.globalph.coupon.apply.OrderDetailToApplyCoupon;
import cn.globalph.coupon.domain.Coupon;
import cn.globalph.coupon.domain.CustomerCoupon;

public interface CouponService {
	List<Coupon> findAllCoupons();
	
	Coupon getCouponById(Long couponId);
	
	List<CustomerCoupon> getCurrentAvailableCouponByCustomerId(Long customerId, OrderDetailToApplyCoupon order);
	
	boolean isAvailable(CustomerCoupon customerCoupon, OrderDetailToApplyCoupon order);
	
	BigDecimal getDiscount(Coupon coupon, OrderDetailToApplyCoupon order);
	
	Boolean sendCustomerCoupon(Long customerId, Integer number);
}

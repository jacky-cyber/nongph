package cn.globalph.coupon.service;

import java.util.List;

import cn.globalph.coupon.domain.CustomerCoupon;

public interface CustomerCouponService {
	public CustomerCoupon saveCustomerCoupon(CustomerCoupon customerCoupon);
	
	public CustomerCoupon createNewCustomerCoupon();
	
	public List<CustomerCoupon> findActiveCouponByCustomerId(Long customerId);
	
	public CustomerCoupon findCustomerCouponByCustomerIdAndCouponId(Long customerId, Long couponId);
	
	public CustomerCoupon findCustomerCouponById(Long customerCouponId);
	
	public CustomerCoupon sendCustomerCoupon(Long customerId, Long couponId);
	
	public Integer findActiveCustomerCouponCount(Long customerId);
}

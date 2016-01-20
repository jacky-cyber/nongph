package cn.globalph.coupon.dao;

import java.util.List;

import cn.globalph.common.dao.BasicEntityDao;
import cn.globalph.coupon.domain.CustomerCoupon;

public interface CustomerCouponDao extends BasicEntityDao<CustomerCoupon>{
	public CustomerCoupon saveCustomerCoupon(CustomerCoupon customerCoupon);
	
	public CustomerCoupon createCustomerCoupon();
	
	public List<CustomerCoupon> findActiveCouponByCustomerId(Long customerId);
	
	public CustomerCoupon findCustomerCouponByCustomerIdAndCouponId(Long customerId, Long couponId);
}

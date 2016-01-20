package cn.globalph.coupon.domain;

import cn.globalph.passport.domain.Customer;

public interface CustomerCoupon {
	public void setId(Long id);
	
	public Long getId();
	
	public void setCutomer(Customer customer);
	
	public Customer getCustomer();
	
	public void setCoupon(Coupon coupon);
	
	public Coupon getCoupon();
	
	public void setStatus(Character status);
	
	public Character getStatus();
	
	public Integer getNumber();
	
	public void setNumber(Integer number);
}

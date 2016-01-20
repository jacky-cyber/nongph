package cn.globalph.b2c.coupon.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.globalph.b2c.coupon.type.CouponCodeType;
import cn.globalph.coupon.domain.Coupon;
import cn.globalph.passport.domain.Customer;

public interface CouponCode extends Serializable {
	public Long getId();
	
	public void setId(Long id);
	
	public Customer getCustomer();
	
	public void setCustomer(Customer customer);
	
	public Date getStartDate();
	
	public void setStartDate(Date startDate);
	
	public Date getEndDate();
	
	public void setEndDate(Date endDate);
	
	public String getCouponCode();
	
	public void setCouponCode(String couponCode);
	
	public BigDecimal getCouponAmount();
	
	public void setCouponAmount(BigDecimal couponAmount);
	
	public BigDecimal getCouponAmountTwo();
	
	public void setCouponAmountTwo(BigDecimal couponAmountTwo);
	
	public Boolean isValid();
	
	public CouponCodeType getStatus();
	
	public void setStatus(CouponCodeType status);
	
	public BigDecimal getDiscountAmount(Long customerId);
	
	public Coupon getCoupon();
	
	public void setCoupon(Coupon coupon);
}

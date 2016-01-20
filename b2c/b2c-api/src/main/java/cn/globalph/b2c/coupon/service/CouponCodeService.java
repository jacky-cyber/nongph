package cn.globalph.b2c.coupon.service;

import cn.globalph.b2c.coupon.domain.CouponCode;

public interface CouponCodeService {
	public CouponCode findCouponCodeByCode(String code);
	
	public CouponCode saveCouponCode(CouponCode couponCode);
}

package cn.globalph.coupon.dao;

import java.util.List;

import cn.globalph.coupon.domain.Coupon;

public interface CouponDao {
	public List<Coupon> findAllCoupons();

	public Coupon getCouponById(Long couponId);
}

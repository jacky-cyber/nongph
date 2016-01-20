package cn.globalph.b2c.coupon.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.globalph.b2c.coupon.dao.CouponCodeDao;
import cn.globalph.b2c.coupon.domain.CouponCode;

@Repository(value = "phCouponCodeService")
public class CouponCodeServiceImpl implements CouponCodeService {
	@Resource(name = "phCouponCodeDao")
	private CouponCodeDao couponCodeDao;
	
	@Override
	public CouponCode findCouponCodeByCode(String code) {
		return couponCodeDao.readCouponCodeByCode(code);
	}
	
	@Override
	public CouponCode saveCouponCode(CouponCode couponCode){
		return couponCodeDao.saveCouponCode(couponCode);
	}
}

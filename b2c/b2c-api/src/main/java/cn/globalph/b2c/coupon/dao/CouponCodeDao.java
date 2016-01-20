package cn.globalph.b2c.coupon.dao;

import cn.globalph.b2c.coupon.domain.CouponCode;
import cn.globalph.common.dao.BasicEntityDao;

public interface CouponCodeDao extends BasicEntityDao<CouponCode> {
	public CouponCode readCouponCodeByCode(String code);
	
	public CouponCode saveCouponCode(CouponCode couponCode);
}

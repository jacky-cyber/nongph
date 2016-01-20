package cn.globalph.b2c.coupon.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import cn.globalph.b2c.coupon.domain.CouponCode;
import cn.globalph.b2c.coupon.domain.CouponCodeImpl;
import cn.globalph.common.dao.BasicEntityDaoImpl;

@Repository(value = "phCouponCodeDao")
public class CouponCodeDaoImpl extends BasicEntityDaoImpl<CouponCode> implements CouponCodeDao{

	@Override
	public Class<? extends CouponCode> getImplClass() {
		return CouponCodeImpl.class;
	}
	
	@Override
	public CouponCode readCouponCodeByCode(String code){
		Query query = em.createNamedQuery("PH_READ_COUPON_CODE_BY_CODE");
		query.setParameter("code", code);
		List<CouponCode> couponCodes = query.getResultList();
		if(couponCodes == null || couponCodes.size() == 0){
			return null;
		}else{
			return couponCodes.get(0);
		}
	}
	
	public CouponCode saveCouponCode(CouponCode couponCode){
		return em.merge(couponCode);
	}
}

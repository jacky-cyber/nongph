package cn.globalph.coupon.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.coupon.domain.Coupon;
import cn.globalph.coupon.domain.CouponImpl;

@Repository("phCouponDao")
public class CouponDaoImpl implements CouponDao {
	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	@Override
	public List<Coupon> findAllCoupons() {
		Query query = em.createNamedQuery("PH_READ_ALL_COUPONS");
		return query.getResultList();
	}

	@Override
	public Coupon getCouponById(Long couponId) {
		return (Coupon) em.find(CouponImpl.class, couponId);
	}
	
}

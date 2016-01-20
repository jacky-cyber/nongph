package cn.globalph.coupon.dao;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import cn.globalph.common.persistence.EntityConfiguration;

@Repository("phCouponAttributeDao")
public class CouponAttributeDaoImpl implements CouponAttributeDao {
	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;
}

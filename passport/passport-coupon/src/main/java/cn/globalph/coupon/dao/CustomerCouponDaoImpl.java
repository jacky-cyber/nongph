package cn.globalph.coupon.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import cn.globalph.common.dao.BasicEntityDaoImpl;
import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.coupon.domain.CustomerCoupon;
import cn.globalph.coupon.domain.CustomerCouponImpl;

@Repository("phCustomerCouponDao")
public class CustomerCouponDaoImpl extends BasicEntityDaoImpl<CustomerCoupon> implements CustomerCouponDao {
	@PersistenceContext(unitName = "blPU")
	protected EntityManager em;

	@Resource(name = "blEntityConfiguration")
	protected EntityConfiguration entityConfiguration;

	@Override
	public CustomerCoupon saveCustomerCoupon(CustomerCoupon customerCoupon) {
		return em.merge(customerCoupon);
	}

	@Override
	public CustomerCoupon createCustomerCoupon() {
		return (CustomerCoupon) entityConfiguration.createEntityInstance(CustomerCoupon.class.getName());
	}

	@Override
	public Class<? extends CustomerCoupon> getImplClass() {
		return CustomerCouponImpl.class;
	}
	
	@Override
	public List<CustomerCoupon> findActiveCouponByCustomerId(Long customerId){
		Query query = em.createNamedQuery("PH_READ_ACTIVE_COUPONS_BY_CUSTOMER_ID");
		query.setParameter("customerId", customerId);
		return query.getResultList();
	}
	
	@Override
	public CustomerCoupon findCustomerCouponByCustomerIdAndCouponId(Long customerId, Long couponId){
		Query query = em.createNamedQuery("PH_READ_CUSTOMER_COUPON_BY_CUSTOMER_ID_AND_COUPON_ID");
		query.setParameter("customerId", customerId);
		query.setParameter("couponId", couponId);
		List<CustomerCoupon> customerCoupons =  query.getResultList();
		if(customerCoupons == null || customerCoupons.size() == 0){
			return null;
		}else{
			return customerCoupons.get(0);
		}
	}
}

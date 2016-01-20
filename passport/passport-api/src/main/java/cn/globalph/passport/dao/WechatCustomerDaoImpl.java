package cn.globalph.passport.dao;

import cn.globalph.common.dao.BasicEntityDaoImpl;
import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.passport.domain.WechatCustomer;
import cn.globalph.passport.domain.WechatCustomerImpl;
import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository("phWechatCustomerDao")
public class WechatCustomerDaoImpl extends BasicEntityDaoImpl<WechatCustomer> implements WechatCustomerDao {

	public WechatCustomer get(String id) {
		return  em.find(WechatCustomerImpl.class, id);
	}

	public WechatCustomer readWechatCustomerByOpenId(String openId) {
		List<WechatCustomer> wechatCustomerList = readWechatCustomersByOpenId(openId);
		return wechatCustomerList == null || wechatCustomerList.isEmpty() ? null : wechatCustomerList.get(0);
	}

	public List<WechatCustomer> readWechatCustomersByOpenId(String openId) {
		TypedQuery<WechatCustomer> query = em.createNamedQuery("PH_READ_WECHAT_CUSTOMER_BY_OPEN_ID", WechatCustomer.class);
		query.setParameter("openId", openId);
//		query.setHint(QueryHints.HINT_CACHEABLE, true);
//		query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");
		return query.getResultList();
	}

	public WechatCustomer create() {
		return (WechatCustomer) entityConfiguration.createEntityInstance(WechatCustomer.class.getName());
	}

	@Override
	public void deleteOpenId(String openId) {
		WechatCustomer wechatCustomer = readWechatCustomerByOpenId(openId);
		if (wechatCustomer != null) {
			em.remove(wechatCustomer);
		}
	}

	@Override
	public WechatCustomer readWechatCustomerByCustomerId(Long customerId) {
		List<WechatCustomer> wechatCustomerList = readWechatCustomersByCustomerId(customerId);
		return wechatCustomerList == null || wechatCustomerList.isEmpty() ? null : wechatCustomerList.get(0);
	}

	@Override
	public List<WechatCustomer> readWechatCustomersByCustomerId(Long customerId) {
		TypedQuery<WechatCustomer> query = em.createNamedQuery("PH_READ_WECHAT_CUSTOMER_BY_CUSTOMER_ID", WechatCustomer.class);
		query.setParameter("customerId", customerId);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");
		return query.getResultList();
	}

	@Override
	public WechatCustomer readWechatCustomerFromWechat(String openId) {
		TypedQuery<WechatCustomer> query = em.createNamedQuery("PH_READ_WECHAT_CUSTOMER_FROM_WECHAT", WechatCustomer.class);
		query.setParameter("openId", openId);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");
		return query.getSingleResult();
	}

	@Override
	public void active(String openId, Long customerId) {
		List<WechatCustomer> wechatCustomerList = readWechatCustomersByOpenId(openId);
		for (WechatCustomer item : wechatCustomerList) {
			item.setActive(item.getCustomerId().equals(customerId));
			em.merge(item);
		}
	}

	@Override
	public Class<? extends WechatCustomer> getImplClass() {
		return WechatCustomerImpl.class;
	}
}

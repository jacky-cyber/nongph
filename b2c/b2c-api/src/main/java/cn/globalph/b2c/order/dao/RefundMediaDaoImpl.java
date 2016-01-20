package cn.globalph.b2c.order.dao;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import cn.globalph.b2c.order.domain.RefundMedia;
import cn.globalph.b2c.order.domain.RefundMediaImpl;
import cn.globalph.common.dao.BasicEntityDaoImpl;
import cn.globalph.common.persistence.EntityConfiguration;

@Repository("phRefundMediaDao")
public class RefundMediaDaoImpl extends BasicEntityDaoImpl<RefundMedia> implements RefundMediaDao{
    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;
	
	@Override
	public Class<? extends RefundMedia> getImplClass() {
		return RefundMediaImpl.class;
	}

	@Override
	public RefundMedia create() {
		return (RefundMedia)entityConfiguration.createEntityInstance(RefundMedia.class.getName());
	}
}

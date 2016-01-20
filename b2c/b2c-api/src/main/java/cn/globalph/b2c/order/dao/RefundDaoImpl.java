package cn.globalph.b2c.order.dao;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import cn.globalph.b2c.order.domain.Refund;
import cn.globalph.b2c.order.domain.RefundImpl;
import cn.globalph.common.dao.BasicEntityDaoImpl;
import cn.globalph.common.persistence.EntityConfiguration;

@Repository("phRefundDao")
public class RefundDaoImpl extends BasicEntityDaoImpl<Refund> implements RefundDao {
    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;
	
	@Override
	public Class<? extends Refund> getImplClass() {
		return RefundImpl.class;
	}
	
	@Override
	public Refund create(){
		return (Refund)entityConfiguration.createEntityInstance(Refund.class.getName());
	}
}

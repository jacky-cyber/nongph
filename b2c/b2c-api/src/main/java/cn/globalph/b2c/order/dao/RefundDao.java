package cn.globalph.b2c.order.dao;

import cn.globalph.b2c.order.domain.Refund;
import cn.globalph.common.dao.BasicEntityDao;

public interface RefundDao extends BasicEntityDao<Refund>{
	public Refund create();
}

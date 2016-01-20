package cn.globalph.b2c.product.dao;

import cn.globalph.b2c.product.domain.GroupOn;
import cn.globalph.common.dao.BasicEntityDao;

public interface GroupOnDao extends BasicEntityDao<GroupOn> {
	public GroupOn saveGroupOn(GroupOn groupOn);
}

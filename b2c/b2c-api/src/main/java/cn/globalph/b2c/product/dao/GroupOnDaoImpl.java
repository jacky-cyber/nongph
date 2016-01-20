package cn.globalph.b2c.product.dao;

import org.springframework.stereotype.Repository;

import cn.globalph.b2c.product.domain.GroupOn;
import cn.globalph.b2c.product.domain.GroupOnImpl;
import cn.globalph.common.dao.BasicEntityDaoImpl;

@Repository(value = "phGroupOnDao")
public class GroupOnDaoImpl extends BasicEntityDaoImpl<GroupOn> implements GroupOnDao {

	@Override
	public Class<? extends GroupOn> getImplClass() {
		return GroupOnImpl.class;
	}

	@Override
	public GroupOn saveGroupOn(GroupOn groupOn) {
		return em.merge(groupOn);
	}
}

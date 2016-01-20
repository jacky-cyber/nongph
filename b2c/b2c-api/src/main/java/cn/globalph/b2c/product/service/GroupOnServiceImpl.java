package cn.globalph.b2c.product.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.globalph.b2c.product.dao.GroupOnDao;
import cn.globalph.b2c.product.domain.GroupOn;

@Repository(value = "phGroupOnService")
public class GroupOnServiceImpl implements GroupOnService {
	@Resource(name = "phGroupOnDao")
	private GroupOnDao groupOnDao;
	
	@Override
	@Transactional(value = "blTransactionManager")
	public GroupOn saveGroupOn(GroupOn groupOn) {
		return groupOnDao.saveGroupOn(groupOn);
	}
	
}

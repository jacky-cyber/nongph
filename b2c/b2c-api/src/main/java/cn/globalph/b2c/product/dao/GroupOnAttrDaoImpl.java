package cn.globalph.b2c.product.dao;

import org.springframework.stereotype.Repository;

import cn.globalph.b2c.product.domain.GroupOnAttr;
import cn.globalph.b2c.product.domain.GroupOnAttrImpl;
import cn.globalph.common.dao.BasicEntityDaoImpl;

@Repository(value = "phGroupOnAttrDao")
public class GroupOnAttrDaoImpl extends BasicEntityDaoImpl<GroupOnAttr> implements
		GroupOnAttrDao {

	@Override
	public Class<? extends GroupOnAttr> getImplClass() {
		return GroupOnAttrImpl.class;
	}
}

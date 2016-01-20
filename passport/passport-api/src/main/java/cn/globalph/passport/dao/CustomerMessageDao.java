package cn.globalph.passport.dao;

import java.util.List;

import cn.globalph.common.dao.BasicEntityDao;
import cn.globalph.passport.domain.CustomerMessage;

public interface CustomerMessageDao extends BasicEntityDao<CustomerMessage> {
	public Long getMessageCountByCustomerIdAndStatus(Long customerId, String status);
	
	public List<CustomerMessage> getMessagesByCustomerId(Long customerId);
	
	public void deativeMessagesByCustomerId(Long customerId);
	
	public CustomerMessage create();
}

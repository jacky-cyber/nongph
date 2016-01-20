package cn.globalph.passport.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.globalph.passport.dao.CustomerMessageDao;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerMessage;
import cn.globalph.passport.service.type.CustomerMessageStatusType;

@Service(value = "phCustomerMessageService")
public class CustomerMessageServiceImpl implements CustomerMessageService {
	@Resource(name = "phCustomerMessageDao")
	private CustomerMessageDao customerMessageDao;
	
	@Override
	public Long findActiveMessageByCustomerId(Long customerId) {
		return customerMessageDao.getMessageCountByCustomerIdAndStatus(customerId,CustomerMessageStatusType.ACTIVE.getType());
	}

	@Override
	public List<CustomerMessage> findMessagesByCustomerId(Long customerId) {
		return customerMessageDao.getMessagesByCustomerId(customerId);
	}

	@Override
	@Transactional("blTransactionManager")
	public void deativeMessagesByCustomerId(Long customerId) {
		customerMessageDao.deativeMessagesByCustomerId(customerId);
	}
	
	@Override
	@Transactional("blTransactionManager")
	public CustomerMessage sendMessageToCustomer(Customer customer, String messageTitle, String messageText){
		CustomerMessage customerMessage = customerMessageDao.create();
		customerMessage.setCreateTime(new Date(System.currentTimeMillis()));
		customerMessage.setMessageText(messageText);
		customerMessage.setTitle(messageTitle);
		customerMessage.setStatus(CustomerMessageStatusType.ACTIVE);
		customerMessage.setCustomer(customer);
		return customerMessageDao.persist(customerMessage);
	}

}

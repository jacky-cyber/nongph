package cn.globalph.passport.service;

import java.util.List;

import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerMessage;

public interface CustomerMessageService {
	public Long findActiveMessageByCustomerId(Long customerId);
	
	public List<CustomerMessage> findMessagesByCustomerId(Long customerId);
	
	public void deativeMessagesByCustomerId(Long customerId);
	
	public CustomerMessage sendMessageToCustomer(Customer customer, String messageTitle, String messageText);
	
}

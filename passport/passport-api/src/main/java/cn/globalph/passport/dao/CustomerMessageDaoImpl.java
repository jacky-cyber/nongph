package cn.globalph.passport.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import cn.globalph.common.dao.BasicEntityDaoImpl;
import cn.globalph.passport.domain.CustomerMessage;
import cn.globalph.passport.domain.CustomerMessageImpl;
import cn.globalph.passport.service.type.CustomerMessageStatusType;

@Repository("phCustomerMessageDao")
public class CustomerMessageDaoImpl extends BasicEntityDaoImpl<CustomerMessage> implements CustomerMessageDao{
	@Override
	public Class<CustomerMessageImpl> getImplClass(){
		return CustomerMessageImpl.class;
	}

	@Override
	public Long getMessageCountByCustomerIdAndStatus(Long customerId,
			String status) {
		Query query = em.createNamedQuery("PH_GET_MESSAGE_COUNT_BY_CUSTOMER_ID_AND_STATUS");
        query.setParameter("customerId", customerId);
        query.setParameter("status", status);
        return (Long)query.getSingleResult();
	}

	@Override
	public List<CustomerMessage> getMessagesByCustomerId(Long customerId) {
		TypedQuery<CustomerMessage> query = em.createNamedQuery("PH_GET_MESSAGES_BY_CUSTOMER_ID",CustomerMessage.class);
        query.setParameter("customerId", customerId);
        return query.getResultList();
	}

	@Override
	public void deativeMessagesByCustomerId(Long customerId) {
		List<CustomerMessage> customerMessages = getMessagesByCustomerId(customerId);
		for(CustomerMessage customerMessage : customerMessages){
			if(customerMessage.getStatus().equals(CustomerMessageStatusType.ACTIVE)){
				customerMessage.setStatus(CustomerMessageStatusType.ARCHIVE);
				persist(customerMessage);
			}
		}
	}
	
	public CustomerMessage create(){
		return (CustomerMessage)entityConfiguration.createEntityInstance(CustomerMessage.class.getName());
	}
}

package cn.globalph.passport.dao;

import cn.globalph.common.dao.BasicEntityDaoImpl;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerImpl;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.TypedQuery;

@Repository("blCustomerDao")
public class CustomerDaoImpl extends BasicEntityDaoImpl<Customer> implements CustomerDao {
    
    public Class<CustomerImpl> getImplClass(){
    	return CustomerImpl.class;
    }
    
    @Override
    public Customer readCustomerByUsername(String username) {        
        List<Customer> customers = readCustomersByUsername(username);
        return customers == null || customers.isEmpty() ? null : customers.get(0);
    }
   
    @Override
    public List<Customer> readCustomersByUsername(String username) {
        TypedQuery<Customer> query = em.createNamedQuery("BC_READ_CUSTOMER_BY_USER_NAME", Customer.class);
        query.setParameter("username", username);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");
        return query.getResultList();        
    }

    @Override
    public Customer readCustomerByEmail(String emailAddress) {
        List<Customer> customers = readCustomersByEmail(emailAddress);
        return customers == null || customers.isEmpty() ? null : customers.get(0);
    }

    @Override
    public Customer readCustomerByPhone(String phone) {
        TypedQuery<Customer> query = em.createNamedQuery("PH_READ_CUSTOMER_BY_PHONE", Customer.class);
        query.setParameter("phone", phone);
//        query.setHint(QueryHints.HINT_CACHEABLE, true);
//        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");
        List<Customer> customers = query.getResultList();
        return customers == null || customers.isEmpty() ? null : customers.get(0);
    }

    @Override
    public List<Customer> readCustomersByEmail(String emailAddress) {
        TypedQuery<Customer> query = em.createNamedQuery("BC_READ_CUSTOMER_BY_EMAIL", Customer.class);
        query.setParameter("email", emailAddress);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");
        return query.getResultList();
    }

    @Override
    public Customer create() {
        Customer customer =  (Customer) entityConfiguration.createEntityInstance(Customer.class.getName());
        return customer;
    }

    @Override
    public Customer save(Customer customer) {
        return em.merge(customer);
    }
}

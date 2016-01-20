package cn.globalph.passport.dao;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.passport.domain.CustomerPayment;
import cn.globalph.passport.domain.CustomerPaymentImpl;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Repository("blCustomerPaymentDao")
public class CustomerPaymentDaoImpl implements CustomerPaymentDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public List<CustomerPayment> readCustomerPaymentsByCustomerId(Long customerId) {
        Query query = em.createNamedQuery("BC_READ_CUSTOMER_PAYMENTS_BY_CUSTOMER_ID");
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }

    @Override
    public CustomerPayment save(CustomerPayment customerPayment) {
        return em.merge(customerPayment);
    }

    @Override
    public CustomerPayment readCustomerPaymentByToken(String token) {
        Query query = em.createNamedQuery("BC_READ_CUSTOMER_PAYMENT_BY_TOKEN");
        query.setParameter("token", token);
        CustomerPayment payment = null;
        try{
            payment = (CustomerPayment) query.getSingleResult();
        } catch (NoResultException e) {
           //do nothing
        }
        return  payment;
    }

    @Override
    public CustomerPayment readCustomerPaymentById(Long customerPaymentId) {
        return (CustomerPayment) em.find(CustomerPaymentImpl.class, customerPaymentId);
    }

    @Override
    public void deleteCustomerPaymentById(Long customerPaymentId) {
        CustomerPayment customerPayment = readCustomerPaymentById(customerPaymentId);
        if (customerPayment != null) {
            em.remove(customerPayment);
        }
    }

    @Override
    public CustomerPayment create() {
        return (CustomerPayment) entityConfiguration.createEntityInstance(CustomerPayment.class.getName());
    }

}

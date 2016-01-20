package cn.globalph.passport.dao;

import cn.globalph.passport.domain.CustomerPayment;

import java.util.List;

public interface CustomerPaymentDao {

    public List<CustomerPayment> readCustomerPaymentsByCustomerId(Long customerId);

    public CustomerPayment save(CustomerPayment customerPayment);

    public CustomerPayment readCustomerPaymentById(Long customerPaymentId);

    public CustomerPayment readCustomerPaymentByToken(String token);

    public void deleteCustomerPaymentById(Long customerPaymentId);

    public CustomerPayment create();

}

package cn.globalph.passport.service;

import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerPayment;

import java.util.List;

public interface CustomerPaymentService {

    public CustomerPayment saveCustomerPayment(CustomerPayment customerPayment);

    public List<CustomerPayment> readCustomerPaymentsByCustomerId(Long customerId);

    public CustomerPayment readCustomerPaymentById(Long customerPaymentId);

    public CustomerPayment readCustomerPaymentByToken(String token);

    public void deleteCustomerPaymentById(Long customerPaymentId);

    public CustomerPayment create();

    public CustomerPayment findDefaultPaymentForCustomer(Customer customer);

    public CustomerPayment setAsDefaultPayment(CustomerPayment payment);

    public Customer deleteCustomerPaymentFromCustomer(Customer customer, CustomerPayment payment);

}

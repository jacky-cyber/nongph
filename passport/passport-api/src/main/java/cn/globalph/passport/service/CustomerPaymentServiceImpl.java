package cn.globalph.passport.service;

import cn.globalph.passport.dao.CustomerPaymentDao;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerPayment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;

@Service("blCustomerPaymentService")
public class CustomerPaymentServiceImpl implements CustomerPaymentService {

    /** Services */
    @Resource(name="blCustomerPaymentDao")
    protected CustomerPaymentDao customerPaymentDao;

    @Resource(name="blCustomerService")
    protected CustomerService customerService;

    @Override
    @Transactional("blTransactionManager")
    public CustomerPayment saveCustomerPayment(CustomerPayment customerPayment) {
        return customerPaymentDao.save(customerPayment);
    }

    @Override
    public List<CustomerPayment> readCustomerPaymentsByCustomerId(Long customerId) {
        return customerPaymentDao.readCustomerPaymentsByCustomerId(customerId);
    }

    @Override
    public CustomerPayment readCustomerPaymentById(Long customerPaymentId) {
        return customerPaymentDao.readCustomerPaymentById(customerPaymentId);
    }

    @Override
    public CustomerPayment readCustomerPaymentByToken(String token) {
        return customerPaymentDao.readCustomerPaymentByToken(token);
    }

    @Override
    @Transactional("blTransactionManager")
    public void deleteCustomerPaymentById(Long customerPaymentId) {
        customerPaymentDao.deleteCustomerPaymentById(customerPaymentId);
    }

    @Override
    @Transactional("blTransactionManager")
    public CustomerPayment create() {
        return customerPaymentDao.create();
    }

    public CustomerPayment findDefaultPaymentForCustomer(Customer customer) {
        if (customer == null) { return null; }
        List<CustomerPayment> payments = readCustomerPaymentsByCustomerId(customer.getId());
        for (CustomerPayment payment : payments) {
            if (payment.isDefault()) {
                return payment;
            }
        }
        return null;
    }

    @Override
    @Transactional("blTransactionManager")
    public CustomerPayment setAsDefaultPayment(CustomerPayment payment) {
        CustomerPayment oldDefault = findDefaultPaymentForCustomer(payment.getCustomer());
        if (oldDefault != null) {
            oldDefault.setDefault(false);
            saveCustomerPayment(oldDefault);
        }
        payment.setDefault(true);
        return saveCustomerPayment(payment);
    }

    @Override
    @Transactional("blTransactionManager")
    public Customer deleteCustomerPaymentFromCustomer(Customer customer, CustomerPayment payment) {
        /*List<CustomerPayment> payments = customer.getCustomerPayments();
        for (CustomerPayment customerPayment : payments) {
            if (customerPayment.getId().equals(payment.getId())) {
                customer.getCustomerPayments().remove(customerPayment);
                break;
            }
        }*/
       return customerService.saveCustomer(customer);
    }

}

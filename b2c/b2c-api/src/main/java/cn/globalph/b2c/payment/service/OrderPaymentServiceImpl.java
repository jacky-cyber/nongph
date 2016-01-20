package cn.globalph.b2c.payment.service;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.payment.dao.OrderPaymentDao;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.common.time.SystemTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service("blOrderPaymentService")
public class OrderPaymentServiceImpl implements OrderPaymentService {

    @Resource(name = "blOrderPaymentDao")
    protected OrderPaymentDao paymentDao;

    @Override
    @Transactional(value = "blTransactionManager")
    public OrderPayment save(OrderPayment payment) {
        return paymentDao.save(payment);
    }

    @Override
    public PaymentTransaction save(PaymentTransaction transaction) {
        return paymentDao.save(transaction);
    }

    @Override
    public OrderPayment readPaymentById(Long paymentId) {
        return paymentDao.readPaymentById(paymentId);
    }

    @Override
    public List<OrderPayment> readPaymentsForOrder(Order order) {
        return paymentDao.readPaymentsForOrder(order);
    }

    @Override
    public OrderPayment create() {
        return paymentDao.create();
    }

    @Override
    public void delete(OrderPayment payment) {
        paymentDao.delete(payment);
    }

    @Override
    public PaymentTransaction createTransaction() {
        PaymentTransaction returnItem = paymentDao.createTransaction();
        
        //TODO: this needs correct timezone conversion, right?
        returnItem.setDate(SystemTime.asDate());
        
        return returnItem;
    }

    @Override
    public PaymentTransaction readTransactionById(Long transactionId) {
        return paymentDao.readTransactionById(transactionId);
    }

}

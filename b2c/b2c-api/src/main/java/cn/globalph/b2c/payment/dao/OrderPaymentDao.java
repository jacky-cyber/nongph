package cn.globalph.b2c.payment.dao;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;

import java.util.List;

public interface OrderPaymentDao {

    public OrderPayment readPaymentById(Long paymentId);

    public OrderPayment save(OrderPayment payment);

    public PaymentTransaction save(PaymentTransaction transaction);

    public List<OrderPayment> readPaymentsForOrder(Order order);

    public OrderPayment create();

    public void delete(OrderPayment payment);

    public PaymentTransaction createTransaction();

    public PaymentTransaction readTransactionById(Long transactionId);

}

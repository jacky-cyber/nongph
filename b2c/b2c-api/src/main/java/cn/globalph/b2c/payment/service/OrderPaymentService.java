package cn.globalph.b2c.payment.service;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;

import java.util.List;

public interface OrderPaymentService {

    public OrderPayment save(OrderPayment payment);

    public PaymentTransaction save(PaymentTransaction transaction);

    public OrderPayment readPaymentById(Long paymentId);

    public List<OrderPayment> readPaymentsForOrder(Order order);

    public OrderPayment create();

    /**
     * Deletes a payment from the system. Note that this is just a soft-delete and simply archives this entity
     * 
     * @see {@link OrderPayment#getArchived()}
     */
    public void delete(OrderPayment payment);

    public PaymentTransaction createTransaction();

    public PaymentTransaction readTransactionById(Long transactionId);

}

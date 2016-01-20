package cn.globalph.b2c.payment.dao;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.OrderPaymentImpl;
import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.b2c.payment.domain.PaymentTransactionImpl;
import cn.globalph.common.persistence.EntityConfiguration;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository("blOrderPaymentDao")
public class OrderPaymentDaoImpl implements OrderPaymentDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public OrderPayment save(OrderPayment payment) {
        return em.merge(payment);
    }

    @Override
    public PaymentTransaction save(PaymentTransaction transaction) {
        return em.merge(transaction);
    }

    @Override
    public OrderPayment readPaymentById(Long paymentId) {
        return em.find(OrderPaymentImpl.class, paymentId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OrderPayment> readPaymentsForOrder(Order order) {
        Query query = em.createNamedQuery("BC_READ_ORDERS_PAYMENTS_BY_ORDER_ID");
        query.setParameter("orderId", order.getId());
        return query.getResultList();
    }

    @Override
    public OrderPayment create() {
        return ((OrderPayment) entityConfiguration.createEntityInstance(OrderPayment.class.getName()));
    }

    @Override
    public PaymentTransaction createTransaction() {
        return entityConfiguration.createEntityInstance(PaymentTransaction.class.getName(), PaymentTransaction.class);
    }

    @Override
    public PaymentTransaction readTransactionById(Long transactionId) {
        return em.find(PaymentTransactionImpl.class, transactionId);
    }

    @Override
    public void delete(OrderPayment paymentInfo) {
        if (!em.contains(paymentInfo)) {
            paymentInfo = readPaymentById(paymentInfo.getId());
        }
        em.remove(paymentInfo);
    }
}

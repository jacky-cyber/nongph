package cn.globalph.b2c.order.domain.wrapper;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.wrap.OrderPaymentWrap;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.b2c.payment.domain.wrap.PaymentTransactionWrap;
import cn.globalph.b2c.payment.domain.wrapper.PaymentTransactionWrapper;
import cn.globalph.b2c.payment.service.OrderPaymentService;
import cn.globalph.common.domain.wrapper.APIUnwrapper;
import cn.globalph.common.domain.wrapper.APIWrapper;
import cn.globalph.common.money.Money;
import cn.globalph.common.payment.PaymentGatewayType;
import cn.globalph.common.payment.PaymentType;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

public class OrderPaymentWrapper implements APIWrapper<OrderPayment, OrderPaymentWrap>, APIUnwrapper<OrderPayment, OrderPaymentWrap> {
	
	private PaymentTransactionWrapper transactionWrapper;
	
	@Resource(name="blOrderService")
	private OrderService orderService;
	 
	@Resource(name="blOrderPaymentService")
	private OrderPaymentService orderPaymentService;
	
    @Override
    public OrderPaymentWrap wrapDetails(OrderPayment model) {
    	OrderPaymentWrap wrap = new OrderPaymentWrap();
    	wrap.setId( model.getId() );

        if (model.getOrder() != null) {
        	wrap.setOrderId( model.getOrder().getId() );
        }

        if (model.getType() != null) {
        	wrap.setType( model.getType().getType() );
        }

        if (model.getGatewayType() != null) {
        	wrap.setGatewayType( model.getGatewayType().getType() );
        }

        if (model.getAmount() != null) {
        	wrap.setAmount( model.getAmount().getAmount() );
        }

        if (model.getTransactions() != null && !model.getTransactions().isEmpty()) {
            List<PaymentTransactionWrap> transactions = new ArrayList<PaymentTransactionWrap>();
            for (PaymentTransaction transaction : model.getTransactions()) {
                transactions.add( transactionWrapper.wrapSummary(transaction) );
            }
            wrap.setTransactions( transactions );
        }

        wrap.setReferenceNumber( model.getReferenceNumber() );
        return wrap;
    }

    @Override
    public OrderPaymentWrap wrapSummary(OrderPayment model) {
        return wrapDetails(model);
    }

    @Override
    public OrderPayment unwrap(OrderPaymentWrap wrap) {
        
        OrderPayment payment = orderPaymentService.create();

       
        Order order = orderService.getOrderById( wrap.getOrderId() );
        if (order != null) {
            payment.setOrder(order);
        }

        payment.setType(PaymentType.getInstance(wrap.getType()));
        payment.setPaymentGatewayType(PaymentGatewayType.getInstance(wrap.getGatewayType()));
        payment.setReferenceNumber(wrap.getReferenceNumber());

        if (wrap.getAmount() != null) {
            payment.setAmount(new Money(wrap.getAmount()));
        }

        if (wrap.getTransactions() != null && !wrap.getTransactions().isEmpty()) {
            for (PaymentTransactionWrap transactionWrap : wrap.getTransactions()) {
                PaymentTransaction transaction = transactionWrapper.unwrap(transactionWrap);
                transaction.setOrderPayment(payment);
                payment.addTransaction(transaction);
            }
        }

        return payment;
    }
}

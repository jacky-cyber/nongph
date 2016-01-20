package cn.globalph.b2c.api.endpoint.checkout;

import cn.globalph.b2c.api.WebServicesException;
import cn.globalph.b2c.api.endpoint.BaseEndpoint;
import cn.globalph.b2c.checkout.service.CheckoutService;
import cn.globalph.b2c.checkout.service.exception.CheckoutException;
import cn.globalph.b2c.checkout.service.workflow.CheckoutResponse;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.wrap.OrderPaymentWrap;
import cn.globalph.b2c.order.domain.wrap.OrderWrap;
import cn.globalph.b2c.order.domain.wrapper.OrderPaymentWrapper;
import cn.globalph.b2c.order.domain.wrapper.OrderWrapper;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.service.OrderPaymentService;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * 结算API具体实现
 * @author felix.wu
 */
public abstract class CheckoutEndpoint extends BaseEndpoint {

    @Resource(name="blCheckoutService")
    protected CheckoutService checkoutService;

    @Resource(name="blOrderService")
    protected OrderService orderService;

    @Resource(name="blOrderPaymentService")
    protected OrderPaymentService orderPaymentService;
    
    private OrderPaymentWrapper orderPaymentWrapper;
    
    private OrderWrapper orderWrapper;
    
    public List<OrderPaymentWrap> findPaymentsForOrder(Long cartId) {
        Order cart = orderService.getOrderById( cartId );
        if (cart != null && cart.getPayments() != null && !cart.getPayments().isEmpty()) {
            List<OrderPayment> payments = cart.getPayments();
            List<OrderPaymentWrap> paymentWrappers = new ArrayList<OrderPaymentWrap>();
            for (OrderPayment payment : payments) {
                paymentWrappers.add( orderPaymentWrapper.wrapSummary(payment) );
            }
            return paymentWrappers;
        }

        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CART_NOT_FOUND);
    }

    public OrderPaymentWrap addPaymentToOrder( Long cartId,
                                               OrderPaymentWrap wrap) {
        Order cart = orderService.getOrderById( cartId );
        if (cart != null) {
            OrderPayment orderPayment = orderPaymentWrapper.unwrap(wrap);
            if (orderPayment.getOrder() != null && orderPayment.getOrder().getId().equals(cart.getId())) {
                orderPayment = orderPaymentService.save(orderPayment);
                OrderPayment savedPayment = orderService.addPaymentToOrder(cart, orderPayment, null);
                return orderPaymentWrapper.wrapSummary(savedPayment);
            }
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CART_NOT_FOUND);

    }

    public OrderWrap removePaymentFromOrder(Long cartId, OrderPaymentWrap wrap) {
        Order cart = orderService.getOrderById( cartId );
        if (cart != null) {
            OrderPayment orderPayment = orderPaymentWrapper.unwrap(wrap);
            if (orderPayment.getOrder() != null && orderPayment.getOrder().getId().equals(cart.getId())) {
                OrderPayment paymentToRemove = null;
                for (OrderPayment payment : cart.getPayments()) {
                    if (payment.getId().equals(orderPayment.getId())) {
                        paymentToRemove = payment;
                    }
                }

                orderService.removePaymentFromOrder(cart, paymentToRemove);
                return  orderWrapper.wrapDetails(cart);
            }
        }

        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CART_NOT_FOUND);
    }

    public OrderWrap performCheckout(Long cartId) {
        Order cart = orderService.getOrderById( cartId );
        if (cart != null) {
            try {
                CheckoutResponse response = checkoutService.performCheckout(cart);
                Order order = response.getOrder();
                return orderWrapper.wrapDetails(order);
            } catch (CheckoutException e) {
                throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                        .addMessage(WebServicesException.CHECKOUT_PROCESSING_ERROR);
            }
        }

        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CART_NOT_FOUND);

    }
}

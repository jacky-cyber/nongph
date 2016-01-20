package cn.globalph.b2c.api.endpoint.order;

import cn.globalph.b2c.api.WebServicesException;
import cn.globalph.b2c.api.endpoint.BaseEndpoint;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.wrap.OrderWrap;
import cn.globalph.b2c.order.domain.wrapper.OrderWrapper;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

public abstract class OrderHistoryEndpoint extends BaseEndpoint {

    @Resource(name="blOrderService")
    protected OrderService orderService;

    @Resource(name="blCustomerService")
    protected CustomerService customerService;
    
    protected OrderWrapper orderWrapper;
    
    public List<OrderWrap> findOrdersForCustomer(Long customerId, String orderStatus) {
        Customer customer = customerService.getCustomerById( customerId );
        OrderStatus status = OrderStatus.getInstance(orderStatus);
        if (customer != null && status != null) {
            List<Order> orders = orderService.findOrdersForCustomer(customer, status);

            if (orders != null && !orders.isEmpty()) {
                List<OrderWrap> wrappers = new ArrayList<OrderWrap>();
                for (Order order : orders) {
                    wrappers.add( orderWrapper.wrapSummary(order) );
                }

                return wrappers;
            }

            throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                    .addMessage(WebServicesException.CART_NOT_FOUND);
        }

        throw WebServicesException.build(Response.Status.BAD_REQUEST.getStatusCode())
                .addMessage(WebServicesException.CUSTOMER_NOT_FOUND);
    }
}

package cn.globalph.b2c.checkout.service;

import cn.globalph.b2c.checkout.service.exception.CheckoutException;
import cn.globalph.b2c.checkout.service.workflow.CheckoutResponse;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.type.OrderStatus;

public interface CheckoutService {

    /**
     * <p>Checks out an order by executing the blCheckoutWorkflow. The <b>order</b> is saved both before and after the workflow
     * is executed so that activities can modify the various entities on and related to the <b>order</b>.</p>
     * 
     * <p>This method is also thread-safe; 2 requests cannot attempt to check out the same <b>order</b></p>
     * 
     * @param order the order to be checked out
     * @return
     * @throws CheckoutException if there are any exceptions while executing any of the activities in the workflow (assuming
     * that the workflow does not already have a preconfigured error handler) or if the given <b>order</b> has already been
     * checked out (in Broadleaf terms this means the <b>order</b> has already been changed to {@link OrderStatus#CONFIRMED})
     */
    CheckoutResponse performCheckout(Order order) throws CheckoutException;

    void performConfirm(Order order) throws CheckoutException;

    void performComplete(Order order) throws CheckoutException;

    void performCancel(Order order) throws CheckoutException;
    
}

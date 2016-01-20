package cn.globalph.b2c.checkout.service;

import cn.globalph.b2c.checkout.service.exception.CheckoutException;
import cn.globalph.b2c.checkout.service.workflow.CheckoutResponse;
import cn.globalph.b2c.checkout.service.workflow.CheckoutSeed;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.exception.RequiredAttributeNotProvidedException;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.workflow.ActivityMessages;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.b2c.workflow.Processor;
import cn.globalph.b2c.workflow.WorkflowException;
import cn.globalph.coupon.issue.event.CouponIssueEvent;
import cn.globalph.coupon.issue.event.CouponIssueEventSource;
import cn.globalph.coupon.issue.event.CouponIssueEventType;
import cn.globalph.coupon.issue.event.condition.impl.OrderPayAmountIssueCondition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service("blCheckoutService")
public class CheckoutServiceImpl implements CheckoutService {

    @Resource(name="blCheckoutWorkflow")
    protected Processor checkoutWorkflow;
    @Resource(name = "phConfirmOrderWorkflow")
    protected Processor confirmOrderWorkflow;
    @Resource(name = "phCompleteOrderWorkflow")
    protected Processor completeOrderWorkflow;
    @Resource(name = "phCancelOrderWorkflow")
    protected Processor cancelOrderWorkflow;
    @Autowired
    protected ApplicationContext applicationContext;

    @Resource(name="blOrderService")
    protected OrderService orderService;
    
    /**
     * Map of locks for given order ids. This lock map ensures that only a single request can handle a particular order
     * at a time
     */
    protected static ConcurrentMap<Long, Object> lockMap = new ConcurrentHashMap<Long, Object>();

    @Override
    public CheckoutResponse performCheckout(Order order) throws CheckoutException {
        CheckoutSeed seed = null;
        try {
            //Immediately fail if another thread is currently attempting to check out the order
            Object lockObject = putLock(order.getId());
            if (lockObject != null) {
                throw new CheckoutException("This order is already in the process of being submitted, unable to checkout order -- id: " + order.getId(), new CheckoutSeed(order, new HashMap<String, Object>()));
            }

            // Immediately fail if this order has already been checked out previously
            if (hasOrderBeenSubmitted(order)) {
                throw new CheckoutException("This order has already been submitted, unable to checkout order -- id: " + order.getId(), new CheckoutSeed(order, new HashMap<String, Object>()));
            }

            // Do a final save of the order before going through with the checkout workflow
            order = orderService.save(order, false);
            seed = new CheckoutSeed(order, new HashMap<String, Object>());

            ProcessContext<CheckoutSeed> context = (ProcessContext<CheckoutSeed>) checkoutWorkflow.doActivities(seed);

            // We need to pull the order off the seed and save it here in case any activity modified the order.
            order = orderService.save(seed.getOrder(), false);
            order.getOrderMessages().addAll(((ActivityMessages) context).getActivityMessages());
            seed.setOrder(order);

            return seed;
        } catch (PricingException e) {
            throw new CheckoutException("Unable to checkout order -- id: " + order.getId(), e, seed);
        } catch (WorkflowException e) {
            throw new CheckoutException("Unable to checkout order -- id: " + order.getId(), e.getRootCause(), seed);
        } catch (RequiredAttributeNotProvidedException e) {
            throw new CheckoutException("Unable to checkout order -- id: " + order.getId(), e.getCause(), seed);
        } finally {
            // The order has completed processing, remove the order from the processing map
            removeLock(order.getId());
        }
    }

    @Override
    public void performConfirm(Order order) throws CheckoutException {
        CheckoutSeed seed = null;
        try {
            //Immediately fail if another thread is currently attempting to check out the order
            Object lockObject = putLock(order.getId());
            if (lockObject != null) {
                throw new CheckoutException("This order is already in a process, unable to pay order -- id: " + order.getId(), new CheckoutSeed(order, new HashMap<String, Object>()));
            }

            // Immediately fail if this order has already been checked out previously
            if (hasOrderBeenConfirmed(order)) {
                throw new CheckoutException("This order has already been submitted, unable to checkout order -- id: " + order.getId(), new CheckoutSeed(order, new HashMap<String, Object>()));
            }

            order.setStatus(OrderStatus.CONFIRMED);
            
            // Do a final save of the order before going through with the checkout workflow
            order = orderService.save(order, false);
            seed = new CheckoutSeed(order, new HashMap<String, Object>());

            ProcessContext<CheckoutSeed> context = (ProcessContext<CheckoutSeed>) confirmOrderWorkflow.doActivities(seed);

            // We need to pull the order off the seed and save it here in case any activity modified the order.
            order = orderService.save(seed.getOrder(), false);
            order.getOrderMessages().addAll(((ActivityMessages) context).getActivityMessages());
            seed.setOrder(order);

        } catch (PricingException e) {
            throw new CheckoutException("Unable to pay order -- id: " + order.getId(), e, seed);
        } catch (WorkflowException e) {
            throw new CheckoutException("Unable to pay order -- id: " + order.getId(), e.getRootCause(), seed);
        } catch (RequiredAttributeNotProvidedException e) {
            throw new CheckoutException("Unable to pay order -- id: " + order.getId(), e.getCause(), seed);
        } finally {
            // The order has completed processing, remove the order from the processing map
            removeLock(order.getId());
        }
    }

    @Override
    @Transactional("blTransactionManager")
    public void performComplete(Order order) throws CheckoutException {
        CheckoutSeed seed = null;
        try {
            //Immediately fail if another thread is currently attempting to check out the order
            Object lockObject = putLock(order.getId());
            if (lockObject != null) {
                throw new CheckoutException("This order is already in a process, unable to pay order -- id: " + order.getId(), new CheckoutSeed(order, new HashMap<String, Object>()));
            }

            order.setStatus(OrderStatus.COMPLETED);
            order.setCompleteDate(new Date(System.currentTimeMillis()));
            
            applicationContext.publishEvent(
                    new CouponIssueEvent(CouponIssueEventType.ORDER_COUPON_EVENT.getType(),
                            new CouponIssueEventSource(order.getCustomer().getId(),
                                    new Date(System.currentTimeMillis())
                            ).addCondition(new OrderPayAmountIssueCondition(order.getTotal().getAmount()))
                    )
            );
            

            // Do a final save of the order before going through with the checkout workflow
            order = orderService.save(order, false);
            seed = new CheckoutSeed(order, new HashMap<String, Object>());

            ProcessContext<CheckoutSeed> context = (ProcessContext<CheckoutSeed>) completeOrderWorkflow.doActivities(seed);

            // We need to pull the order off the seed and save it here in case any activity modified the order.
            order = orderService.save(seed.getOrder(), false);
            order.getOrderMessages().addAll(((ActivityMessages) context).getActivityMessages());
            seed.setOrder(order);

        } catch (PricingException e) {
            throw new CheckoutException("Unable to complete order -- id: " + order.getId(), e, seed);
        } catch (WorkflowException e) {
            throw new CheckoutException("Unable to complete order -- id: " + order.getId(), e.getRootCause(), seed);
        } catch (RequiredAttributeNotProvidedException e) {
            throw new CheckoutException("Unable to complete order -- id: " + order.getId(), e.getCause(), seed);
        } finally {
            // The order has completed processing, remove the order from the processing map
            removeLock(order.getId());
        }
    }

    @Override
    @Transactional("blTransactionManager")
    public void performCancel(Order order) throws CheckoutException {
        CheckoutSeed seed = null;
        try {
            //Immediately fail if another thread is currently attempting to check out the order
            Object lockObject = putLock(order.getId());
            if (lockObject != null) {
                throw new CheckoutException("This order is already in a process, unable to cancel order -- id: " + order.getId(), new CheckoutSeed(order, new HashMap<String, Object>()));
            }

            // Do a final save of the order before going through with the checkout workflow
            order.setStatus(OrderStatus.CANCELLED);
            order = orderService.save(order, false);
            seed = new CheckoutSeed(order, new HashMap<String, Object>());

            ProcessContext<CheckoutSeed> context = (ProcessContext<CheckoutSeed>) cancelOrderWorkflow.doActivities(seed);

            // We need to pull the order off the seed and save it here in case any activity modified the order.
            order = orderService.save(seed.getOrder(), false);
            order.getOrderMessages().addAll(((ActivityMessages) context).getActivityMessages());
            seed.setOrder(order);

        } catch (PricingException e) {
            throw new CheckoutException("Unable to cancel order -- id: " + order.getId(), e, seed);
        } catch (WorkflowException e) {
            throw new CheckoutException("Unable to cancel order -- id: " + order.getId(), e.getRootCause(), seed);
        } catch (RequiredAttributeNotProvidedException e) {
            throw new CheckoutException("Unable to cancel order -- id: " + order.getId(), e.getCause(), seed);
        } finally {
            // The order has completed processing, remove the order from the processing map
            removeLock(order.getId());
        }
    }

    /**
     * Checks if the <b>order</b> has already been gone through the checkout workflow.
     *
     * @param order
     * @return
     */
    protected boolean hasOrderBeenSubmitted(Order order) {
        return OrderStatus.SUBMITTED.equals(order.getStatus());
    }

    /**
     * Checks if the <b>order</b> has already been gone through the checkout workflow.
     *
     * @param order
     * @return
     */
    protected boolean hasOrderBeenConfirmed(Order order) {
        return OrderStatus.CONFIRMED.equals(order.getStatus());
    }

    /**
    * Get an object to lock on for the given order id
    * 
    * @param orderId
    * @return null if there was not already a lock object available. If an object was already in the map, this will return
    * that object, which means that there is already a thread attempting to go through the checkout workflow
    */
    protected Object putLock(Long orderId) {
        return lockMap.putIfAbsent(orderId, new Object());
    }
    
    /**
     * Done with processing the given orderId, remove the lock from the map
     * 
     * @param orderId
     */
    protected void removeLock(Long orderId) {
        lockMap.remove(orderId);
    }
    
}

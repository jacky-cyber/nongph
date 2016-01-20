package cn.globalph.b2c.web.order;

import org.aspectj.lang.ProceedingJoinPoint;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.passport.domain.Customer;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class OrderStateAOP implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    public Object processOrderRetrieval(ProceedingJoinPoint call) throws Throwable {
        
        OrderState orderState = applicationContext.getBean("blOrderState", OrderState.class);
        Customer customer = (Customer) call.getArgs()[0];
        Order order = orderState.getOrder(customer);
        Object returnValue;
        if (order != null) {
            returnValue = order;
        } else {
            returnValue = call.proceed();
            returnValue = orderState.setOrder(customer, (Order) returnValue);
        }

        return returnValue;
    }
}

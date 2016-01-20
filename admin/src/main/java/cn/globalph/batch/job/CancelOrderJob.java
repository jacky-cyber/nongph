package cn.globalph.batch.job;

import java.util.List;

import cn.globalph.b2c.checkout.service.CheckoutService;
import cn.globalph.b2c.checkout.service.exception.CheckoutException;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.OrderLogService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.batch.core.SingletonJob;
import cn.globalph.batch.core.annotation.JobId;
import cn.globalph.batch.core.context.JobContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component("CancelOrderJob")
@JobId("CancelOrderJob")
public class CancelOrderJob extends SingletonJob {
    protected final Log logger = LogFactory.getLog(this.getClass());
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    @Resource(name = "phOrderLogService")
    protected OrderLogService orderLogService;
    @Resource(name = "blCheckoutService")
    protected CheckoutService checkoutService;

    @Override
    protected void doExecute(JobContext jobContext) throws Exception {
	   	 List<Order> orders = orderService.findOverdueOrders();
	     for(Order order : orders){
	    	 try {
				checkoutService.performCancel(order);
			} catch (CheckoutException e) {
				logger.error(e.getMessage());
			}
	     }
    }
    
    
}

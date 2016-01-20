package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderLog;
import cn.globalph.b2c.order.service.OrderLogService;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;


/**
 * Split order by provider
 *
 * @author steven
 * @since Jun 28 2015
 */
public class OrderLogActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    protected static final Log LOG = LogFactory.getLog(OrderLogActivity.class);
    @Resource(name = "phOrderLogService")
    protected OrderLogService orderLogService;

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        if (OrderStatus.SUBMITTED.equals(order.getStatus())) {
            OrderLog orderLog = orderLogService.create();
            orderLog.setOrder(order);
            orderLog.setMessage("您提交了订单");
            orderLog.setType(OrderLog.ORDER_LOG_TYPE_CUSTOMER);
            orderLog.setOperator("SYSTEM");
            orderLogService.save(orderLog);
        }
        if (OrderStatus.CONFIRMED.equals(order.getStatus())) {
            OrderLog orderLog = orderLogService.create();
            orderLog.setOrder(order);
            orderLog.setMessage("您已付款");
            orderLog.setType(OrderLog.ORDER_LOG_TYPE_CUSTOMER);
            orderLog.setOperator("SYSTEM");
            orderLogService.save(orderLog);
        } else if (OrderStatus.COMPLETED.equals(order.getStatus())) {
            OrderLog orderLog = orderLogService.create();
            orderLog.setOrder(order);
            orderLog.setMessage("订单已完成");
            orderLog.setType(OrderLog.ORDER_LOG_TYPE_SYSTEM);
            orderLog.setOperator("SYSTEM");
            orderLogService.save(orderLog);
        } else if (OrderStatus.CANCELLED.equals(order.getStatus())) {
            OrderLog orderLog = orderLogService.create();
            orderLog.setOrder(order);
            orderLog.setMessage("订单已取消");
            orderLog.setType(OrderLog.ORDER_LOG_TYPE_SYSTEM);
            orderLog.setOperator("SYSTEM");
            orderLogService.save(orderLog);
        }
        return context;
    }

}


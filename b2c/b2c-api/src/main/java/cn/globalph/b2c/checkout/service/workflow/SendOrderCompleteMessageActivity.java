package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.sms.SMSUtil;
import cn.globalph.passport.domain.Customer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Send order confirmation message
 */
public class SendOrderCompleteMessageActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    protected static final Log LOG = LogFactory.getLog(SendOrderConfirmationEmailActivity.class);

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        Customer customer = order.getCustomer();
        String message = "尊敬的" + customer.getName() + "！您的订单" + order.getOrderNumber() + "已完成，感谢您的惠顾！";
        try {
            SMSUtil.sendMessage(customer.getPhone(), message);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return context;
    }
}
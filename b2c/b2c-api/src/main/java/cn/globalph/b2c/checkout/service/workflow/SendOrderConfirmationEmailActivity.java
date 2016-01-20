package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.email.service.EmailService;
import cn.globalph.common.email.service.info.EmailInfo;

import java.util.HashMap;

import javax.annotation.Resource;


/**
 * Send order confirmation email
 */
public class SendOrderConfirmationEmailActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    protected static final Log LOG = LogFactory.getLog(SendOrderConfirmationEmailActivity.class);

    @Resource(name = "blEmailService")
    protected EmailService emailService;

    @Resource(name = "blOrderConfirmationEmailInfo")
    protected EmailInfo orderConfirmationEmailInfo;

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        HashMap<String, Object> vars = new HashMap<String, Object>();
        vars.put("customer", order.getCustomer());
        vars.put("orderNumber", order.getOrderNumber());
        vars.put("order", order);

        //Email service failing should not trigger rollback
        try {
            emailService.sendTemplateEmail(order.getCustomer().getEmailAddress(), getOrderConfirmationEmailInfo(), vars);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return context;
    }

    public EmailInfo getOrderConfirmationEmailInfo() {
        return orderConfirmationEmailInfo;
    }

    public void setOrderConfirmationEmailInfo(EmailInfo orderConfirmationEmailInfo) {
        this.orderConfirmationEmailInfo = orderConfirmationEmailInfo;
    }

}


package cn.globalph.b2c.checkout.service.workflow;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderAddress;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.workflow.BaseActivity;
import cn.globalph.b2c.workflow.ProcessContext;
import cn.globalph.common.sms.SMSUtil;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.WechatCustomer;
import cn.globalph.passport.service.WechatCustomerService;
import cn.globalph.wechat.WechatUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Send order confirmation message
 */
public class SendOrderConfirmationMessageActivity extends BaseActivity<ProcessContext<CheckoutSeed>> {

    protected static final Log LOG = LogFactory.getLog(SendOrderConfirmationEmailActivity.class);

    @Resource(name = "phWechatCustomerService")
    private WechatCustomerService wechatCustomerService;
    @Resource(name = "phWechatUtil")
    private WechatUtil wechatUtil;

    @Override
    public ProcessContext<CheckoutSeed> execute(ProcessContext<CheckoutSeed> context) throws Exception {
        Order order = context.getSeedData().getOrder();
        sendSms(order);
        return context;
    }

    private void sendSms(Order order) {
        Customer customer = order.getCustomer();
        Date submitDate = order.getSubmitDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
        String message = "尊敬的用户，您于" +
                    format.format(submitDate) +
                    "提交的订单（" + order.getOrderNumber() + "）已收到,";
        if (!(hasGroupOnProduct(order) || hasPresaleProduct(order))) {
            message += "我们将在24小时内发货";
        } else if (isAllGroupOnProduct(order)) {
            message += "我们将在本次团购活动结束后当天发货";
        } else if (isAllPresaleProduct(order)) {
            message += "我们将在本次预售活动结束后当天发货";
        } else if (isGroupOnProductOrPresaleProduct(order)) {
            message += "我们将在24小时内发货,预售团购商品除外";
        } else {
            message += "我们将在本次团购/预售活动结束后当天发货";
        }
        message += "。";
        try {
            SMSUtil.sendMessage(customer.getPhone(), message);
            sendWechatMessage(order);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private Boolean isGroupOnProductOrPresaleProduct(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            if ((!orderItem.getSku().getIsGroupOn()) || (!orderItem.getSku().isPresale())) return false;
        }
        return true;
    }

    private Boolean isAllGroupOnProduct(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            if (!orderItem.getSku().getIsGroupOn()) return false;
        }
        return true;
    }

    private Boolean isAllPresaleProduct(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            if (!orderItem.getSku().isPresale()) return false;
        }
        return true;
    }

    private Boolean hasGroupOnProduct(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            if (orderItem.getSku().getIsGroupOn()) return true;
        }
        return false;
    }

    private Boolean hasPresaleProduct(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
        	if (orderItem.getSku().isPresale()) return true;
        }
        return false;
    }

    private void sendWechatMessage(Order order) {
        String payMethod = order.getPayments().get(0).getType().getFriendlyType();

        StringBuilder content = new StringBuilder();
        for (OrderItem orderItem : order.getOrderItems()) {
            content.append(orderItem.getName());
            content.append(" 数量: ");
            content.append(orderItem.getQuantity());
            content.append("\n");
        }

        try {
            WechatCustomer wechatCustomer = wechatCustomerService.readWechatCustomerByCustomerId(order.getCustomer().getId());
            if (wechatCustomer != null && StringUtils.isNotEmpty(wechatCustomer.getOpenId())) {
                wechatUtil.sendConfirmMessage(wechatCustomer.getOpenId(), order.getOrderNumber(), order.getTotal().getAmount(),
                        payMethod, content.toString());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        content.append("下单人: ").append(order.getCustomer().getName());
        content.append("\n下单电话: ").append(order.getCustomer().getPhone());

        OrderAddress orderAddress = order.getOrderAddress();
        if (orderAddress != null) {
            content.append("\n收货人: ").append(orderAddress.getReceiver());
            content.append("\n收货电话: ").append(orderAddress.getPhone());
            content.append("\n收货地址: ").append(orderAddress.getFullAddress());
        }
        if (StringUtils.isNotBlank(order.getRemark())) {
            content.append("\n备注: ").append(order.getRemark());
        }

        try {
            wechatUtil.sendConfirmMessageToAdmin(order.getOrderNumber(), order.getTotal().getAmount(), payMethod, content.toString());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}

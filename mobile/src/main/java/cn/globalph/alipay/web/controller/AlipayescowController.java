package cn.globalph.alipay.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.checkout.service.gateway.PassthroughPaymentConstants;
import cn.globalph.b2c.order.domain.NullOrderImpl;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.b2c.payment.service.OrderPaymentService;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.web.order.CartState;
import cn.globalph.common.payment.PaymentGatewayType;
import cn.globalph.common.payment.PaymentTransactionType;
import cn.globalph.common.payment.PaymentType;
import cn.globalph.common.payment.dto.PaymentResponseDTO;
import cn.globalph.common.payment.service.PaymentGatewayCheckoutService;
import cn.globalph.common.payment.service.PaymentGatewayConfiguration;
import cn.globalph.common.payment.service.PaymentGatewayWebResponseService;
import cn.globalph.common.vendor.service.exception.PaymentException;
import cn.globalph.common.web.payment.controller.PaymentGatewayAbstractController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 支付宝担保支付
 * @author qiutian
 */
@Controller("alipayescowController")
@RequestMapping("/" + AlipayescowController.GATEWAY_CONTEXT_KEY)
public class AlipayescowController  {

    protected static final Log LOG = LogFactory.getLog(AlipayescowController.class);
    protected static final String GATEWAY_CONTEXT_KEY = "alipayescow";
    /* Services */
    @Resource(name = "blOrderService")
    protected OrderService orderService;

    @Resource(name = "blOrderPaymentService")
    protected OrderPaymentService orderPaymentService;
    
    /* Optional Service */
    @Autowired(required=false)
    @Qualifier("blPaymentGatewayCheckoutService")
    protected PaymentGatewayCheckoutService paymentGatewayCheckoutService;


    @RequestMapping(value = "/return", method = RequestMethod.GET)
    public String returnEndpoint(Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) throws PaymentException {
    	/**基本参数**/
    	//成功标示
    	String is_success = request.getParameter("is_success");
    	//合作身份ID
    	String partnerId = request.getParameter("partnerId");
    	//签名方式
    	String sign_type = request.getParameter("sign_type");
    	//签名
    	String sign = request.getParameter("sign");
        //参数编码字符集
    	String charset = request.getParameter("charset");
    	/**业务参数**/
    	//通知校验ID
    	String notify_id = request.getParameter("notify_id");
    	//通知类型
    	String notify_type = request.getParameter("notify_type");
    	//通知时间
    	String notify_time = request.getParameter("notify_time");
    	//交易号
    	String trade_no = request.getParameter("trade_no");
    	//商品名称
    	String subject = request.getParameter("subject");
    	//商品单价
    	String price = request.getParameter("price");
    	//商品数量
    	String quantity = request.getParameter("quantity");
    	//卖家支付宝账号
    	String seller_email = request.getParameter("seller_email");
    	//买家支付宝账号
    	String buyer_email = request.getParameter("buyer_email");
    	//卖家支付宝账号对应的支付宝唯一用户号
    	String seller_id = request.getParameter("seller_id");
    	//买家ID
    	String buyer_id = request.getParameter("buyer_id");
    	//商品折扣
    	String discount = request.getParameter("discount");
    	//总额
    	String total_fee = request.getParameter("total_fee");
    	//交易状态
    	String trade_status = request.getParameter("trade_status");
    	//总价是否调整过
    	String is_total_fee_adjust = request.getParameter("is_total_fee_adjust");
    	//是否使用红包
    	String use_coupon = request.getParameter("use_coupon");
    	//商户网站唯一订单号
    	String out_trade_no = request.getParameter("out_trade_no");
    	//商品描述
    	String body = request.getParameter("body");
    	//收款类型
    	String payment_type = request.getParameter("payment_type");
    	//物流类型
    	String logistics_type = request.getParameter("logistics_type");
    	//物流运费
    	String logistics_fee = request.getParameter("logistics_fee");
    	//物流支付类型
    	String logistics_payment = request.getParameter("logistics_payment");
    	//物流状态更新时间
    	String gmt_logistics_modify = request.getParameter("gmt_logistics_modify");
    	//买家动作集合
    	String buyer_actions = request.getParameter("buyer_actions");
    	//卖家动作集合
    	String seller_actions = request.getParameter("seller_actions");
    	//交易创建时间
    	String gmt_create = request.getParameter("gmt_create");
    	//交易支付时间
    	String gmt_payment = request.getParameter("gmt_payment");
    	//退款状态
    	String refund_status = request.getParameter("refund_status");
    	//交易退款时间
    	String gmt_refund = request.getParameter("gmt_refund");
    	//收货人姓名
    	String receive_name = request.getParameter("receive_name");
    	//收货人地址
    	String receive_address = request.getParameter("receive_address");
    	//收货人邮编
    	String receive_zip = request.getParameter("receive_zip");
    	//收货人电话
    	String receive_phone = request.getParameter("receive_phone");
    	//收货人手机
    	String receive_mobile = request.getParameter("receive_mobile");
    	Order order = orderService.findOrderByOrderNumber(out_trade_no);
    	 List<OrderPayment> paymentsToInvalidate = new ArrayList<OrderPayment>();
         for (OrderPayment payment : order.getPayments()) {
             if (payment.isActive()) {
                 if (payment.getTransactions() == null || payment.getTransactions().isEmpty()) {
                     paymentsToInvalidate.add(payment);
                 } else {
                     for (PaymentTransaction transaction : payment.getTransactions()) {
                         if (!PaymentTransactionType.UNCONFIRMED.equals(transaction.getType())) {
                              paymentsToInvalidate.add(payment);
                         }
                     }
                 }
             }
         }

         for (OrderPayment payment : paymentsToInvalidate) {
             order.getPayments().remove(payment);
             if (paymentGatewayCheckoutService != null) {
                 paymentGatewayCheckoutService.markPaymentAsInvalid(payment.getId());
             }
         }

         //Create a new Order Payment of the passed in type
         OrderPayment passthroughPayment = orderPaymentService.create();
         passthroughPayment.setType(PaymentType.ALIPAYESCOW);
         passthroughPayment.setPaymentGatewayType(PaymentGatewayType.PASSTHROUGH);
         passthroughPayment.setAmount(order.getTotalAfterAppliedPayments());
         passthroughPayment.setOrder(order);

         // Create the transaction for the payment
         PaymentTransaction transaction = orderPaymentService.createTransaction();
         transaction.setAmount(order.getTotalAfterAppliedPayments());
         transaction.setRawResponse("Passthrough Payment");
         transaction.setSuccess(true);
         transaction.setType(PaymentTransactionType.AUTHORIZE_AND_CAPTURE);
         transaction.getAdditionalFields().put(PassthroughPaymentConstants.PASSTHROUGH_PAYMENT_TYPE, PaymentType.ALIPAYESCOW.getType());

         transaction.setOrderPayment(passthroughPayment);
         passthroughPayment.addTransaction(transaction);
         orderService.addPaymentToOrder(order, passthroughPayment, null);

         try {
			orderService.save(order, true);
		} catch (PricingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

         return processCompleteCheckoutOrderFinalized(redirectAttributes);
    }
    
    public String processCompleteCheckoutOrderFinalized(final RedirectAttributes redirectAttributes) throws PaymentException {
        Order cart = CartState.getCart();

        if (cart != null && !(cart instanceof NullOrderImpl)) {
            try {
                String orderNumber = initiateCheckout(cart.getId());
                return getConfirmationViewRedirect(orderNumber);
            } catch (Exception e) {
                handleProcessingException(e, redirectAttributes);
            }
        }

        return "redirect:/checkout";
    }

    public String initiateCheckout(Long orderId) throws Exception{
        if (paymentGatewayCheckoutService != null && orderId != null) {
            return paymentGatewayCheckoutService.initiateCheckout(orderId);
        }
        return null;
    }

    public String getBaseConfirmationRedirect() {
        return "redirect:/confirmation";
    }

    protected String getConfirmationViewRedirect(String orderNumber) {
        return getBaseConfirmationRedirect() + "/" + orderNumber;
    }
    
    public void handleProcessingException(Exception e, RedirectAttributes redirectAttributes) throws PaymentException {
        if (LOG.isTraceEnabled()) {
            LOG.trace("A Processing Exception Occurred finalizing the order. Adding Error to Redirect Attributes.");
        }

        redirectAttributes.addAttribute(PaymentGatewayAbstractController.PAYMENT_PROCESSING_ERROR,
                PaymentGatewayAbstractController.getProcessingErrorMessage());
    }
    
    @RequestMapping(value = "/notify", method = RequestMethod.POST)
    public String notifyEndpoint(HttpServletRequest request) throws PaymentException {
    	//通知校验ID
    	String notify_id = request.getParameter("notify_id");
    	//通知类型
    	String notify_type = request.getParameter("notify_type");
    	//通知时间
    	String notify_time = request.getParameter("notify_time");
    	//签名方式
    	String sign_type = request.getParameter("sign_type");
    	//签名
    	String sign = request.getParameter("sign");
    	//交易号
    	String trade_no = request.getParameter("trade_no");
    	//商品名称
    	String subject = request.getParameter("subject");
    	//商品单价
    	String price = request.getParameter("price");
    	//商品数量
    	String quantity = request.getParameter("quantity");
    	//卖家支付宝账号
    	String seller_email = request.getParameter("seller_email");
    	//买家支付宝账号
    	String buyer_email = request.getParameter("buyer_email");
    	//卖家支付宝账号对应的支付宝唯一用户号
    	String seller_id = request.getParameter("seller_id");
    	//买家ID
    	String buyer_id = request.getParameter("buyer_id");
    	//商品折扣
    	String discount = request.getParameter("discount");
    	//总额
    	String total_fee = request.getParameter("total_fee");
    	//交易状态
    	String trade_status = request.getParameter("trade_status");
    	//总价是否调整过
    	String is_total_fee_adjust = request.getParameter("is_total_fee_adjust");
    	//是否使用红包
    	String use_coupon = request.getParameter("use_coupon");
    	//商户网站唯一订单号
    	String out_trade_no = request.getParameter("out_trade_no");
    	//商品描述
    	String body = request.getParameter("body");
    	//收款类型
    	String payment_type = request.getParameter("payment_type");
    	//物流类型
    	String logistics_type = request.getParameter("logistics_type");
    	//物流运费
    	String logistics_fee = request.getParameter("logistics_fee");
    	//物流支付类型
    	String logistics_payment = request.getParameter("logistics_payment");
    	//物流状态更新时间
    	String gmt_logistics_modify = request.getParameter("gmt_logistics_modify");
    	//买家动作集合
    	String buyer_actions = request.getParameter("buyer_actions");
    	//卖家动作集合
    	String seller_actions = request.getParameter("seller_actions");
    	//交易创建时间
    	String gmt_create = request.getParameter("gmt_create");
    	//交易支付时间
    	String gmt_payment = request.getParameter("gmt_payment");
    	//退款状态
    	String refund_status = request.getParameter("refund_status");
    	//交易退款时间
    	String gmt_refund = request.getParameter("gmt_refund");
    	//收货人姓名
    	String receive_name = request.getParameter("receive_name");
    	//收货人地址
    	String receive_address = request.getParameter("receive_address");
    	//收货人邮编
    	String receive_zip = request.getParameter("receive_zip");
    	//收货人电话
    	String receive_phone = request.getParameter("receive_phone");
    	//收货人手机
    	String receive_mobile = request.getParameter("receive_mobile");
        return "";
    }
    
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorEndpoint(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes,
                                Map<String, String> pathVars) throws PaymentException {
//        redirectAttributes.addAttribute(PAYMENT_PROCESSING_ERROR,
//                request.getParameter(PAYMENT_PROCESSING_ERROR));
        return "";
    }
}

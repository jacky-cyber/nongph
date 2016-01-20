package cn.globalph.controller.alipay;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.b2c.web.controller.checkout.CheckoutController;
import cn.globalph.common.payment.PaymentGatewayType;
import cn.globalph.common.payment.PaymentTransactionType;
import cn.globalph.common.payment.PaymentType;

import cn.globalph.controller.wechat.ResponseMessage;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.tool.hbm2x.StringUtils;
import org.mortbay.util.ajax.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author steven
 * @since 6/24/15
 */
@Controller
@RequestMapping("/alipay")
public class AlipayController extends CheckoutController {
    private static final Log LOG = LogFactory.getLog(AlipayController.class);
    @Value("${environment}")
    protected String environment;

    /**
     * @param orderNumber 订单号
     * @param request     HttpServletRequest
     * @return page
     */
    @ResponseBody
    @RequestMapping(value = "/prepare/{orderNumber}")
    public String alipay(@PathVariable String orderNumber, HttpServletRequest request, Model model) {
    	boolean isFromWechat = false;
    	String userAgent = request.getHeader("User-Agent");
    	if(StringUtils.isNotEmpty(userAgent)){
    		if(userAgent.toLowerCase().contains("micromessenger")){
    			isFromWechat = true;
    		}
    	}
    	if(!isFromWechat){
	        Order order = orderService.findOrderByOrderNumber(orderNumber);
	
	        String serverHost = request.getServerName() + ":" + request.getServerPort();
	        String notifyUrl = "http://" + serverHost + "/alipay/notify";
	        String returnUrl = "http://" + serverHost + "/alipay/return";
	        String wechat = (String)request.getParameter("wechat");
	        if(StringUtils.isNotEmpty(wechat)){
	        	 returnUrl = "http://" + serverHost + "/alipay/wechatReturn";
	        }
	       
	        String subject = "";
	        for (OrderItem orderItem : order.getOrderItems()) {
	            subject += orderItem.getName() + "_";
	        }
	        subject = subject.trim().replaceAll("_$", "");
	
	        //把请求参数打包成数组
	        Map<String, String> sParaTemp = new HashMap<>();
	        sParaTemp.put("service", "alipay.wap.create.direct.pay.by.user");
	        sParaTemp.put("partner", AlipayConfig.partner);
	        sParaTemp.put("seller_id", AlipayConfig.seller_id);
	        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
	        sParaTemp.put("payment_type", "1"); //支付类型 必填，不能修改
	        sParaTemp.put("notify_url", notifyUrl); //服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数
	        sParaTemp.put("return_url", returnUrl); //页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/
	        sParaTemp.put("out_trade_no", orderNumber);//商户订单号 商户网站订单系统中唯一订单号，必填
	        sParaTemp.put("subject", subject); //订单名称 必填
	        if ("PRD".equalsIgnoreCase(environment)) {
	            String totalFee = order.getTotal().stringValue();
	            sParaTemp.put("total_fee", totalFee);   //付款金额 必填
	        } else {
	            sParaTemp.put("total_fee", "0.01"); //for test
	        }
	//        sParaTemp.put("show_url", show_url); //商品展示地址 选填，需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html
	//        sParaTemp.put("body", body); //订单描述 选填
	//        sParaTemp.put("it_b_pay", it_b_pay); //超时时间 选填
	//        sParaTemp.put("extern_token", extern_token); //钱包token 选填
	
	        //建立请求
	        return AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
    	}else{
    		StringBuffer sbHtml = new StringBuffer();
            sbHtml.append("<script>window.location.href=\"/checkout/alipayinwechat?orderNumber="+ orderNumber + "\";</script>");
            return sbHtml.toString();
    	}
    }


    /*add  by jenny start*/
    @ResponseBody
    @RequestMapping(value = "/status/{orderNumber}")
    public ResponseMessage isConfirm(@PathVariable String orderNumber) {
        //获取订单，判断订单状态
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        if (OrderStatus.CONFIRMED.equals(order.getStatus())) {
            return ResponseMessage.success(true);
        } else {
            return ResponseMessage.error("not confirmed");
        }
    }
    /*add by jenny end*/

    /**
     * @param orderNumber 商户订单号
     * @param tradeNo     支付宝交易号
     * @param tradeStatus 交易状态
     * @param request     HttpServletRequest
     * @return page
     */
    @RequestMapping(value = "/return")
    public String returnUrl(@RequestParam("out_trade_no") String orderNumber,
                            @RequestParam("trade_no") String tradeNo,
                            @RequestParam("trade_status") String tradeStatus,
                            HttpServletRequest request) {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        if (OrderStatus.CONFIRMED.equals(order.getStatus())) {
            return "redirect:/account/orders/" + orderNumber;
        }
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }

            params.put(name, valueStr);
        }

        //计算得出通知验证结果
        boolean verify_result = AlipayNotify.verify(params);

        if (verify_result) {//验证成功

            if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                if (OrderStatus.SUBMITTED.equals(order.getStatus())) {
                    List<OrderPayment> paymentsToInvalidate = new ArrayList<>();
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

                    //Create a new Order Payment of wechatpay in type
                    OrderPayment wechatPayment = orderPaymentService.create();
                    wechatPayment.setType(PaymentType.ALIPAY_WAP_DIRECT);
                    wechatPayment.setReferenceNumber(tradeNo);
                    wechatPayment.setPaymentGatewayType(PaymentGatewayType.ALIPAY);
                    wechatPayment.setAmount(order.getTotal());
                    wechatPayment.setOrder(order);

                    PaymentTransaction transaction = orderPaymentService.createTransaction();
                    transaction.setAmount(wechatPayment.getAmount());
                    transaction.setRawResponse("Alipay Payment");
                    transaction.setType(PaymentTransactionType.AUTHORIZE_AND_CAPTURE);
                    transaction.setSuccess(true);

                    transaction.setOrderPayment(wechatPayment);
                    wechatPayment.addTransaction(transaction);
                    orderService.addPaymentToOrder(order, wechatPayment, null);

                    try {
                        checkoutService.performConfirm(order);
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            }

            return "redirect:/confirmation/" + orderNumber;
        } else {
            return "redirect:/failure/" + orderNumber;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/notify")
    public String notify(@RequestParam("out_trade_no") String orderNumber,
                         @RequestParam("trade_no") String tradeNo,
                         @RequestParam("trade_status") String tradeStatus,
                         HttpServletRequest request) {

        Order order = orderService.findOrderByOrderNumber(orderNumber);
        if (OrderStatus.CONFIRMED.equals(order.getStatus())) {
            return "success";
        }
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        if (AlipayNotify.verify(params)) {//验证成功

            if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                if (OrderStatus.SUBMITTED.equals(order.getStatus())) {
                    List<OrderPayment> paymentsToInvalidate = new ArrayList<>();
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

                    //Create a new Order Payment of wechatpay in type
                    OrderPayment wechatPayment = orderPaymentService.create();
                    wechatPayment.setType(PaymentType.ALIPAY_WAP_DIRECT);
                    wechatPayment.setReferenceNumber(tradeNo);
                    wechatPayment.setPaymentGatewayType(PaymentGatewayType.ALIPAY);
                    wechatPayment.setAmount(order.getTotal());
                    wechatPayment.setOrder(order);

                    PaymentTransaction transaction = orderPaymentService.createTransaction();
                    transaction.setAmount(wechatPayment.getAmount());
                    transaction.setRawResponse("Alipay Payment");
                    transaction.setType(PaymentTransactionType.AUTHORIZE_AND_CAPTURE);
                    transaction.setSuccess(true);

                    transaction.setOrderPayment(wechatPayment);
                    wechatPayment.addTransaction(transaction);
                    orderService.addPaymentToOrder(order, wechatPayment, null);

                    try {
                        checkoutService.performConfirm(order);
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                        return "fail";
                    }
                }
            }

            return "success";    //请不要修改或删除
        } else {//验证失败
            return "fail";
        }
    }
    
    /**
     * @param orderNumber 商户订单号
     * @param tradeNo     支付宝交易号
     * @param tradeStatus 交易状态
     * @param request     HttpServletRequest
     * @return page
     */
    @RequestMapping(value = "/wechatReturn")
    public String wechatReturnUrl(@RequestParam("out_trade_no") String orderNumber,
                            @RequestParam("trade_no") String tradeNo,
                            @RequestParam("trade_status") String tradeStatus,
                            HttpServletRequest request) {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        if (OrderStatus.CONFIRMED.equals(order.getStatus())) {
            return "redirect:/static/page/returnWechatRemind.html";
        }
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }

            params.put(name, valueStr);
        }

        //计算得出通知验证结果
        boolean verify_result = AlipayNotify.verify(params);

        if (verify_result) {//验证成功

            if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                if (OrderStatus.SUBMITTED.equals(order.getStatus())) {
                    List<OrderPayment> paymentsToInvalidate = new ArrayList<>();
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

                    //Create a new Order Payment of wechatpay in type
                    OrderPayment wechatPayment = orderPaymentService.create();
                    wechatPayment.setType(PaymentType.ALIPAY_WAP_DIRECT);
                    wechatPayment.setReferenceNumber(tradeNo);
                    wechatPayment.setPaymentGatewayType(PaymentGatewayType.ALIPAY);
                    wechatPayment.setAmount(order.getTotal());
                    wechatPayment.setOrder(order);

                    PaymentTransaction transaction = orderPaymentService.createTransaction();
                    transaction.setAmount(wechatPayment.getAmount());
                    transaction.setRawResponse("Alipay Payment");
                    transaction.setType(PaymentTransactionType.AUTHORIZE_AND_CAPTURE);
                    transaction.setSuccess(true);

                    transaction.setOrderPayment(wechatPayment);
                    wechatPayment.addTransaction(transaction);
                    orderService.addPaymentToOrder(order, wechatPayment, null);

                    try {
                        checkoutService.performConfirm(order);
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            }

            return "redirect:/static/page/returnWechatRemind.html";
        } else {
            return "redirect:/failure/" + orderNumber;
        }
    }  
}

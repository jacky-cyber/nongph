package cn.globalph.controller.wechat;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.web.controller.checkout.CheckoutController;
import cn.globalph.common.payment.PaymentGatewayType;
import cn.globalph.common.payment.PaymentTransactionType;
import cn.globalph.common.payment.PaymentType;
import cn.globalph.common.vendor.service.exception.PaymentException;
import com.wecubics.payment.utils.wx.WechatOrderQuery;
import com.wecubics.payment.utils.wx.XmlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author steven
 * @since 6/17/15
 */
@Controller
@RequestMapping("/wechatpay")
public class PayController extends CheckoutController {
    private static final Log LOG = LogFactory.getLog(PayController.class);
    @Resource(name = "wechatUtils")
    private WechatUtils wechatUtils;

    @Value("${environment}")
    protected String environment;

    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public String testPay(String orderNumber, String code, ModelMap modelMap) throws PaymentException, PricingException {
        String openId = wechatUtils.requestOpenId(code);
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        modelMap.put("order", order);
        modelMap.put("openId", openId);
        return "checkout/wechatPay";
    }

    @ResponseBody
    @RequestMapping(value = "/prepare", produces = "application/json", method = RequestMethod.POST)
    public Map pay(String openId, String orderNumber, HttpServletRequest request) {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        String serverHost = request.getServerName() + ":" + request.getServerPort();
        return wechatUtils.generateWechatPayPrepareInfo(serverHost, openId, order, "PRD".equalsIgnoreCase(environment));
    }

    @RequestMapping(value = "/return", method = RequestMethod.GET)
    public String success(String orderNumber) {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        if (OrderStatus.CONFIRMED.equals(order.getStatus())) {
            return "redirect:/account/orders/" + orderNumber;
        }
        Map<String, Object> orderStatus = wechatUtils.queryWechatPayStatus(orderNumber);
        if (WechatOrderQuery.SUCCESS.equals(orderStatus.get(WechatUtils.TRATE_STATE))) {
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
                wechatPayment.setType(PaymentType.WECHAT);
                wechatPayment.setReferenceNumber((String) orderStatus.get(WechatOrderQuery.TRANSATION_ID));
                wechatPayment.setPaymentGatewayType(PaymentGatewayType.WECHAT);
                wechatPayment.setAmount(order.getTotal());
                wechatPayment.setOrder(order);

                PaymentTransaction transaction = orderPaymentService.createTransaction();
                transaction.setAmount(wechatPayment.getAmount());
                transaction.setRawResponse("Wechat Payment");
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
        } else {
            return "redirect:/failure/" + orderNumber;
        }
        return "redirect:/confirmation/" + orderNumber;
    }

    @ResponseBody
    @RequestMapping(value = "/notify")
    public Map<String, String> notify(HttpServletRequest request) throws IOException {
        StringBuilder buffer = new StringBuilder();
        InputStream inputStream = request.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }

        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        Map<String, Object> result = XmlUtils.xml2Map(buffer.toString());
        String returnCode = (String) result.get("return_code");
        String resultCode = (String) result.get("result_code");
        String orderNumber = (String) result.get("out_trade_no");

        if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode)) {
            Order order = orderService.findOrderByOrderNumber(orderNumber);
            if (OrderStatus.CONFIRMED.equals(order.getStatus())) {
                return new HashMap<String, String>() {{
                    put("return_code", "SUCCESS");
                }};
            }
            Map<String, Object> orderStatus = wechatUtils.queryWechatPayStatus(orderNumber);
            if (WechatOrderQuery.SUCCESS.equals(orderStatus.get(WechatUtils.TRATE_STATE))) {
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
                    wechatPayment.setType(PaymentType.WECHAT);
                    wechatPayment.setReferenceNumber((String) orderStatus.get(WechatOrderQuery.TRANSATION_ID));
                    wechatPayment.setPaymentGatewayType(PaymentGatewayType.WECHAT);
                    wechatPayment.setAmount(order.getTotal());
                    wechatPayment.setOrder(order);

                    PaymentTransaction transaction = orderPaymentService.createTransaction();
                    transaction.setAmount(wechatPayment.getAmount());
                    transaction.setRawResponse("Wechat Payment");
                    transaction.setType(PaymentTransactionType.AUTHORIZE_AND_CAPTURE);
                    transaction.setSuccess(true);

                    transaction.setOrderPayment(wechatPayment);
                    wechatPayment.addTransaction(transaction);
                    orderService.addPaymentToOrder(order, wechatPayment, null);

                    try {
                        checkoutService.performConfirm(order);
                    } catch (Exception e) {
                        LOG.error(e.getMessage(), e);
                        return new HashMap<String, String>() {{
                            put("return_code", "FAIL");
                        }};
                    }
                }
            } else {
                return new HashMap<String, String>() {{
                    put("return_code", "FAIL");
                }};
            }
        }
        return new HashMap<String, String>() {{
            put("return_code", "SUCCESS");
        }};
    }
}

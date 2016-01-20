package cn.globalph.alipay.web.controller;

import cn.globalph.alipay.config.AlipayConfig;
import cn.globalph.alipay.util.AlipaySubmit;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.web.order.CartState;
import cn.globalph.passport.domain.Address;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付宝担保支付
 * 
 * @author qiutian
 */
@Controller("alipayescowProcessorController")
public class AlipayescowProcessorController {

	@RequestMapping(value = "/alipayescow", method = RequestMethod.POST)
	public @ResponseBody String retrieveHostedEndpoint(
			HttpServletRequest request) {
		Order order = (Order) CartState.getCart();
		
		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		// //////////////////////////////////请求参数//////////////////////////////////////

		// 支付类型
		String payment_type = "1";
		// 必填，不能修改
		// 服务器异步通知页面路径
		String notify_url = "http://create_partner_trade_by_buyer-JAVA-UTF-8/notify_url.jsp";
		// 需http://格式的完整路径，不能加?id=123这类自定义参数

		// 页面跳转同步通知页面路径
		String return_url = "http://115.29.211.82";
		// 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

		// 商户订单号
		String out_trade_no = order.getOrderNumber();
		// 商户网站订单系统中唯一订单号，必填

		
		// 订单名称
		String subject="品荟--";
		List<OrderItem> items = order.getOrderItems();
		subject = subject + items.get(0).getSku().getName();
		if(items.size()>1){
			subject = subject + "等多件";		
		}
		// 必填

		// 付款金额
		String price = order.getSubTotal().subtract(order.getOrderAdjustmentsValue()).toString();
		// 必填

		// 商品数量
		String quantity = "1";
		// 必填，建议默认为1，不改变值，把一次交易看成是一次下订单而非购买一件商品
		// 物流费用
		String logistics_fee = order.getFulfillmentGroups().get(0).getFulfillmentPrice().stringValue();
		// 必填，即运费
		// 物流类型
		String logistics_type = "EXPRESS";
		// 必填，三个值可选：EXPRESS（快递）、POST（平邮）、EMS（EMS）
		// 物流支付方式
		String logistics_payment = "BUYER_PAY";
		// 必填，两个值可选：SELLER_PAY（卖家承担运费）、BUYER_PAY（买家承担运费）
		// 订单描述

//		String body = new String(request.getParameter("WIDbody").getBytes(
//				"ISO-8859-1"), "UTF-8");
		// 商品展示地址
//		String show_url = new String(request.getParameter("WIDshow_url")
//				.getBytes("ISO-8859-1"), "UTF-8");
		// 需以http://开头的完整路径，如：http://www.商户网站.com/myorder.html
		
		Address address = null;
		try{
			address = order.getFulfillmentGroups().get(0).getAddress();
		}catch(Exception e){
			address = null;
		}
		if(address!=null){
		// 收货人姓名
		String receive_name = address.getReceiver();
		// 如：张三

		// 收货人地址
		String receive_address = address.getFirstLevelCommunity().getCommunityName()+address.getSecondLevelCommunity().getCommunityName()+address.getAddress();
		// 如：XX省XXX市XXX区XXX路XXX小区XXX栋XXX单元XXX号

		// 收货人邮编
		String receive_zip = address.getPostalCode();
		// 如：123456

//		// 收货人电话号码
//		String receive_phone = address.getPhone();
//		// 如：0571-88158090

		// 收货人手机号码
		String receive_mobile = address.getPhone();
		// 如：13312341234
		
		sParaTemp.put("receive_name", receive_name);
		sParaTemp.put("receive_address", receive_address);
		sParaTemp.put("receive_zip", receive_zip);
		sParaTemp.put("receive_mobile", receive_mobile);
	}
		// ////////////////////////////////////////////////////////////////////////////////

		
		sParaTemp.put("service", "create_partner_trade_by_buyer");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("seller_email", AlipayConfig.seller_email);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("price", price);
		sParaTemp.put("quantity", quantity);
		sParaTemp.put("logistics_fee", logistics_fee);
		sParaTemp.put("logistics_type", logistics_type);
		sParaTemp.put("logistics_payment", logistics_payment);

		// 建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
//		out.println(sHtmlText);
//
//		StringBuffer response = new StringBuffer();
//		response.append("<!DOCTYPE HTML>");
//		response.append("<!--[if lt IE 7]> <html class=\"no-js lt-ie9 lt-ie8 lt-ie7\" lang=\"en\"> <![endif]-->");
//		response.append("<!--[if IE 7]> <html class=\"no-js lt-ie9 lt-ie8\" lang=\"en\"> <![endif]-->");
//		response.append("<!--[if IE 8]> <html class=\"no-js lt-ie9\" lang=\"en\"> <![endif]-->");
//		response.append("<!--[if gt IE 8]><!--> <html class=\"no-js\" lang=\"en\"> <!--<![endif]-->");
//		response.append("<body>");
//		response.append("<h1>Mock Hosted Checkout</h1>");
//		response.append("<p>This is an example that demonstrates the flow of a Hosted Third Party Checkout Integration (e.g. PayPal Express Checkout)</p>");
//		response.append("<p>This customer will be prompted to either enter their credentials or fill out their payment information. Once complete, "
//				+ "they will be redirected back to either a confirmation page or a review page to complete checkout.</p>");
//		response.append("<form action=\""
//				+ paymentGatewayConfiguration.getHostedRedirectReturnUrl()
//				+ "\" method=\"GET\" id=\"NullPaymentGatewayRedirectForm\" name=\"NullPaymentGatewayRedirectForm\">");
//		response.append("<input type=\"hidden\" name=\""
//				+ NullPaymentGatewayConstants.TRANSACTION_AMT + "\" value=\""
//				+ transactionAmount + "\"/>");
//		response.append("<input type=\"hidden\" name=\""
//				+ NullPaymentGatewayConstants.ORDER_ID + "\" value=\""
//				+ orderId + "\"/>");
//		response.append("<input type=\"hidden\" name=\""
//				+ NullPaymentGatewayConstants.COMPLETE_CHECKOUT_ON_CALLBACK
//				+ "\" value=\"" + completeCheckoutOnCallback + "\"/>");
//		response.append("<input type=\"hidden\" name=\""
//				+ NullPaymentGatewayConstants.RESULT_MESSAGE + "\" value=\""
//				+ resultMessage + "\"/>");
//
//		response.append("<input type=\"submit\" value=\"Please Click Here To Complete Checkout\"/>");
//		response.append("</form>");
//		response.append("</body>");
//		response.append("</html>");

		return sHtmlText;
	}
}

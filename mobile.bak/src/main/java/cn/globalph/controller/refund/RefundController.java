package cn.globalph.controller.refund;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.OrderLog;
import cn.globalph.b2c.order.domain.Refund;
import cn.globalph.b2c.order.domain.RefundMedia;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.OrderLogService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.RefundMediaService;
import cn.globalph.b2c.order.service.RefundService;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.order.service.type.RefundStatus;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.cms.file.domain.StaticAsset;
import cn.globalph.cms.file.service.StaticAssetService;
import cn.globalph.cms.file.service.StaticAssetStorageDBService;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;

@Controller
@RequestMapping("/refund")
public class RefundController extends BasicController{
	@Resource(name = "blOrderService")
	private OrderService orderService;
	
	@Resource(name = "phOrderLogService")
	private OrderLogService orderLogService;
	
	@Resource(name = "blOrderItemService")
	private OrderItemService orderItemService;
	
	@Resource(name = "cmsStaticAssetService")
	private StaticAssetService staticAssetService;
	
    @Resource(name = "cmsStaticAssetStorageDBService")
    private StaticAssetStorageDBService staticAssetStorageService;
    
    @Resource(name = "phRefundService")
    private RefundService refundService;
    
    @Resource(name = "phRefundMediaService")
    private RefundMediaService refundMediaService;
	
	private static final Log LOG = LogFactory.getLog(RefundController.class);
	
	@RequestMapping(value="/{orderNumber}/items", method = RequestMethod.GET)
	public String refund(@PathVariable("orderNumber") String orderNumber, Model model, HttpServletRequest request){
		Customer customer = CustomerState.getCustomer();
		if(customer == null || customer.isAnonymous()){
			return "redirect:/";
		}
		
		if(StringUtils.isEmpty(orderNumber)){
			return "redirect:/";
		}
		
		Order order = orderService.findOrderByOrderNumber(orderNumber);
		
		if(order == null){
			return "redirect:/";
		}else{
			model.addAttribute("order", order);
			if(StringUtils.isNotEmpty((String)request.getParameter("v"))){
				model.addAttribute("vhr",true);
			}else{
				model.addAttribute("vhr",false);
			}
			return "refund/viewItems";
		}
		
	}
	
	/**
	 * 申请订单退款
	 * @param orderNumber
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{orderNumber}/order", method = RequestMethod.GET)
	public String refundOrder(@PathVariable("orderNumber") String orderNumber, Model model, HttpServletRequest request){
		Customer customer = CustomerState.getCustomer();
		if(customer == null || customer.isAnonymous()){
			return "redirect:/";
		}
		
		if(StringUtils.isEmpty(orderNumber)){
			return "redirect:/";
		}
		
		Order order = orderService.findOrderByOrderNumber(orderNumber);
		
		if(order == null){
			return "redirect:/";
		}else{
			model.addAttribute("order", order);
			return "refund/refundOrder";
		}
		
	}
	
	/**
	 * 提交申请订单退款
	 * @param orderNumber
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/{orderNumber}/order", method = RequestMethod.POST)
	public String doRefundOrder(@PathVariable("orderNumber") String orderNumber, Model model, HttpServletRequest request){
		Customer customer = CustomerState.getCustomer();
		if(customer == null || customer.isAnonymous()){
			return "redirect:/";
		}
		
		if(StringUtils.isEmpty(orderNumber)){
			return "redirect:/";
		}
		
		Order order = orderService.findOrderByOrderNumber(orderNumber);

		if(order == null){
			LOG.error("can not find order: " + orderNumber);
			return "redirect:/";
		}
		
		if(order.getRefund() != null){
			return "redirect:/";
		}
		
		//问题描述
		String refundDesc = request.getParameter("refundDesc");

		
		
		if(StringUtils.isEmpty(refundDesc)){
			model.addAttribute("errorTips", "问题描述不能为空！");
			model.addAttribute("order", order);
			return "refund/refundOrder";
		}

		
		Refund refund = refundService.createRefund();
		refund.setOrder(order);
		refund.setRefundDesc(refundDesc);
		refund.setRefundStatus(RefundStatus.PENDING);
		refund.setRefundTime(new Date(System.currentTimeMillis()));
		refundService.saveRefund(refund);
		
		order.setStatus(OrderStatus.ORDER_REFUND);
		try {
			orderService.save(order, false);
		} catch (PricingException e) {
			LOG.error("save order error");
		}
		
		OrderLog orderLog = orderLogService.create();
		orderLog.setDateCreated(new Date(System.currentTimeMillis()));
		orderLog.setDisplay(true);
		orderLog.setMessage("申请退款");
		orderLog.setOperator("SYSTEM");
		orderLog.setOrder(order);
		orderLog.setType("CUSTOMER");
		orderLogService.save(orderLog);
		
		return "refund/refundItemPending";
		
	}
	
	@RequestMapping(value="/{orderItemId}", method = RequestMethod.GET)
	public String toRefundItem(@PathVariable("orderItemId") String orderItemId, Model model){
		Customer customer = CustomerState.getCustomer();
		if(customer == null || customer.isAnonymous()){
			return "redirect:/";
		}
		
		Long oiid = null;
		try{
			oiid = Long.parseLong(orderItemId);
		}catch(NumberFormatException e){
			LOG.error("orderItemId:" + orderItemId +" can not parse to Long");
			return "redirect:/";
		}
		
		OrderItem orderItem = orderItemService.readOrderItemById(oiid);
		if(orderItem == null){
			LOG.error("can not find orderItem: " + oiid);
			return "redirect:/";
		}

		model.addAttribute("orderItem", orderItem);
		
		return "refund/refundItem";
		
	}
		
	@RequestMapping(value="/{orderItemId}", method = RequestMethod.POST)
	public String doRefundItem(@PathVariable("orderItemId") String orderItemId, Model model, HttpServletRequest request){
		Customer customer = CustomerState.getCustomer();
		if(customer == null || customer.isAnonymous()){
			return "redirect:/";
		}
		
		Long oiid = null;
		try{
			oiid = Long.parseLong(orderItemId);
		}catch(NumberFormatException e){
			LOG.error("orderItemId:" + orderItemId +" can not parse to Long");
			return "redirect:/";
		}
		
		OrderItem orderItem = orderItemService.readOrderItemById(oiid);
		if(orderItem == null){
			LOG.error("can not find orderItem: " + oiid);
			return "redirect:/";
		}
		
		if(orderItem.getRefund() != null){
			return "redirect:/";
		}
		
		//申请数量
		String wareNum = request.getParameter("wareNum");
		//问题描述
		String refundDesc = request.getParameter("refundDesc");
		//上传图片
		String[] uploadImgs = request.getParameterValues("uploadImg");

		Long wn = null;
		try{
			wn = Long.parseLong(wareNum);
		}catch(NumberFormatException e){
			LOG.error("apply num paser to int error, apply num: " + wareNum);
			return "redirect:/";
		}
		
		if(wn > orderItem.getQuantity() || wn <1){
			LOG.error("apply num invalid");
			return "redirect:/";
		}
		
		if(StringUtils.isEmpty(refundDesc)){
			model.addAttribute("errorTips", "问题描述不能为空！");
			model.addAttribute("orderItem", orderItem);
			return "refund/refundItem";
		}
		
		if(uploadImgs == null || uploadImgs.length == 0){
			model.addAttribute("errorTips", "请上传图片！");
			model.addAttribute("orderItem", orderItem);
			return "refund/refundItem";
		}
		
		Refund refund = refundService.createRefund();
		refund.setOrderItem(orderItem);
		refund.setRefundDesc(refundDesc);
		refund.setRefundStatus(RefundStatus.PENDING);
		refund.setRefundTime(new Date(System.currentTimeMillis()));
		refund.setRefundNum((Integer.parseInt(wn.toString())));
		refundService.saveRefund(refund);
		
		for(String uploadImg : uploadImgs){
			RefundMedia refundMedia =  refundMediaService.createRefundMedia();
			refundMedia.setMediaUrl(uploadImg);
			refundMedia.setRefund(refund);
			refundMediaService.saveRefundMedia(refundMedia);
		}
		
		Order order = orderItem.getOrder();
		order.setStatus(OrderStatus.ITEM_REFUND);
		try {
			orderService.save(order, false);
		} catch (PricingException e) {
			LOG.error("save order error");
		}
		
		OrderLog orderLog = orderLogService.create();
		orderLog.setDateCreated(new Date(System.currentTimeMillis()));
		orderLog.setDisplay(true);
		orderLog.setMessage("申请退货");
		orderLog.setOperator("SYSTEM");
		orderLog.setOrder(order);
		orderLog.setType("CUSTOMER");
		orderLogService.save(orderLog);
		
		return "refund/refundItemPending";
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/uploadImg", method = RequestMethod.POST,produces = "application/json")
	public Map upload(HttpServletRequest request,
	            @RequestParam("uploadImg") MultipartFile uploadImg) throws IOException {
	        
	        StaticAsset staticAsset = staticAssetService.createStaticAssetFromFile(uploadImg, null);
	        staticAssetStorageService.createStaticAssetStorageFromFile(uploadImg, staticAsset);

	        String staticAssetUrlPrefix = staticAssetService.getStaticAssetUrlPrefix();
	        if (staticAssetUrlPrefix != null && !staticAssetUrlPrefix.startsWith("/")) {
	            staticAssetUrlPrefix = "/" + staticAssetUrlPrefix;
	        }
	        
	        Map map = new HashMap<String,Object>();
	        map.put("success", staticAssetUrlPrefix + staticAsset.getFullUrl());
	        return map;
	    }    
}

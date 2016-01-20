package cn.globalph.controller.account;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderLog;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/account/orders")
public class OrderHistoryController extends cn.globalph.b2c.web.controller.cart.OrderHistoryController {
    @RequestMapping(method = RequestMethod.GET)
    public String viewOrderHistory(HttpServletRequest request, Model model) {
        return super.viewOrderHistory(request, model); 
    }
    
    @ResponseBody
    @RequestMapping(value="/ajax", method = RequestMethod.GET, produces = {"application/json"})
    public List<Map<String,Object>> viewOrderHistoryAjax(HttpServletRequest request, int page, String type){
        List<Order> orders;
        Customer customer = CustomerState.getCustomer();
        List<OrderStatus> sts = new ArrayList<OrderStatus>();
        if(customer == null || customer.isAnonymous()){
        	if(LOG.isInfoEnabled()){
        		LOG.info("view orders, customer is null or anonymous");
        	}
        	return null;
        }
    	if(StringUtils.isNotEmpty(type)){
        	if(type.equals("wait4pay")){
        		sts.add(OrderStatus.SUBMITTED);
                orders = orderService.findOrdersForCustomer(customer, sts, PAGE_SIZE, page);
        	}else if(type.equals("wait4receive")){
        		sts.add(OrderStatus.CONFIRMED);
                orders = orderService.findOrdersForCustomer(customer, sts, PAGE_SIZE, page);
        	}else if(type.equals("wait4comment")){
        		orders = orderService.findNoCommentOrdersForCustomer(customer, PAGE_SIZE, page);
        	}else{
        		sts.add(OrderStatus.CONFIRMED);
        		sts.add(OrderStatus.SUBMITTED);
        		sts.add(OrderStatus.COMPLETED);
                orders = orderService.findOrdersForCustomer(customer, sts, PAGE_SIZE, page);
        	}
        }else{
    		sts.add(OrderStatus.CONFIRMED);
    		sts.add(OrderStatus.SUBMITTED);
    		sts.add(OrderStatus.COMPLETED);
            orders = orderService.findOrdersForCustomer(customer, sts, PAGE_SIZE, page);
        }
        List<Map<String,Object>> lis = new ArrayList<Map<String,Object>>();
        for(Order order : orders){
        	Map<String,Object> li = new HashMap<String, Object>();
        	li.put("orderNumber", order.getOrderNumber());
        	try{
        		li.put("primaryImgUrl", 
        				order.getOrderItems().get(0).getSku().getSkuMedia().get("primary").getUrl());
        	}catch(Exception e){
        		if(LOG.isWarnEnabled()){
        			LOG.warn("get order.orderItem[0].sku.skuMedia[primary].url error,order id:" + order.getId());
        		}
        	}
        	try{
        		li.put("name", order.getOrderItems().get(0).getSku().getName());
        	}catch(Exception e){
        		if(LOG.isWarnEnabled()){
        			LOG.warn("get order.orderItem[0].sku.name error,order id:" + order.getId());
        		}
        	}
        	
        	li.put("price", order.getTotal().toString());
        	
        	try{
        		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		li.put("submitDate", fmt.format(order.getSubmitDate()));
        	}catch(Exception e){
        		if(LOG.isWarnEnabled()){
        			LOG.warn("get order.submitDate error,order id:" + order.getId());
        		}
        	}
        	
        	li.put("orderStatus", order.getStatus().getType());
        	li.put("isReview", order.isReview());  
        	li.put("isRefundAvailable", order.isRefundAvailable());  
        	li.put("hasRefundItem", order.getHasRefundItem()); 
        	lis.add(li);
        }       
        return lis;
    }

    @RequestMapping(value = "/{orderNumber}", method = RequestMethod.GET)
    public String viewOrderDetails(HttpServletRequest request, Model model, @PathVariable("orderNumber") String orderNumber) {

        Order order = orderService.findOrderByOrderNumber(orderNumber);
        if (order == null) {
            throw new IllegalArgumentException("The orderNumber provided is not valid");
        }
        List<Order> suborderList = orderService.readSuborderList(order.getId());
        order.setSuborderList(suborderList);
        model.addAttribute("order", order);
        return isAjaxRequest(request) ? getOrderDetailsView() : getOrderDetailsRedirectView();
    }

    @RequestMapping(value = "/log/{orderNumber}", method = RequestMethod.GET)
    public String viewOrderLog(Model model, @PathVariable("orderNumber") String orderNumber) {

        Order order = orderService.findOrderByOrderNumber(orderNumber);
        if (order == null) {
            throw new IllegalArgumentException("The orderNumber provided is not valid");
        }
        List<OrderLog> orderLogs = orderLogService.findOrderLogsByOrderId(order.getId(), true);
        Collections.sort(orderLogs, new Comparator<OrderLog>() {
            @Override
            public int compare(OrderLog o1, OrderLog o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        model.addAttribute("order", order);
        return "account/orderLog";
    }

}

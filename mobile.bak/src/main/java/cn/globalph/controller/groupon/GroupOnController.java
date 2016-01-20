package cn.globalph.controller.groupon;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.OrderItemImpl;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.type.OrderItemType;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.product.dao.SkuDao;
import cn.globalph.b2c.product.domain.GroupOn;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.service.GroupOnService;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;

@Controller
@RequestMapping(value = "/groupOn")
public class GroupOnController {
	@Resource(name = "blSkuDao")
	private SkuDao skuDao;
	
	@Resource(name = "blOrderService")
	private OrderService orderService;
	
	@Resource(name = "blOrderItemService")
	private OrderItemService orderItemService;
	
	@Resource(name = "phGroupOnService")
	private GroupOnService groupOnService;
	
	private static final Log LOG = LogFactory.getLog(GroupOnController.class);
	
	@RequestMapping("/participateIn/{skuId}")
    public String participateIn( HttpServletRequest request, 
    		              HttpServletResponse response, 
    		              Model model,@PathVariable Long skuId) {
		Customer customer = CustomerState.getCustomer();
		if(customer == null || customer.isAnonymous()){
			return "redirect:/login";
		}
		
        Sku sku = skuDao.readSkuById(skuId);
        if(sku == null){
        	LOG.warn("sku not exsit, sku id: " + skuId);
        	return "redirect:/";
        }
        
        if(!sku.getIsGroupOn()){
        	LOG.warn("sku not supprot group on, sku id: " + skuId);
        	return "redirect:/";
        }
        
        GroupOn groupOn = sku.getGroupOn();
        if(groupOn == null){
        	LOG.warn("sku(id:"+skuId + ") is group on, but get groupOn null");
        	return "redirect:/";
        }
        
        if(!groupOn.isValid()){
        	LOG.warn("group on activity is over");
        	return "redirect:/";
        }
        
        List<Order> orders = groupOn.getOrders();
        boolean hasGroupOn = false;
        for(Order order : orders){
        	if(order.getCustomer().equals(customer) && order.getStatus().equals(OrderStatus.GROUP_ON)){
        		hasGroupOn = true;
        		break;
        	}
        }
        
      if(hasGroupOn){
    	  model.addAttribute("message","hasGroupOn");
    	  return "redirect:/groupOn/myOrders";
      }else{
    	  groupOn.setParticipateInCount(groupOn.getParticipateInCount() + 1);
    	  groupOnService.saveGroupOn(groupOn);
    	  
    	  Order order = orderService.createNewCartForCustomer(customer);
    	  order.setStatus(OrderStatus.GROUP_ON);
    	  order.setReview(false);
    	  order.setDeliveryInfoSent(false);
    	  order.setShipped(false);
    	  order.setProvider(sku.getProduct().getProvider());
    	  order.setGroupOn(groupOn);
    	  order.setIsGroupOn(true);
          try {
              orderService.save(order, false);
          } catch (PricingException e) {
			  LOG.error(e.getMessage(), e);
          }
    	  
    	  OrderItem orderItem = new OrderItemImpl();
    	  orderItem.setName(sku.getName());
    	  orderItem.setOrderItemType(OrderItemType.BASIC);
    	  orderItem.setQuantity(groupOn.getSkuQuantity());
    	  orderItem.setSalePrice(sku.getSalePrice());
    	  orderItem.setRetailPrice(sku.getRetailPrice());
    	  orderItem.setRetailPriceOverride(false);
    	  orderItem.setSalePriceOverride(false);
    	  orderItem.setOrder(order);
    	  orderItem.setSku(sku);
    	  orderItem.setIsUsed(true);
    	  orderItem.setIsReview(false);
    	  orderItemService.saveOrderItem(orderItem);
    	  
      }
      model.addAttribute("message","success");
      return "redirect:/groupOn/myOrders";
	}
	
	@RequestMapping("/myOrders")
    public String myOrders( HttpServletRequest request, 
    		              HttpServletResponse response, 
    		              Model model) {
		Customer customer = CustomerState.getCustomer();
		List<Order> orders = orderService.findOrdersForCustomer(customer, OrderStatus.GROUP_ON);
		model.addAttribute("orders", orders);
		return "account/groupOn";
	}
	
	@RequestMapping("/{orderId}/cancel")
    public String exitGroupOn( HttpServletRequest request, 
    		              HttpServletResponse response, 
    		              Model model,@PathVariable Long orderId) {
		Order order = orderService.getOrderById(orderId);
		if(order == null||
			!order.getCustomer().getId().equals(CustomerState.getCustomer().getId())){
			return "redirect:/";
		}
		order.setStatus(OrderStatus.CANCELLED);
        try {
            orderService.save(order, false);
        } catch (PricingException e) {
			LOG.error(e.getMessage(), e);
        }
        
        GroupOn groupOn = order.getGroupOn();
        groupOn.setParticipateInCount(groupOn.getParticipateInCount() - 1);
        groupOnService.saveGroupOn(groupOn);
        
        model.addAttribute("message", "exitSuccess");
        return "redirect:/groupOn/myOrders";
	}
}

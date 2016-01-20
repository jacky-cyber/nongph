package cn.globalph.controller.checkout;

import cn.globalph.b2c.checkout.service.exception.CheckoutException;
import cn.globalph.b2c.coupon.domain.CouponCode;
import cn.globalph.b2c.coupon.type.CouponCodeType;
import cn.globalph.b2c.delivery.DeliveryType;
import cn.globalph.b2c.order.domain.*;
import cn.globalph.b2c.order.service.exception.IllegalCartOperationException;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.product.domain.*;
import cn.globalph.b2c.web.checkout.model.BillingInfoForm;
import cn.globalph.b2c.web.checkout.model.GiftCardInfoForm;
import cn.globalph.b2c.web.checkout.model.OrderInfoForm;
import cn.globalph.b2c.web.checkout.model.ShippingInfoForm;
import cn.globalph.b2c.web.controller.checkout.CheckoutController;
import cn.globalph.b2c.web.order.CartState;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.money.Money;
import cn.globalph.common.payment.PaymentTransactionType;
import cn.globalph.common.payment.PaymentType;
import cn.globalph.common.vendor.service.exception.PaymentException;
import cn.globalph.controller.wechat.WechatUtils;
import cn.globalph.coupon.apply.OrderDetailToApplyCoupon;
import cn.globalph.coupon.domain.CustomerCoupon;
import cn.globalph.passport.domain.Address;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GlobalphCheckoutController extends CheckoutController {
    private static final Log LOG = LogFactory.getLog(GlobalphCheckoutController.class);
    @Resource(name = "wechatUtils")
    private WechatUtils wechatUtils;

    @RequestMapping("/checkout")
    public String checkout(HttpServletRequest request, Model model) {
    	String oid = request.getParameter("oid");
    	Order order = null;
    	if(StringUtils.isNotEmpty(oid)){
    		try{
    			Long orderId = Long.parseLong(oid);
    			order = orderService.getOrderById(orderId);
    			request.getSession().setAttribute("oid", orderId);
    		}catch(NumberFormatException e){
    			order = CartState.getCart();
    		}
    	}
    	
    	if(order == null){
    		order = CartState.getCart();
    		request.getSession().removeAttribute("oid");
    	}
    	
    	if(order instanceof NullOrderImpl){
    		 return "redirect:/cart";
    	 }

        if (!hasUsedItems(order)) {
            return "redirect:/cart";
        }

    	if(order.getIsGroupOn()){
    		GroupOn groupOn = order.getGroupOn();
    		if(groupOn.isValid()){
    			return "redircet:/";
    		}else{
    			Integer participateInCount = groupOn.getParticipateInCount();
    			Money subTotal = null;
    			Money price = null;
    			List<GroupOnAttr> attrs = groupOn.getGroupOnAttrs();
    			for(GroupOnAttr attr : attrs){
    				if(participateInCount >= attr.getCountRangeFrom() &&
    						participateInCount <= attr.getCountRangeTo()){
    					price = attr.getPrice();
    					subTotal = attr.getPrice().multiply(groupOn.getSkuQuantity());
    					break;
    				}
    			}
    			
    			if(price != null){
    				List<OrderItem> orderItems = order.getOrderItems();
    				OrderItem orderItem = orderItems.get(0);
    				orderItem.setPrice(price);
    				orderItemService.saveOrderItem(orderItem);
    			}
    			
    			if(subTotal != null){
    				order.setSubTotal(subTotal);
    				order.setTotal(subTotal);
    		        try {
    		            orderService.save(order, true);
    		        } catch (Exception e) {
    		            LOG.error(e.getMessage(), e);
    		        }
    			}
    		}
    	}
    	
		Customer customer = CustomerState.getCustomer();
		OrderAddress orderAddress = order.getOrderAddress();

		if (orderAddress == null) {
			Address defaultAddress = addressService
					.readDefaultAddressByCustomerId(customer.getId());
			if (defaultAddress != null) {
				orderAddress = new OrderAddressImpl();
				orderAddress.setAddress(defaultAddress.getAddress());
				orderAddress.setCity(defaultAddress.getCity());
				orderAddress.setDistrict(defaultAddress.getDistrict());
                orderAddress.setCommunity(defaultAddress.getCommunity());
                orderAddress.setOrder(order);
				orderAddress.setPhone(defaultAddress.getPhone());
				orderAddress.setProvince(defaultAddress.getProvince());
				orderAddress.setPostalCode(defaultAddress.getPostalCode());
				orderAddress.setReceiver(defaultAddress.getReceiver());
				orderAddressService.save(orderAddress);
			} else {
				List<Address> addresses = addressService
						.readActiveAddressesByCustomerId(customer.getId());
				if (addresses != null && addresses.size() > 0) {
					Address address = addresses.get(0);
					orderAddress = new OrderAddressImpl();
					orderAddress.setAddress(address.getAddress());
					orderAddress.setCity(address.getCity());
					orderAddress.setDistrict(address.getDistrict());
                    orderAddress.setCommunity(address.getCommunity());
                    orderAddress.setOrder(order);
					orderAddress.setPhone(address.getPhone());
					orderAddress.setProvince(address.getProvince());
					orderAddress.setPostalCode(address.getPostalCode());
					orderAddress.setReceiver(address.getReceiver());
					orderAddressService.save(orderAddress);
				}
			}
		}

        model.addAttribute("address", orderAddress);
        Map<Provider, List<OrderItem>> providerMap = new HashMap<>();
        List<OrderItem> orderItems = order.getUsedOrderItems();
        for (OrderItem orderItem : orderItems) {
            Provider provider = orderItem.getSku().getProduct().getProvider();

            if (!providerMap.containsKey(provider)) {
                providerMap.put(provider, new ArrayList<OrderItem>());
            }
            List<OrderItem> orderItemList = providerMap.get(provider);
            orderItemList.add(orderItem);
        }

		List<CustomerCoupon> customerCoupons = null;
		if(CustomerState.getCustomer() != null
 			&& order != null){
			customerCoupons = couponService.getCurrentAvailableCouponByCustomerId(CustomerState.getCustomer().getId(), orderService.generateOrderDetailToApplyCoupon(order));
		}   

        for (Provider provider : providerMap.keySet()) {
            List<OrderItem> orderItemList = providerMap.get(provider);
            if (provider.isSupportPickup()) {
                OrderItem lastOrderItem = orderItemList.get(orderItemList.size() - 1);

                //给最后一个order item 设置默认自提点 在DetermineDeliveryTypeActivity里, 系统将根据此信息设置订单信息
                if (StringUtils.isEmpty(lastOrderItem.getDeliveryType()) && lastOrderItem.getPickupAddress() == null) {
                    if (provider.isSupportPickup()) {
                        List<PickupAddress> pickupAddresses = provider.getPickupAddresses();
                        if (pickupAddresses != null && !pickupAddresses.isEmpty()) {
                            for (PickupAddress pickupAddress : pickupAddresses) {
                                if (pickupAddress.isDefault()) {
                                    lastOrderItem.setDeliveryType(DeliveryType.PICKUP.getType());
                                    lastOrderItem.setPickupAddress(pickupAddress);
                                    orderItemService.saveOrderItem(lastOrderItem);
                                }
                            }
                        }
                    }

                }
            }
        }

        model.addAttribute("providerMap", providerMap);
         model.addAttribute("order", order);
         model.addAttribute("customerCoupons", customerCoupons);
        try {
            orderService.save(order, true);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
         return getCheckoutView();
    }

    private Boolean hasUsedItems(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        if (orderItems != null && !orderItems.isEmpty()) {
            for (OrderItem orderItem : orderItems) {
                if (orderItem.isUsed() && orderItem.getQuantity() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @RequestMapping("/checkout/choosePickupAddress")
    public String choosePickupAddress(Long orderItemId, Long pickupAddressId) {
        Order order = CartState.getCart();
        if (order instanceof NullOrderImpl) {
            return "redirect:/cart";
        }

        OrderItem orderItem = orderItemService.readOrderItemById(orderItemId);
        List<PickupAddress> pickupAddresses = orderItem.getSku().getProduct().getProvider().getPickupAddresses();
        if (pickupAddresses != null && !pickupAddresses.isEmpty()) {
            for (PickupAddress pickupAddress : pickupAddresses) {
                if (pickupAddressId.equals(pickupAddress.getId())) {
                    orderItem.setPickupAddress(pickupAddress);
                    orderItem.setDeliveryType(DeliveryType.PICKUP.getType());
                    orderItemService.saveOrderItem(orderItem);
                    return "redirect:/checkout";
                }
            }
        }
        return "redirect:/checkout";
    }

    @RequestMapping("/checkout/chooseLogistics")
    public String chooseLogistics(Long orderItemId) {
        Order order = CartState.getCart();
        if (order instanceof NullOrderImpl) {
            return "redirect:/cart";
        }

        OrderItem orderItem = orderItemService.readOrderItemById(orderItemId);
        List<PickupAddress> pickupAddresses = orderItem.getSku().getProduct().getProvider().getPickupAddresses();
        if (pickupAddresses != null && !pickupAddresses.isEmpty()) {
            orderItem.setPickupAddress(null);
            orderItem.setDeliveryType(DeliveryType.LOGISTICS.getType());
            orderItemService.saveOrderItem(orderItem);
            return "redirect:/checkout";
        }
        return "redirect:/checkout";
    }


    @RequestMapping("/checkout/submitOrder")
    public String submitOrder(HttpServletRequest request, Model model, RedirectAttributes attributes) {
    	Order cart = null;
    	Long oid = (Long)request.getSession().getAttribute("oid");
        String remark = request.getParameter("remark");
    	if(oid != null){
    		cart = orderService.getOrderById(oid);
    	}
    	if(cart == null){
    		cart = CartState.getCart();
    		oid = null;
    	}
        if(remark.length()>150){
            model.addAttribute("errormsg", "很抱歉备注信息过长！");
            return "redirect:/checkout";
        }
        cart.setRemark(remark);
    	 if(cart.getApplyCoupon() != null){
    		 CustomerCoupon customerCoupon = cart.getApplyCoupon();
    		 if(!couponService.isAvailable(customerCoupon, orderService.generateOrderDetailToApplyCoupon(cart))){
                 LOG.warn("coupon " + cart.getApplyCoupon().getId() + " could not be applyed to order " + cart.getId());
    			 cart.setApplyCoupon(null);
                 cart.setCouponDiscount(Money.ZERO);
                 try {
                     orderService.save(cart, true);
                 } catch (PricingException e) {
                     LOG.error(e.getMessage(), e);
                 }
    			 model.addAttribute("errormsg", "请重新选择优惠券");
    			 return "redirect:/checkout";
    		 }
    	 }
    
    	 if (cart.getOrderAddress() != null || !cart.getNeedAddress()) {
             try {
                 orderService.save(cart, false);
             } catch (PricingException e) {
                 LOG.error(e.getMessage(), e);
             }
         }else{
        	 attributes.addAttribute("error", "missAddress");
        	 if(oid != null){
        		 attributes.addAttribute("oid", oid);
        	 }
        	 return "redirect:/checkout";
         }
         try {
             orderService.preValidateCartOperation(cart);
         } catch (IllegalCartOperationException ex) {
             model.addAttribute("cartRequiresLock", true);
         }
         
         if (!(cart instanceof NullOrderImpl)) {
             model.addAttribute("paymentRequestDTO", dtoTranslationService.translateOrder(cart));
         }
         populateModelWithReferenceData(request, model);
         //下单成功，生成订单号
         if (!(cart instanceof NullOrderImpl)) {
             List<OrderItem> orderItems = cart.getOrderItems();
             //不参与结算的订单项
             List<OrderItem> unUseOrderItems = new ArrayList<OrderItem>();
             List<OrderItem> usedOrderItems = new ArrayList<OrderItem>();
             for (OrderItem orderItem : orderItems) {
                 if (!orderItem.isUsed()) {
                     unUseOrderItems.add(orderItem);
                 }else{
                	 usedOrderItems.add(orderItem);
                 }
             }
             for(OrderItem unUseOrderItem : unUseOrderItems){
            	 orderItems.remove(unUseOrderItem);
             }
             
             //秒杀活动商品数量限制
             for(OrderItem uoi : usedOrderItems){
            	 Sku sku = uoi.getSku();
            	 if(sku.getIsSeckilling()){
					 if(!sku.isSeckillingValid()){
                         attributes.addAttribute("errormsg", "秒杀商品：" + sku.getName() + "活动已结束，无法参与结束，请将其移除购物车！");
                         return getCheckoutPageRedirect();
                     }else{
                         int hasBuy = orderService.getSkuCountInOrders(sku.getId(), CustomerState.getCustomer().getId());
                         int canBuy = sku.getLimit();
                         int nowBuy = uoi.getQuantity();
                         if(hasBuy >= canBuy){
                             attributes.addAttribute("errormsg", "秒杀商品：" + sku.getName() + "，您已超过最大购买限度，无法购买此商品");
                             return getCheckoutPageRedirect();
                         }else{
                             int nowMaxCanBuy = canBuy - hasBuy;
                             if(nowBuy > nowMaxCanBuy){
                                 attributes.addAttribute("errormsg", "秒杀商品：" + sku.getName() + "，您最多还能购买" + nowMaxCanBuy +"件");
                                 return getCheckoutPageRedirect();
                             }
                         }

                     }
				 }
             }
             
             if (unUseOrderItems.size() > 0) {
                 Order order = orderService.createNewCartForCustomer(cart.getCustomer());
                 order.setSubTotal(Money.ZERO);
                 order.setTotal(Money.ZERO);
                 order.setTotalFulfillmentCharges(Money.ZERO);
                 for (OrderItem oi : unUseOrderItems) {
                     oi.setOrder(order);
                     orderItemService.saveOrderItem(oi);
                 }
                 order.setOrderItems(unUseOrderItems);
                 try {
                     orderService.save(order, false);
                 } catch (PricingException e) {
                     LOG.error(e.getMessage(), e);
                 }
             }
             
             if (StringUtils.isEmpty(cart.getOrderNumber())) {
                 try {
                     checkoutService.performCheckout(cart);
                 } catch (CheckoutException e) {
                     model.addAttribute("errormsg", "提交订单出错");
                     LOG.error(e.getMessage(), e);
                     return getCheckoutView();
                 }
             }
         }
         
         model.addAttribute("order", cart);
         model.addAttribute("appid", wechatUtils.getAppId());
         if(oid!=null){
        	 request.getSession().removeAttribute("oid");
         }
         if(cart.getOrderItems() == null || cart.getOrderItems().size() == 0 || cart.getUsedItemsNum() == 0){
        	 return "redirect:/cart";
         }
         return "checkout/preparePay";
    }
    
    @RequestMapping("/checkout/{orderNumber}")
    public String checkout(HttpServletRequest request, Model model, @PathVariable(value = "orderNumber") String orderNumber) {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        model.addAttribute("order", order);
        model.addAttribute("appid", wechatUtils.getAppId());
        if(order.getOrderItems() == null || order.getOrderItems().size() == 0 || order.getUsedItemsNum() == 0){
       	 return "redirect:/cart";
        }
        return "checkout/preparePay";
    }
    
    @RequestMapping(value = "/checkout/coupon", method = RequestMethod.GET)
    public String useCoupon(HttpServletRequest request, Model model) {
    	Order order = null;
    	Long oid = (Long)request.getSession().getAttribute("oid");
    	if(oid != null){
    		order = orderService.getOrderById(oid);
    	}
    	if(order == null){
    		order = CartState.getCart();
    		oid = null;
    	}
    	
		List<CustomerCoupon> customerCoupons = null;
		if(CustomerState.getCustomer() != null
    			&& order != null){
			customerCoupons = couponService.getCurrentAvailableCouponByCustomerId(CustomerState.getCustomer().getId(), orderService.generateOrderDetailToApplyCoupon(order));
		
		} 
        model.addAttribute("customerCoupons", customerCoupons);
        return "checkout/coupon";
    }
    
    /**
     * 使用优惠码页面
     * @return
     */
    @RequestMapping(value = "/checkout/applyCouponCode", method = RequestMethod.GET)
    public String applyCouponCodePage(Model model){
    	return getApplyCouponCodeView();
    }
    
    /**
     * 使用优惠码处理请求
     * @param code
     * @param model
     * @return
     */
    @RequestMapping(value = "/checkout/applyCouponCode", method = RequestMethod.POST)
    public String applyCouponCode(@ModelAttribute(value = "code") String code, Model model){
    	if(StringUtils.isEmpty(code)){
    		return getCheckoutPageRedirect();
    	}
    	
    	CouponCode couponCode = couponCodeService.findCouponCodeByCode(code);
    	if(couponCode == null){
    		model.addAttribute("responseMsg", "您填写的品荟码无效");	
    		return getApplyCouponCodeView();
    	}
    	
    	if(!couponCode.getStatus().equals(CouponCodeType.ACTIVE)){
    		model.addAttribute("responseMsg", "您填写的品荟码已使用");	
    		return getApplyCouponCodeView();
    	}
    	
    	if(!couponCode.isValid()){
    		model.addAttribute("responseMsg", "您填写的品荟码已过期");	
    		return getApplyCouponCodeView();
    	}
    	
    	Order order = CartState.getCart();
    	Customer customer = order.getCustomer();
    	order.setCouponCode(couponCode);
    	order.setCouponCodeDiscount(new Money(couponCode.getDiscountAmount(customer.getId())));
    	try {
			orderService.save(order, true);
		} catch (PricingException e) {
			LOG.error("save order error:" + e.getMessage());
		}
    	return getCheckoutPageRedirect();
    }
    
    /**
     * 取消使用优惠码
     * @return
     */
    @RequestMapping(value = "/checkout/cancelCode", method = RequestMethod.GET)
    public String cancelCode(){
    	Order order = CartState.getCart();
    	order.setCouponCode(null);
    	order.setCouponCodeDiscount(Money.ZERO);
    	try {
			orderService.save(order, true);
		} catch (PricingException e) {
            LOG.error(e.getMessage(), e);
		}
    	return getCheckoutPageRedirect();  
    }

    /*add by jenny s 15/09/11*/
    /**
     * 使用积分页面
     * @return
     */
    @RequestMapping(value = "/checkout/applyBonusPoint", method = RequestMethod.GET)
    public String applyBonusPointPage(Model model) {
        return getApplyBonusPointView();
    }

    /**
     * 使用积分处理请求
     *
     * @param bonus
     * @param model
     * @return
     */
    @RequestMapping(value = "/checkout/applyBonusPoint", method = RequestMethod.POST)
    public String applyBonusPoint(@ModelAttribute(value = "bonus") String bonus, Model model) {
        Order order = CartState.getCart();
        Customer customer = order.getCustomer();
        Integer bonusPoint;
        try {
            bonusPoint = Integer.parseInt(bonus);
            if (bonusPoint < 0 || bonusPoint > customer.getBonusPoint()) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            model.addAttribute("responseMsg", "请输入0至" + customer.getBonusPoint() + "之间的整数!");
            return getApplyBonusPointView();
        }
        order.setDeductionBonusPoint(bonusPoint);
        try {
            orderService.save(order, true);
        } catch (PricingException e) {
            LOG.error("save order error:" + e.getMessage());
        }
        return getCheckoutPageRedirect();
    }

    /**
     * 取消使用积分
     *
     * @return
     */
    @RequestMapping(value = "/checkout/cancelBonus", method = RequestMethod.GET)
    public String cancelBonus() {
        Order order = CartState.getCart();
        order.setTotal(new Money(order.getTotal().getAmount().doubleValue() + order.getDeductionBonusPoint() * 0.01));
        order.setDeductionBonusPoint(0);
        try {
            orderService.save(order, true);
        } catch (PricingException e) {
            LOG.error(e.getMessage(), e);
        }
        return getCheckoutPageRedirect();
    }

    /*add by jenny e 15/09/11 */
    @RequestMapping(value = "/checkout/coupon", method = RequestMethod.POST)
    public String useCoupon(HttpServletRequest request, Model model,@ModelAttribute(value="applyCouponId") Long applyCouponId) {
    	Order order = null;
    	Long oid = (Long)request.getSession().getAttribute("oid");
    	if(oid != null){
    		order = orderService.getOrderById(oid);
    	}
    	if(order == null){
    		order = CartState.getCart();
    		oid = null;
    	}
    	
    	if(applyCouponId == null || applyCouponId == 0){
    		order.setCouponDiscount(Money.ZERO);
        	order.setApplyCoupon(null);
        	try {
    			orderService.save(order, false);
    		} catch (PricingException e) {
    			model.addAttribute("errorMessage", "不使用优惠券时出错");
    			List<CustomerCoupon> customerCoupons = null;
    			if(CustomerState.getCustomer() != null
            			&& order != null){
    				customerCoupons = couponService.getCurrentAvailableCouponByCustomerId(CustomerState.getCustomer().getId(), orderService.generateOrderDetailToApplyCoupon(order));
    			}   
                model.addAttribute("customerCoupons", customerCoupons);
        		return "checkout/coupon";
    		}	
            if(oid != null){
           	 return "redirect:/checkout?oid=" + oid;
           }else{
           	 return "redirect:/checkout";
           }
    	}
    	
    	CustomerCoupon customerCoupon = customerCouponService.findCustomerCouponById(applyCouponId);
    	List<CustomerCoupon> customerCoupons = null;
    	
    	OrderDetailToApplyCoupon orderDetailToApplyCoupon = orderService.generateOrderDetailToApplyCoupon(order);
    	
    	if(!couponService.isAvailable(customerCoupon, orderDetailToApplyCoupon)){
    		model.addAttribute("errorMessage", "此优惠券不可用");
    		if(CustomerState.getCustomer() != null
    			&& order != null){
    			customerCoupons = couponService.getCurrentAvailableCouponByCustomerId(CustomerState.getCustomer().getId(), orderService.generateOrderDetailToApplyCoupon(order));
    		}
            model.addAttribute("customerCoupons", customerCoupons);
    		return "checkout/coupon";
    	}
    	
    	Money mon = new Money(couponService.getDiscount(customerCoupon.getCoupon(), orderDetailToApplyCoupon));
    	order.setCouponDiscount(mon);
    	order.setApplyCoupon(customerCoupon);
    	try {
			orderService.save(order, false);
		} catch (PricingException e) {
			model.addAttribute("errorMessage", "使用优惠券时出错");
    		if(CustomerState.getCustomer() != null
    			&& order != null){
    			customerCoupons = couponService.getCurrentAvailableCouponByCustomerId(CustomerState.getCustomer().getId(), orderService.generateOrderDetailToApplyCoupon(order));
    		}  
            model.addAttribute("customerCoupons", customerCoupons);
    		return "checkout/coupon";
		}	
    	
        if(oid != null){
       	 return "redirect:/checkout?oid=" + oid;
       }else{
       	 return "redirect:/checkout";
       }
    }

    @RequestMapping(value = "/checkout/preparePay", method = RequestMethod.POST)
    public String prepare(String orderNumber, Model model) throws ServiceException {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        model.addAttribute("order", order);
        if (order.getOrderAddress() == null) {
            return "redirect:/checkout/" + orderNumber + "?error=missAddress";
        }
        model.addAttribute("appid", wechatUtils.getAppId());
        return "checkout/preparePay";
    }
    
    @RequestMapping(value = "/checkout/alipayinwechat", method = RequestMethod.GET)
    public String alipayInWechat(String orderNumber, Model model, HttpServletRequest request) throws ServiceException {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
      	boolean isFromWechat = false;
    	String userAgent = request.getHeader("User-Agent");
    	if(StringUtils.isNotEmpty(userAgent)){
    		if(userAgent.toLowerCase().contains("micromessenger")){
    			isFromWechat = true;
    		}
    	}
    	if(isFromWechat){
	        model.addAttribute("order", order);
	        return "checkout/alipayInWechat";
    	}else{
    		return "redirect:/alipay/prepare/" + order.getOrderNumber() + "?wechat=true";
    	}
    }

    @RequestMapping(value = "/checkout/{orderNumber}/cancel", method = RequestMethod.GET)
    public String cancelOrder(@PathVariable String orderNumber, Model model, HttpServletRequest request, RedirectAttributes attributes) throws ServiceException {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        Customer customer = CustomerState.getCustomer();
        if(customer == null 
        		|| customer.isAnonymous() 
        		|| order == null
        		|| order.getCustomer() == null
        		|| order.getCustomer().getId() == null
        		|| !customer.getId().equals(order.getCustomer().getId())){
        	return "redirect:/account/menu";
        } else {
            try {
                checkoutService.performCancel(order);
            } catch (CheckoutException e) {
                LOG.error("cancel order, but save error");
                LOG.error(e.getMessage(), e);
				attributes.addFlashAttribute("msg", "cancelfail");
			}
            attributes.addFlashAttribute("msg", "cancelsuccess");
            return "redirect:/account/menu";
        }

    }
    
    @RequestMapping(value = "/checkout/{orderNumber}/complete", method = RequestMethod.GET)
    public String completeOrder(@PathVariable String orderNumber, Model model, HttpServletRequest request, RedirectAttributes attributes) throws ServiceException {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        Customer customer = CustomerState.getCustomer();
        if(customer == null 
        		|| customer.isAnonymous() 
        		|| order == null
        		|| order.getCustomer() == null
        		|| order.getCustomer().getId() == null
        		|| !customer.getId().equals(order.getCustomer().getId())){
        	return "redirect:/account/orders";
        } else {
            try {
                checkoutService.performComplete(order);
            } catch (CheckoutException e) {
                LOG.error(e.getMessage(), e);
                attributes.addFlashAttribute("msg", "completefail");
            }
            model.addAttribute("order", order);
            attributes.addFlashAttribute("msg", "completesuccess");
            return "redirect:/account/orders/" + orderNumber;
        }
    }
    
    @RequestMapping(value = "/checkout/{orderNumber}/items", method = RequestMethod.GET)
    public String orderItems(@PathVariable String orderNumber, Model model, HttpServletRequest request) throws ServiceException {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        model.addAttribute("order", order);
        return "checkout/items";
    }
    
    @RequestMapping(value = "/checkout/items", method = RequestMethod.GET)
    public String cartItems(Model model) throws ServiceException {
        Order order = CartState.getCart();
        model.addAttribute("order", order);
        return "checkout/items";
    }

    @RequestMapping(value = "/checkout/savedetails", method = RequestMethod.POST)
    public String saveGlobalOrderDetails(HttpServletRequest request, Model model,
            @ModelAttribute("shippingInfoForm") ShippingInfoForm shippingForm,
            @ModelAttribute("billingInfoForm") BillingInfoForm billingForm,
            @ModelAttribute("giftCardInfoForm") GiftCardInfoForm giftCardInfoForm,
            @ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm, BindingResult result) throws ServiceException {
        return super.saveGlobalOrderDetails(request, model, orderInfoForm, result);
    }

    @RequestMapping(value = "/pay/complete/{orderNumber}", method = RequestMethod.GET)
    public String completePay(@PathVariable(value = "orderNumber") String orderNumber) {
        try {
            Order order = orderService.findOrderByOrderNumber(orderNumber);
            if (OrderStatus.SUBMITTED.equals(order.getStatus()) && order.getTotal().isZero()) {
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

                checkoutService.performConfirm(order);
            } else {
                LOG.error("Total of order " + orderNumber + " is not zero, could not be confirmed");
                return "redirect:/failure/" + orderNumber;
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "redirect:/failure/" + orderNumber;
        }
        return "redirect:/confirmation/" + orderNumber;
    }

    @RequestMapping(value = "/checkout/complete", method = RequestMethod.POST)
    public String processCompleteCheckoutOrderFinalized(RedirectAttributes redirectAttributes) throws PaymentException {
        return super.processCompleteCheckoutOrderFinalized(redirectAttributes);
    }

    @RequestMapping(value = "/checkout/cod/complete", method = RequestMethod.POST)
    public String processPassthroughCheckout(RedirectAttributes redirectAttributes)
            throws PaymentException, PricingException {
        return super.processPassthroughCheckout(redirectAttributes, PaymentType.COD);
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
    }
    
}

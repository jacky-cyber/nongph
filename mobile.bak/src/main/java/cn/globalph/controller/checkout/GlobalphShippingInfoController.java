package cn.globalph.controller.checkout;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderAddress;
import cn.globalph.b2c.order.domain.OrderAddressImpl;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.web.checkout.model.OrderMultishipOptionForm;
import cn.globalph.b2c.web.checkout.model.ShippingInfoForm;
import cn.globalph.b2c.web.controller.checkout.ShippingInfoController;
import cn.globalph.b2c.web.order.CartState;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.passport.domain.Address;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class GlobalphShippingInfoController extends ShippingInfoController {
	@Resource(name = "blOrderService")
	private OrderService orderService;
	
    @RequestMapping(value = "/checkout/orderAddress", method = RequestMethod.GET)
    public String selectOrderAddress(Long addressId, HttpServletRequest request) {
    	Order cart = null;
    	Long oid = (Long)request.getSession().getAttribute("oid");
    	if(oid != null){
    		cart = orderService.getOrderById(oid);
    	}
    	if(cart == null){
    		cart = CartState.getCart();
    		oid = null;
    	}
        OrderAddress orderAddress = cart.getOrderAddress();
        Address selectedAddress = addressService.readAddressById(addressId);
        if(orderAddress == null){ 	
        	if(selectedAddress != null){
        		 orderAddress = new OrderAddressImpl();
        		 orderAddress.setAddress(selectedAddress.getAddress());
        		 orderAddress.setAddress(selectedAddress.getAddress());
                 orderAddress.setCity(selectedAddress.getCity());
                 orderAddress.setDistrict(selectedAddress.getDistrict());
                orderAddress.setCommunity(selectedAddress.getCommunity());
                 orderAddress.setOrder(cart);
                 orderAddress.setPhone(selectedAddress.getPhone());
                 orderAddress.setProvince(selectedAddress.getProvince());
                 orderAddress.setPostalCode(selectedAddress.getPostalCode());
                 orderAddress.setReceiver(selectedAddress.getReceiver());
                 orderAddressService.save(orderAddress);
        	}
        }else{
    		orderAddress.setAddress(selectedAddress.getAddress());
   		 	orderAddress.setAddress(selectedAddress.getAddress());
            orderAddress.setCity(selectedAddress.getCity());
            orderAddress.setDistrict(selectedAddress.getDistrict());
            orderAddress.setCommunity(selectedAddress.getCommunity());
            orderAddress.setPhone(selectedAddress.getPhone());
            orderAddress.setProvince(selectedAddress.getProvince());
            orderAddress.setPostalCode(selectedAddress.getPostalCode());
            orderAddress.setReceiver(selectedAddress.getReceiver());
            orderAddressService.save(orderAddress);
    	}
        if(oid != null){
        	 return "redirect:/checkout?oid=" + oid;
        }else{
        	 return "redirect:/checkout";
        }
       
    }

    @RequestMapping(value="/checkout/singleship", method = RequestMethod.GET)
    public String convertToSingleship(HttpServletRequest request, HttpServletResponse response, Model model) throws PricingException {
        return super.convertToSingleship(request, response, model);
    }

    @RequestMapping(value="/checkout/singleship", method = RequestMethod.POST)
    public String saveSingleShip(HttpServletRequest request, HttpServletResponse response, Model model,
                                 @ModelAttribute("shippingInfoForm") ShippingInfoForm shippingForm,
                                 BindingResult result)
            throws PricingException, ServiceException {
        return super.saveSingleShip(request, response, model, shippingForm, result);
    }

    @RequestMapping(value = "/checkout/multiship", method = RequestMethod.GET)
    public String showMultiship(HttpServletRequest request, HttpServletResponse response, Model model,
                                @ModelAttribute("orderMultishipOptionForm") OrderMultishipOptionForm orderMultishipOptionForm,
                                BindingResult result) throws PricingException {
        return super.showMultiship(request, response, model);
    }
     
    @RequestMapping(value = "/checkout/select-address/{customerAddressId}", method = RequestMethod.GET)
    public String viewCustomerAddress(HttpServletRequest request, Model model,  @ModelAttribute("shippingInfoForm") ShippingInfoForm shippingForm, @PathVariable("customerAddressId") Long customerAddressId) {
        return super.viewCustomerAddress(request, model,shippingForm,customerAddressId);
    }
    
    @RequestMapping(value = "/checkout/select-address", method = RequestMethod.GET)
    public String viewCustomerAddress(HttpServletRequest request, Model model,  @ModelAttribute("shippingInfoForm") ShippingInfoForm shippingForm) {
        return super.viewCustomerAddress(request, model,shippingForm);
    }
    
    @RequestMapping(value = "/checkout/add-address", method = RequestMethod.GET)
    public String showMultishipAddAddress(HttpServletRequest request, HttpServletResponse response, Model model,
                                          @ModelAttribute("addressForm") ShippingInfoForm addressForm, BindingResult result) {
        return super.showMultishipAddAddress(request, response, model);
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
    }
}
package cn.globalph.controller.checkout;

import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.web.checkout.model.OrderMultishipOptionForm;
import cn.globalph.b2c.web.checkout.model.ShippingInfoForm;
import cn.globalph.b2c.web.controller.checkout.ShippingInfoController;
import cn.globalph.common.exception.ServiceException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class GlobalphShippingInfoController extends ShippingInfoController {

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
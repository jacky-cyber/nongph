package cn.globalph.controller.checkout;

import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.web.checkout.model.BillingInfoForm;
import cn.globalph.b2c.web.checkout.model.CustomerCreditInfoForm;
import cn.globalph.b2c.web.checkout.model.GiftCardInfoForm;
import cn.globalph.b2c.web.checkout.model.OrderInfoForm;
import cn.globalph.b2c.web.checkout.model.ShippingInfoForm;
import cn.globalph.b2c.web.controller.checkout.CheckoutController;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.payment.PaymentType;
import cn.globalph.common.vendor.service.exception.PaymentException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class GlobalphCheckoutController extends CheckoutController {

    @RequestMapping("/checkout")
    public String checkout(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm,
            @ModelAttribute("shippingInfoForm") ShippingInfoForm shippingForm,
            @ModelAttribute("billingInfoForm") BillingInfoForm billingForm,
            @ModelAttribute("giftCardInfoForm") GiftCardInfoForm giftCardInfoForm,
            @ModelAttribute("customerCreditInfoForm") CustomerCreditInfoForm customerCreditInfoForm,
            RedirectAttributes redirectAttributes) {
        return super.checkout(request, response, model, redirectAttributes,shippingForm);
    }

    @RequestMapping(value = "/checkout/savedetails", method = RequestMethod.POST)
    public String saveGlobalOrderDetails(HttpServletRequest request, Model model,
            @ModelAttribute("shippingInfoForm") ShippingInfoForm shippingForm,
            @ModelAttribute("billingInfoForm") BillingInfoForm billingForm,
            @ModelAttribute("giftCardInfoForm") GiftCardInfoForm giftCardInfoForm,
            @ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm, BindingResult result) throws ServiceException {
        return super.saveGlobalOrderDetails(request, model, orderInfoForm, result);
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

package cn.globalph.controller.checkout;

import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.web.checkout.model.BillingInfoForm;
import cn.globalph.b2c.web.checkout.model.GiftCardInfoForm;
import cn.globalph.b2c.web.checkout.model.OrderInfoForm;
import cn.globalph.b2c.web.checkout.model.ShippingInfoForm;
import cn.globalph.b2c.web.controller.checkout.BroadleafBillingInfoController;
import cn.globalph.common.exception.ServiceException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class BillingInfoController extends BroadleafBillingInfoController {

    @RequestMapping(value="/checkout/billing", method = RequestMethod.POST)
    public String saveBillingAddress(HttpServletRequest request, HttpServletResponse response, Model model,
                                     @ModelAttribute("orderInfoForm") OrderInfoForm orderInfoForm,
                                     @ModelAttribute("shippingInfoForm") ShippingInfoForm shippingForm,
                                     @ModelAttribute("giftCardInfoForm") GiftCardInfoForm giftCardInfoForm,
                                     @ModelAttribute("billingInfoForm") BillingInfoForm billingForm,
                                     BindingResult result)
            throws PricingException, ServiceException {
        return super.saveBillingAddress(request, response, model, billingForm, result);
    }

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
    }

}

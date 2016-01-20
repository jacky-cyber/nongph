package cn.globalph.controller.account;

import cn.global.passport.web.controller.BasicRegisterController;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.passport.web.core.form.RegisterCustomerForm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The controller responsible for registering a customer
 */
@Controller
@RequestMapping("/register")
public class RegisterController extends BasicRegisterController {
    
    @RequestMapping(method=RequestMethod.GET)
    public String register(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute("registrationForm") RegisterCustomerForm registerCustomerForm) {
        return super.register(registerCustomerForm, request, response, model);
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public String processRegister(HttpServletRequest request, HttpServletResponse response, Model model,
            @ModelAttribute("registrationForm") RegisterCustomerForm registerCustomerForm, BindingResult errors) throws ServiceException, PricingException {
        return super.processRegister(registerCustomerForm, errors, request, response, model);
    }
    
    @ModelAttribute("registrationForm") 
    public RegisterCustomerForm initCustomerRegistrationForm() {
        return super.initCustomerRegistrationForm();        
    }
}

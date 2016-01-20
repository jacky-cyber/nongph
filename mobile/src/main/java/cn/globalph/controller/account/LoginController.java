package cn.globalph.controller.account;

import cn.global.passport.web.controller.BasicLoginController;
import cn.global.passport.web.controller.ResetPasswordForm;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.service.GenericResponse;
import cn.globalph.passport.domain.Customer;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The controller responsible for all actions involving logging a customer in
 */
@Controller
public class LoginController extends BasicLoginController {
    
    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        return super.login(request, response, model);
    }
    
    @RequestMapping(value="/login/forgotPassword", method=RequestMethod.GET)
    public String forgotPassword(HttpServletRequest request, HttpServletResponse response, Model model) {
        return super.forgotPassword(request, response, model);
    }
    
    @RequestMapping(value="/login/forgotPassword", method=RequestMethod.POST)
    public String processForgotPassword(@RequestParam("emailAddress") String emailAddress, HttpServletRequest request, Model model) {
        return super.processForgotPassword(emailAddress, request, model);
    }   

    @RequestMapping(value="/login/resetPassword", method=RequestMethod.GET)
    public String resetPassword(HttpServletRequest request, HttpServletResponse response, Model model) {
        return super.resetPassword(request, response, model);
    }   
    
    @RequestMapping(value="/login/resetPassword", method=RequestMethod.POST)
    public String processResetPassword(@ModelAttribute("resetPasswordForm") ResetPasswordForm resetPasswordForm, HttpServletRequest request, HttpServletResponse response, Model model, BindingResult errors) throws ServiceException {
        return super.processResetPassword(resetPasswordForm, request, response, model, errors);
    }   
    
    @RequestMapping(value="/login/resetPasswordByPhone", method=RequestMethod.POST)
    public String processResetPasswordByPhone(@ModelAttribute("resetPasswordForm") ResetPasswordForm resetPasswordForm, HttpServletRequest request, HttpServletResponse response, Model model, BindingResult errors) throws ServiceException {
        return super.processResetPasswordByPhone(resetPasswordForm, request, response, model, errors);
    }   
    
    @RequestMapping(value="/emailValidation/{customerId}",method=RequestMethod.GET)
    public String emailValidation(@PathVariable Long customerId, HttpServletRequest request, 
    		HttpServletResponse response, Model model){
        String token = (String)request.getParameter("token");
        if(StringUtils.isNotEmpty(token)){
        	Customer customer = customerService.getCustomerById(customerId);
        	if(customer == null){
        		model.addAttribute("message", "无效操作");
        	}else{
        		if(token.equals(customer.getEmailToken())){
        			if(customer.getValidationStatus()==2){
        				customer.setValidationStatus(3);
        				customerService.saveCustomer(customer);
        				model.addAttribute("message", "邮箱认证成功");
        			}else if(customer.getValidationStatus()==0){
        				customer.setValidationStatus(1);
        				customerService.saveCustomer(customer);
        				model.addAttribute("message", "邮箱认证成功");
        			}else{
        				model.addAttribute("message", "邮箱已认证");
        			}
        		}else{
        			model.addAttribute("message", "无效操作");
        		}
        	}
        	return "account/emailValidationResult";
        }else{
	    	GenericResponse errorResponse = customerService.sendEmailValidationNotification(customerId, getEmailValidationUrl(request,customerId));
	        if (errorResponse.getHasErrors()) {
	             String errorCode = errorResponse.getErrorCodesList().get(0);
	             model.addAttribute("errorCode", errorCode);             
	        } 
	        return "account/emailValidation";
        }
    }
    
    @Override
    public String getResetPasswordUrl(HttpServletRequest request) {     
        String url = request.getScheme() + "://" + request.getServerName() + getResetPasswordPort(request, request.getScheme() + "/");
        
        if (request.getContextPath() != null && ! "".equals(request.getContextPath())) {
            url = url + request.getContextPath() + "/login/resetPassword";
        } else {
            url = url + "/login/resetPassword";
        }
        return url;
    }
    
    public String getEmailValidationUrl(HttpServletRequest request,Long customerId) {     
        String url = request.getScheme() + "://" + request.getServerName() + getResetPasswordPort(request, request.getScheme() + "/");
        
        if (request.getContextPath() != null && ! "".equals(request.getContextPath())) {
            url = url + request.getContextPath() + "/emailValidation/" + customerId;
        } else {
            url = url + "/emailValidation/" + customerId;
        }
        return url;
    }
}

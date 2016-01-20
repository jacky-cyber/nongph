package cn.global.passport.web.controller;

import cn.global.passport.web.controller.validator.UpdateAccountValidator;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.passport.web.core.CustomerState;
import cn.globalph.web.util.WebValidation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class UpdateAccountController extends BasicController {

    @Resource(name = "blCustomerService")
    protected CustomerService customerService;
    
    @Resource(name="blOrderService")
    protected OrderService orderService;
    
    @Resource(name = "blUpdateAccountValidator")
    protected UpdateAccountValidator updateAccountValidator;

    protected String accountUpdatedMessage = "账号信息修改成功";
    
    protected static String updateAccountView = "account/updateAccount";
    protected static String accountRedirectView = "redirect:/account";

    public String viewUpdateAccount(HttpServletRequest request, Model model, UpdateAccountForm form) {
        Customer customer = CustomerState.getCustomer();
        form.setEmailAddress(customer.getEmailAddress());
        form.setName(customer.getName());
        form.setPhone(customer.getPhone());
        form.setCustomerId(customer.getId());
        form.setValidationStatus(customer.getValidationStatus());
        return getUpdateAccountView();
    }

    public String processUpdateAccount(HttpServletRequest request, Model model, UpdateAccountForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        updateAccountValidator.validate(form, result);
        if (result.hasErrors()) {
            return getUpdateAccountView();
        }
        Customer customer = CustomerState.getCustomer();
//        //更改邮箱
//        if(!form.getEmailAddress().equals(customer.getEmailAddress())){
//        	//新邮箱默认为未认证
//        	if(customer.getValidationStatus()==1){
//        		customer.setValidationStatus(0);
//        	}else
//        	if(customer.getValidationStatus()==3){
//        		customer.setValidationStatus(2);
//        	}
//        	customer.setEmailAddress(form.getEmailAddress());
//        }else{
//        //之前无邮箱或未更改邮箱
//        customer.setEmailAddress(form.getEmailAddress());}
//        
//        //更改手机号码
//        if(!customer.getPhone().equals(form.getPhone())){
//        	//新手机号码默认为未认证
//        	if(customer.getValidationStatus()==2){
//        		customer.setValidationStatus(0);
//        	}else
//        	if(customer.getValidationStatus()==3){
//        		customer.setValidationStatus(1);
//        	}
//         	if(!WebValidation.isPhone(form.getPhone())){
//        		result.rejectValue("phone", "phone.invalid");
//        		return getUpdateAccountView();
//        	}else{
//        		customer.setPhone(form.getPhone());
//        	}
//        }else{
//        //之前无手机号码或未更改手机号码
//        	if(!WebValidation.isPhone(form.getPhone())){
//        		result.rejectValue("phone", "phone.invalid");
//        		return getUpdateAccountView();
//        	}else{
//        		customer.setPhone(form.getPhone());
//        	}
//        }
        
        customer.setName(form.getName());
        
        customerService.saveCustomer(customer);
        redirectAttributes.addFlashAttribute("successMessage", getAccountUpdatedMessage());
        return getAccountRedirectView();
    }

    public String getUpdateAccountView() {
        return updateAccountView;
    }
    
    public String getAccountRedirectView() {
        return accountRedirectView;
    }
    
    public String getAccountUpdatedMessage() {
        return accountUpdatedMessage;
    }

}

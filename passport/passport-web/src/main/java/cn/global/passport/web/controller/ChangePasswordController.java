package cn.global.passport.web.controller;

import cn.global.passport.web.controller.validator.ChangePasswordValidator;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.security.util.PasswordChange;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.passport.web.core.CustomerState;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This controller handles password changes for a customer's account
 */
public class ChangePasswordController extends BasicController {

    @Resource(name = "blCustomerService")
    protected CustomerService customerService;
    @Resource(name = "blChangePasswordValidator")
    protected ChangePasswordValidator changePasswordValidator;

    protected String passwordChangedMessage = "密码修改成功";
    
    protected static String changePasswordView = "account/changePassword";
    protected static String changePasswordRedirect = "redirect:/account/password";

    public String viewChangePassword(HttpServletRequest request, Model model) {
        return getChangePasswordView();
    }

    public String processChangePassword(HttpServletRequest request, Model model, ChangePasswordForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        PasswordChange passwordChange = new PasswordChange(CustomerState.getCustomer().getLoginName());
        passwordChange.setCurrentPassword(form.getCurrentPassword());
        passwordChange.setNewPassword(form.getNewPassword());
        passwordChange.setNewPasswordConfirm(form.getNewPasswordConfirm());
        changePasswordValidator.validate(passwordChange, result);
        if (result.hasErrors()) {
            return getChangePasswordView();
        }
        customerService.changePassword(passwordChange);
        redirectAttributes.addFlashAttribute("successMessage", getPasswordChangedMessage());
        return "redirect:/account/menu";
    }

    public String getChangePasswordView() {
        return changePasswordView;
    }
    
    public String getChangePasswordRedirect() {
        return changePasswordRedirect;
    }
    
    public String getPasswordChangedMessage() {
        return passwordChangedMessage;
    }
    
}

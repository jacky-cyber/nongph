package cn.globalph.controller.account;

import cn.global.passport.web.controller.ChangePasswordForm;
import cn.globalph.common.exception.ServiceException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/account/password")
public class ChangePasswordController extends cn.global.passport.web.controller.ChangePasswordController {

    @RequestMapping(method = RequestMethod.GET)
    public String viewChangePassword(HttpServletRequest request, Model model, @ModelAttribute("changePasswordForm") ChangePasswordForm form) {
        return super.viewChangePassword(request, model);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processChangePassword(HttpServletRequest request, Model model, @ModelAttribute("changePasswordForm") ChangePasswordForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        return super.processChangePassword(request, model, form, result, redirectAttributes);
    }
    
}

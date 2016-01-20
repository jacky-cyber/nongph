package cn.globalph.controller.account;

import java.util.List;

import cn.global.passport.web.controller.UpdateAccountForm;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.coupon.service.CustomerCouponService;
import cn.globalph.passport.web.core.CustomerState;
import cn.globalph.passport.domain.CustomerMessage;
import cn.globalph.passport.domain.WechatCustomer;
import cn.globalph.passport.service.CustomerMessageService;
import cn.globalph.passport.service.CustomerUserDetails;
import cn.globalph.passport.service.WechatCustomerService;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/account")
public class UpdateAccountController extends cn.global.passport.web.controller.UpdateAccountController {

    @Resource(name = "phWechatCustomerService")
    private WechatCustomerService wechatCustomerService;
    
    @Resource(name = "phCustomerCouponService")
    private CustomerCouponService customerCouponService;
    
    @Resource(name = "phCustomerMessageService")
    private CustomerMessageService customerMessageService; 
    
    @RequestMapping(method = RequestMethod.GET)
    public String viewUpdateAccount(HttpServletRequest request, Model model, @ModelAttribute("updateAccountForm") UpdateAccountForm form) {
        return super.viewUpdateAccount(request, model, form);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processUpdateAccount(HttpServletRequest request, Model model, @ModelAttribute("updateAccountForm") UpdateAccountForm form, BindingResult result, RedirectAttributes redirectAttributes) throws ServiceException {
        return super.processUpdateAccount(request, model, form, result, redirectAttributes);
    }

    @RequestMapping(value="/menu",method = RequestMethod.GET)
    public String viewAccountMenu(HttpServletRequest request, Model model) {
    	if(CustomerState.getCustomer() == null || CustomerState.getCustomer().isAnonymous()){
    		return "redirect:/";
    	}
    	
    	List<Order> wait4commentOrders = orderService.findNoCommentOrdersForCustomer(CustomerState.getCustomer());
    	int wait4comment = wait4commentOrders != null ? wait4commentOrders.size() : 0;

        List<Order> wait4receiveOrders = orderService.findOrdersForCustomer(CustomerState.getCustomer(), OrderStatus.CONFIRMED);
        int wait4receive = wait4receiveOrders != null ? wait4receiveOrders.size() : 0;

        List<Order> wait4payOrders = orderService.findOrdersForCustomer(CustomerState.getCustomer(), OrderStatus.SUBMITTED);
        int wait4pay = wait4payOrders != null ? wait4payOrders.size() : 0;
        
        model.addAttribute("wait4payCount", wait4pay);
        model.addAttribute("wait4receiveCount", wait4receive);
        model.addAttribute("wait4commentCount", wait4comment);
        model.addAttribute("activeCouponCount", customerCouponService.findActiveCustomerCouponCount(CustomerState.getCustomer().getId()));
        model.addAttribute("activeMessageCount", customerMessageService.findActiveMessageByCustomerId(CustomerState.getCustomer().getId()));
        return "account/menu";
    }

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String viewAccountManage(HttpServletRequest request, Model model) {
        CustomerUserDetails userDetails = (CustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<WechatCustomer> currWechatCustomerList = wechatCustomerService.readWechatCustomersByCustomerId(userDetails.getId());
        return "account/manage";

    }
    
    @RequestMapping(value="/messages",method = RequestMethod.GET)
    public String viewMessage(HttpServletRequest request, Model model) {
    	if(CustomerState.getCustomer() == null || CustomerState.getCustomer().isAnonymous()){
    		return "redirect:/";
    	}
    	List<CustomerMessage> customerMessages = customerMessageService.findMessagesByCustomerId(CustomerState.getCustomer().getId());
   
    	model.addAttribute("customerMessages", customerMessages);
    	model.addAttribute("newMessagesCount", customerMessageService.findActiveMessageByCustomerId(CustomerState.getCustomer().getId()));
    	customerMessageService.deativeMessagesByCustomerId(CustomerState.getCustomer().getId());
        return "account/messages";
    }
}

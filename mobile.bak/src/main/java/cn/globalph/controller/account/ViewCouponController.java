package cn.globalph.controller.account;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.globalph.coupon.domain.Coupon;
import cn.globalph.coupon.domain.CustomerCoupon;
import cn.globalph.coupon.service.CustomerCouponService;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;

@Controller
@RequestMapping("/account")
public class ViewCouponController {
	@Resource(name = "phCustomerCouponService")
	private CustomerCouponService customerCouponService;
	
	@RequestMapping(value = "/coupon", method = RequestMethod.GET)
	public String showCoupon(Model model){
		Customer customer = CustomerState.getCustomer();
		List<CustomerCoupon> customerCoupons = customerCouponService.findActiveCouponByCustomerId(customer.getId());
		model.addAttribute("customerCoupons", customerCoupons);
		return "account/myCoupons";
	}
}

package cn.globalph.controller.account;

import cn.globalph.common.sms.SMSUtil;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.web.util.WebValidation;
import com.twelvemonkeys.lang.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sms")
public class SMSController {
	@Resource(name = "blCustomerService")
	protected CustomerService customerService;

	// 进入手机认证页面
	@RequestMapping(value = "/{phone}", method = RequestMethod.GET)
	public String viewPhoneValidation(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("phone") String phone,RedirectAttributes redirectAttributes,Model model) {
		// 系统内查找拥有此手机号码的用户，如果为空，则此号码尚未在系统内注册使用
		Customer customer = customerService.readCustomerByPhone(phone);
		if(customer == null){
			redirectAttributes.addFlashAttribute("errorMessage", "系统内不存在此手机号");
			return "redirect:/account";
		}
		model.addAttribute("newPhone", phone);
		model.addAttribute("oldPhone",phone);
		model.addAttribute("validationCode", "");
		return "account/phoneValidation";
		
	}

	//重新绑定手机号码
	@RequestMapping(value = "/resetPhone/{oldPhone}/{newPhone}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> resetPhone(HttpServletRequest reqeust, HttpServletResponse response,
			@PathVariable("oldPhone") String oldPhone, @PathVariable("newPhone") String newPhone){
		if(!WebValidation.isPhone(oldPhone)){
			Map map = new HashMap<String,Object>();
			map.put("error", "旧手机号码格式不正确");
			return map;
		}
		if(!WebValidation.isPhone(newPhone)){
			Map map = new HashMap<String,Object>();
			map.put("error", "新手机号码格式不正确");
			return map;
		}
		if(newPhone.equals(oldPhone)){
			Customer customer = customerService.readCustomerByPhone(newPhone);
			if(customer == null){
				Map map = new HashMap<String, Object>();
				map.put("error", "手机号码系统内不存在");
				return map;
			}
			if(customer.getValidationStatus() == 2 || customer.getValidationStatus() == 3){
				Map map = new HashMap<String, Object>();
				map.put("error", "此手机号码已认证");
				return map;
			}
			String validationCode = SMSUtil.generateValidationCode();
			customer.setValidationCode(validationCode);
			customerService.saveCustomer(customer);
			return SMSUtil.sendValidationCode(newPhone, validationCode);
		}else{
			Customer customer = customerService.readCustomerByPhone(newPhone);
			if(customer != null){
				Map map = new HashMap<String, Object>();
				map.put("error", "新手机号码系统内已存在");
				return map;
			}
			customer = customerService.readCustomerByPhone(oldPhone);
			if(customer == null){
				Map map = new HashMap<String, Object>();
				map.put("error", "旧手机号码系统内不存在");
				return map;
			}
			if(customer.getValidationStatus() == 2 || customer.getValidationStatus() ==3){
				customer.setValidationStatus(customer.getValidationStatus() - 2);
			}
			String validationCode = SMSUtil.generateValidationCode();
			customer.setValidationCode(validationCode);
			customer.setPhone(newPhone);
			customerService.saveCustomer(customer);
			return SMSUtil.sendValidationCode(newPhone, validationCode);
		}
	}
	
	//重新绑定手机号码
	@RequestMapping(value = "/resetPhone", method = RequestMethod.POST)
	public String resetPhone(HttpServletRequest reqeust, HttpServletResponse response,
			@ModelAttribute("oldPhone") String oldPhone, @ModelAttribute("newPhone") String newPhone, 
			@ModelAttribute("validationCode") String validationCode, Model model){
		if(StringUtil.isEmpty(validationCode)){
			model.addAttribute("errorInfo", "验证码不能为空");
			return "account/phoneValidation";
		}
		if(!WebValidation.isPhone(oldPhone)){
			model.addAttribute("errorInfo", "旧手机号码格式不正确");
			return "account/phoneValidation";
		}
		if(!WebValidation.isPhone(newPhone)){
			model.addAttribute("errorInfo", "新手机号码格式不正确");
			return "account/phoneValidation";
		}
		if(oldPhone.equals(newPhone)){
			Customer customer = customerService.readCustomerByPhone(newPhone);
			if(customer == null){
				model.addAttribute("errorInfo", "此手机号码系统内不存在");
				return "account/phoneValidation";
			}
			if(customer.getValidationStatus() == 2 || customer.getValidationStatus() == 3){
				model.addAttribute("errorInfo", "手机号码已认证");
				return "account/phoneValidation";
			}
			if(validationCode.equals(customer.getValidationCode())){
				customer.setValidationStatus(customer.getValidationStatus() + 2);
				customerService.saveCustomer(customer);
				return "redirect:/account";
			}else{
				model.addAttribute("errorInfo", "验证码不正确");
				return "account/phoneValidation";
			}
		}else{
			Customer customer = customerService.readCustomerByPhone(newPhone);
			if(customer == null){
				model.addAttribute("errorInfo", "此手机号码系统内不存在");
				return "account/phoneValidation";
			}
			if(customer.getValidationStatus() == 2 || customer.getValidationStatus() == 3){
				model.addAttribute("errorInfo", "手机号码已认证");
				return "account/phoneValidation";
			}
			if(validationCode.equals(customer.getValidationCode())){
				customer.setValidationStatus(customer.getValidationStatus() + 2);
				customerService.saveCustomer(customer);
				return "redirect:/account";
			}else{
				model.addAttribute("errorInfo", "验证码不正确");
				return "account/phoneValidation";
			}
		}
	}
	
	// 发送手机短信验证码
	@RequestMapping(value = "/{phone}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Map<String, Object> sendMsgVC(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("phone") String phone) {
		// 系统内查找拥有此手机号码的用户，如果为空，则此号码尚未在系统内注册使用
		Customer customer = customerService.readCustomerByPhone(phone);
		// 手机号码注册
		// 此手机号码尚未在系统内注册绑定
		if (customer == null) {
			// 创建用户，存放手机验证码
			customer = customerService.createNewCustomer();
			customer.setPhone(phone);
			String validationCode = SMSUtil.generateValidationCode();
			customer.setValidationCode(validationCode);
			customerService.saveCustomer(customer, false);
			// 发送手机验证码
			return SMSUtil.sendValidationCode(phone, validationCode);
		}
		// 此手机号尚未在系统内注册成功，此前已发送验证码，需重新发送验证码
		if (!customer.isRegistered()) {
			String validationCode = SMSUtil.generateValidationCode();
			customer.setValidationCode(validationCode);
			customerService.saveCustomer(customer, false);
			// 发送手机验证码
			return SMSUtil.sendValidationCode(phone, validationCode);
		}
		// 手机号码认证//
		// 0:手机号码和邮箱都尚未认证
		// 1:邮箱已认证
		// 2:手机号码已认证
		// 3:邮箱与手机号码都已认证
		// 手机号码尚未认证，但此前已发送了验证码
		if (customer.getValidationStatus() == null
				|| customer.getValidationStatus() == 0
				|| customer.getValidationStatus() == 1) {
			String validationCode = SMSUtil.generateValidationCode();
			customer.setValidationCode(validationCode);
			customerService.saveCustomer(customer, true);
			// 发送手机验证码
			return SMSUtil.sendValidationCode(phone, validationCode);
		}
		// 手机号码已认证
		if (customer.getValidationStatus() == 2
				|| customer.getValidationStatus() == 3) {
			Map map = new HashMap<String, Object>();
			map.put("error", "手机号码已使用且已认证");
			return map;
		}

		// 系统设计不应该走到这里。。
		// 返回map,会自动转为json
		Map map = new HashMap<String, Object>();
		map.put("error", "系统内部逻辑错误");
		return map;
	}

	// 发送手机短信验证码
	@RequestMapping(value = "forgetPassword/{phone}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Map<String, Object> forgetPassword(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("phone") String phone) {
		Customer customer = customerService.readCustomerByPhone(phone);
		if(customer == null){
			Map map = new HashMap<String, Object>();
			map.put("error", "此手机号码系统内不存在");
			return map;
		}
		String validationCode = SMSUtil.generateValidationCode();
		customer.setValidationCode(validationCode);
		customerService.saveCustomer(customer);
		return SMSUtil.sendValidationCode(phone, validationCode);
	}
	
	@RequestMapping(value = "/{phone}/{validationCode}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody Map<String, Object> validatePhone(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("phone") String phone,
			@PathVariable("validationCode") String validationCode) {
		// 返回map,会自动转为json
		Map map = new HashMap<String, Object>();
		// 系统内查找拥有此手机号码的用户，不为空
		Customer customer = customerService.readCustomerByPhone(phone);
		// 为空不应该。。
		if (customer == null) {
			map.put("error", "系统内部逻辑错误");
			return map;
		}
		// 此手机号码已认证
		if (customer.getValidationStatus() == 2
				|| customer.getValidationStatus() == 3) {
			map.put("error", "手机号码已认证");
			return map;
		}
		// 短信验证码尚未发送
		if (StringUtils.isEmpty(customer.getValidationCode())) {
			map.put("error", "短信验证码尚未发送");
			return map;
		}
		// 短信验证码不能为空
		if (StringUtils.isEmpty(validationCode)) {
			map.put("error", "短信验证码不能为空");
			return map;
		}
		if (!customer.getValidationCode().equals(validationCode)) {
			map.put("error", "短信验证码不正确");
			return map;
		}
		// 短信验证成功
		customer.setValidationStatus(customer.getValidationStatus() + 2);
		customerService.saveCustomer(customer);
		map.put("success", "手机认证成功");
		return map;
	}
}
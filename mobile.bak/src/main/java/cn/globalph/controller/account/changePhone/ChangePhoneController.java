package cn.globalph.controller.account.changePhone;

import cn.globalph.common.sms.SMSUtil;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.passport.web.core.CustomerState;
import cn.globalph.web.util.WebValidation;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/account/changePhone")
public class ChangePhoneController {
	private static final Log LOG = LogFactory.getLog(ChangePhoneController.class);
	private static String CACHE_NAME = "phPhoneValicationCodeElements";
	
	@Resource(name = "blCustomerService")
	private CustomerService customerService;
	
	@RequestMapping(value = "/step1", method = RequestMethod.GET)
	public String toStep1(HttpServletRequest request, HttpServletResponse reponse, Model model){
		Customer customer = CustomerState.getCustomer();
		if (customer == null || customer.isAnonymous()) {
			return "redirect:/account/menu";
		}else{
			String phone = customer.getPhone();
			if(StringUtils.isEmpty(phone)){
				LOG.error("customer's phone is null, customer id:" + customer.getId());
				return "redirect:/account/menu"; 
			}else{
				String  s = (String)request.getParameter("s");
				if(StringUtils.isNotEmpty(s)){
					Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
					String validationCode = SMSUtil.generateValidationCode();
					Element element = new Element(customer.getId() + phone, validationCode);
					cache.put(element);
					SMSUtil.sendValidationCode(phone, validationCode);
					model.addAttribute("isSend", "1");
				}
				return "account/changePhone/step1";
			}
		}
	}
	
	@RequestMapping(value = "/step2", method = RequestMethod.GET)
	public String toStep2(HttpServletRequest request, HttpServletResponse reponse, 
			@ModelAttribute(value = "validationCode") String validationCode, Model model){
		if(StringUtils.isEmpty(validationCode)){
			model.addAttribute("errorTips", "短信验证码不能为空");
			return "account/changePhone/step1";
		}
		if(!WebValidation.isValidationCode(validationCode)){
			model.addAttribute("errorTips", "短信验证码格式有误,请重新输入");
			return "account/changePhone/step1";
		}
		
		Customer customer = CustomerState.getCustomer();
		if (customer == null || customer.isAnonymous()) {
			return "redirect:/account/menu";
		}else{
			String phone = customer.getPhone();
			if(StringUtils.isEmpty(phone)){
				LOG.error("customer's phone is null, customer id:" + customer.getId());
				return "redirect:/account/menu"; 
			}else{
				Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
				Element element = cache.get(customer.getId() + phone);
				if(element == null){
					model.addAttribute("errorTips", "短信校验码错误");
					return "account/changePhone/step1";
				}else{
					String value = (String)element.getObjectValue();
					if(StringUtils.isEmpty(value) || !value.equals(validationCode)){
						model.addAttribute("errorTips", "短信校验码错误");
						return "account/changePhone/step1";
					}else{
						return "account/changePhone/step2";
					}
				}
			}
		}
	}
	
	@RequestMapping(value = "/step2/valicationCode", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> step2SendValicationCode(HttpServletRequest request, HttpServletResponse reponse, 
			@ModelAttribute(value = "newPhone") String newPhone, Model model){
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isEmpty(newPhone)){
			map.put("errorTips", "验证手机号码不能为空");
			return map;
		}
		if(!WebValidation.isPhone(newPhone)){
			map.put("errorTips", "验证手机号格式有误,请重新输入");
			return map;
		}
		
		Customer cus  = customerService.readCustomerByPhone(newPhone);
		if(cus != null){
			map.put("errorTips", "此手机号已使用");
			return map;
		}
		
		Customer customer = CustomerState.getCustomer();
		if (customer == null || customer.isAnonymous()) {
			map.put("errorTips", "未知用户");
			return map;
		}else{
			String validationCode = SMSUtil.generateValidationCode();
			Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
			Element element = new Element(customer.getId() + newPhone, validationCode);
			cache.put(element);
			SMSUtil.sendValidationCode(newPhone, validationCode);
//			Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
//			Element element = new Element(customer.getId() + newPhone, "111111");
//			cache.put(element);
			return map;
		}
	}
	
	@RequestMapping(value = "/step2", method = RequestMethod.POST)
	public String step3(HttpServletRequest request, HttpServletResponse reponse, Model model,
			@ModelAttribute(value = "code") String validationCode, @ModelAttribute(value = "newPhone") String newPhone){
		Customer customer = CustomerState.getCustomer();
		if(customer == null || customer.isAnonymous()){
			return "redirect:/account/menu";
		}else{	
			if(StringUtils.isEmpty(newPhone)){
				model.addAttribute("errorTips", "手机号码不能为空");
				return "account/changePhone/step2";
			}
			if(!WebValidation.isPhone(newPhone)){
				model.addAttribute("errorTips", "手机号码格式错误");
				return "account/changePhone/step2";
			}
			if(StringUtils.isEmpty(validationCode)){
				model.addAttribute("errorTips", "短信验证码不能为空");
				return "account/changePhone/step2";
			}
			if(!WebValidation.isValidationCode(validationCode)){
				model.addAttribute("errorTips", "短信验证码格式有误,请重新输入");
				return "account/changePhone/step2";
			}
			
			Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
			Element element = cache.get(customer.getId() + newPhone);
			if(element == null){
				model.addAttribute("errorTips", "系统异常,使用了不允许的身份校验方式");
				return "account/changePhone/step2";
			}else{
				String value = (String)element.getObjectValue();
				if(StringUtils.isEmpty(value) || !validationCode.equals(value)){
					model.addAttribute("errorTips", "短信校验码错误");
					return "account/changePhone/step2";
				}else{
					customer.setPhone(newPhone);
					customerService.saveCustomer(customer);
					return "account/changePhone/step3";
				}
			}
		}
	
	}
}

package cn.globalph.controller.account.changeEmail;

import cn.globalph.common.email.service.EmailService;
import cn.globalph.common.email.service.info.EmailInfo;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/account/changeEmail")
public class ChangeEmailController {
	private static final Log LOG = LogFactory.getLog(ChangeEmailController.class);
	private static String PHONE_CACHE = "phPhoneValicationCodeElements";
	private static String EMAIL_CACHE = "phEmailValicationCodeElements";
	
	@Resource(name="blEmailService")
	private EmailService emailService;
    @Resource(name="phEmailValidationEmailInfo")
    private EmailInfo emailValidationEmailInfo;
    
	@Resource(name = "blCustomerService")
	private CustomerService customerService;
	
	@RequestMapping(value = "/step1", method = RequestMethod.GET)
	public String toStep1(HttpServletRequest request, HttpServletResponse reponse, Model model){
		Customer customer = CustomerState.getCustomer();
		if(customer == null || customer.isAnonymous()){
			return "redirect:/account/menu";
		}else{
			return "account/changeEmail/step1";
		}
	}
	
	@RequestMapping(value = "/step1/{phone}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> validationCode(HttpServletRequest request, HttpServletResponse reponse, 
			Model model, @PathVariable(value = "phone") String phone){
		Map<String, Object> map = new HashMap<String, Object>();
		Customer customer = CustomerState.getCustomer();
		if(customer == null || customer.isAnonymous()){
			map.put("errorTips", "系统异常,使用了不允许的身份校验方式");
			return map;
		}else{
			if(StringUtils.isEmpty(phone)){
				map.put("errorTips", "验证手机号码不能为空");
				return map;
			}else if(!WebValidation.isPhone(phone)){
				map.put("errorTips", "验证手机号格式有误,请重新输入");
				return map;
			}else{
				if(!phone.equals(customer.getPhone())){
					map.put("errorTips", "系统异常，使用了无效的手机号码");
					return map;
				}else{
					String validationCode = SMSUtil.generateValidationCode();
					Cache cache = CacheManager.getInstance().getCache(PHONE_CACHE);
					Element element = new Element(customer.getId() + phone, validationCode);
					cache.put(element);
					SMSUtil.sendValidationCode(phone, validationCode);
//					Cache cache = CacheManager.getInstance().getCache(PHONE_CACHE);
//					Element element = new Element(customer.getId() + phone, "111111");
//					cache.put(element);
					return map;
				}
			}
		}
	}
	
	@RequestMapping(value = "/step2", method = RequestMethod.GET)
	public String toStep2(HttpServletRequest request, HttpServletResponse reponse, 
			Model model, @ModelAttribute(value = "validationCode") String code){
		Customer customer = CustomerState.getCustomer();
		if(customer == null || customer.isAnonymous()){
			model.addAttribute("errorTips", "系统异常,使用了不允许的身份校验方式");
			return "account/changeEmail/step1";
		}else{
			if(StringUtils.isEmpty(code)){
				model.addAttribute("errorTips", "短信验证码不能为空");
				return "account/changeEmail/step1";
			}else if(!WebValidation.isValidationCode(code)){
				model.addAttribute("errorTips", "短信验证码格式有误,请重新输入");
				return "account/changeEmail/step1";
			}else{
				Cache cache = CacheManager.getInstance().getCache(PHONE_CACHE);
				Element element = cache.get(customer.getId() + customer.getPhone());
				if(element == null || !code.equals((String)element.getObjectValue())){
					model.addAttribute("errorTips", "短信校验码错误");
					return "account/changeEmail/step1";
				}else{
					return "account/changeEmail/step2";
				}
			}
		}
	}
	
	@RequestMapping(value = "/step3", method = RequestMethod.POST)
	public String toStep3(HttpServletRequest request, HttpServletResponse reponse, 
			Model model, @ModelAttribute(value = "email") String email){
		Customer customer = CustomerState.getCustomer();
		if(customer == null || customer.isAnonymous()){
			model.addAttribute("errorTips", "系统异常,使用了不允许的身份校验方式");
			return "account/changeEmail/step2";
		}else{
			if(StringUtils.isEmpty(email)){
				model.addAttribute("errorTips", "邮箱地址不能为空");
				return "account/changeEmail/step2";
			}else if(!WebValidation.isEmail(email)){
				model.addAttribute("errorTips", "邮箱地址格式有误,请重新输入");
				return "account/changeEmail/step2";
			}else{
				Customer cus = customerService.readCustomerByEmail(email);
				if(cus != null){
					model.addAttribute("errorTips", "邮箱地址也被使用");
					return "account/changeEmail/step2";
				}
				String validationCode = SMSUtil.generateValidationCode();
				Cache cache = CacheManager.getInstance().getCache(EMAIL_CACHE);
				Element element = new Element(email, customer.getId() + validationCode);
				cache.put(element);
				
				String validationEmailUrl = getValidationEmailUrl(request) + "?email=" + email + "&code=" + validationCode;
				HashMap<String, Object> vars = new HashMap<String, Object>();
				vars.put("resetPasswordUrl", validationEmailUrl); 
	            vars.put("userName", customer.getName());
	            vars.put("submitDate", new Date(System.currentTimeMillis()));
	            try{
	            	emailService.sendTemplateEmail(email, emailValidationEmailInfo, vars);
	            	model.addAttribute("email", email);
	            	return "account/changeEmail/step3";
	            }catch(Exception e){
	            	LOG.error("send email error,email address:" + email);
	            	model.addAttribute("errorTips", "邮件发送失败，请检查邮箱地址重新输入");
	            	return "account/changeEmail/step2";
	            }
				
			}
		}
	}
	
	@RequestMapping(value = "/validate", method = RequestMethod.GET)
	public String validate(HttpServletRequest request, HttpServletResponse reponse, 
			Model model, @ModelAttribute(value = "email") String email, @ModelAttribute(value = "code") String code){
		if(StringUtils.isEmpty(email) 
				|| !WebValidation.isEmail(email)
				|| StringUtils.isEmpty(code) 
				|| !WebValidation.isValidationCode(code)){
			return "redirect:/";
		}
		Cache cache = CacheManager.getInstance().getCache(EMAIL_CACHE);
		Element element = cache.get(email);
		if(element == null || StringUtils.isEmpty((String)element.getObjectValue())){
			return "redirect:/";
		}else{
			String value = (String)element.getObjectValue();
			if(value.length() < 7){
				return "redirect:/";
			}else{
				String cid = value.substring(0,value.length() - 6);
				String cod = value.substring(value.length() - 6);
				if(!cod.equals(code)){
					return "redirect:/";
				}
				try{
					Long id = Long.parseLong(cid);
					Customer customer = customerService.getCustomerById(id);
					if(customer == null ){
						throw new Exception("can not find customer, id: " + id);
					}else{
						customer.setEmailAddress(email);
						customer.setValidationStatus(3);
						customerService.saveCustomer(customer);
						cache.remove(email);
						return "account/changeEmail/success";
					}
				}catch(Exception e){
					return "redirect:/";
				}
			}
		}
	}
	
    private String getValidationEmailUrl(HttpServletRequest request) {     
        String url = request.getScheme() + "://" + request.getServerName() + getValidationEmailPort(request, request.getScheme() + "/");
        
        if (request.getContextPath() != null && ! "".equals(request.getContextPath())) {
            url = url + request.getContextPath() + "/account/changeEmail/validate";
        } else {
            url = url + "/account/changeEmail/validate";
        }
        return url;
    }
    
    public String getValidationEmailPort(HttpServletRequest request, String scheme) {
        if ("http".equalsIgnoreCase(scheme) && request.getServerPort() != 80) {
            return ":" + request.getServerPort();
        } else if ("https".equalsIgnoreCase(scheme) && request.getServerPort() != 443) {
            return ":" + request.getServerPort();
        }
        return "";  // no port required
    }
}

package cn.global.passport.web.controller;

import cn.global.passport.web.controller.validator.RegisterCustomerByPhoneValidator;
import cn.global.passport.web.controller.validator.RegisterCustomerValidator;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.web.RequestProcessor;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.coupon.issue.event.CouponIssueEvent;
import cn.globalph.coupon.issue.event.CouponIssueEventSource;
import cn.globalph.coupon.issue.event.CouponIssueEventType;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.passport.service.LoginService;
import cn.globalph.passport.web.core.CustomerState;
import cn.globalph.passport.web.core.form.RegisterCustomerForm;
import cn.globalph.passport.web.core.security.CustomerStateRequestProcessor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * The controller responsible for registering a customer.
 * 
 * Uses a component registered with the name blCustomerValidator to perform
 * validation of the submitted customer.
 * 
 * Uses the property "useEmailForLogin" to determine if the username should be
 * defaulted to the email address if no username is supplied.
 * 
 * @author felix.wu
 */
public class BasicRegisterController extends BasicController {

	protected boolean useEmailForLogin = true;
	protected static String registerSuccessView = "ajaxredirect:/";
	protected static String registerView = "authentication/register";

	@Resource(name = "blCustomerService")
	protected CustomerService customerService;

	@Resource(name = "blRegisterCustomerValidator")
	protected RegisterCustomerValidator registerCustomerValidator;

	@Resource(name = "blRegisterCustomerByPhoneValidator")
	protected RegisterCustomerByPhoneValidator registerCustomerByPhoneValidator;

	@Resource(name = "blLoginService")
	protected LoginService loginService;

	@Resource(name = "blCustomerStateRequestProcessor")
	private CustomerStateRequestProcessor customerStateRequestProcessor;

	@Resource(name = "blCartStateRequestProcessor")
	private RequestProcessor cartStateRequestProcessor;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	public String register(RegisterCustomerForm registerCustomerForm,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String redirectUrl = request.getParameter("successUrl");
		if (StringUtils.isNotBlank(redirectUrl)) {
			registerCustomerForm.setRedirectUrl(redirectUrl);
		}
		return getRegisterView();
	}

	public String processRegister(RegisterCustomerForm registerCustomerForm,
			BindingResult errors, HttpServletRequest request,
			HttpServletResponse response, Model model) throws ServiceException {

			Customer customer = registerCustomerForm.getCustomer();
			customer.setName(customer.getEmailAddress());
			customer.setLoginName(customer.getEmailAddress());

		registerCustomerValidator.validate(registerCustomerForm, errors,
				useEmailForLogin);
		if (!errors.hasErrors()) {
			Customer newCustomer = null;
			try{
			newCustomer = customerService.registerCustomer(
					registerCustomerForm.getCustomer(),
					registerCustomerForm.getPassword(),
					registerCustomerForm.getPasswordConfirm(),true);
			}catch(Exception e){
				errors.rejectValue("customer.emailAddress", "emailAddress.invalid", null, null);
				return getRegisterView();
			}
			assert (newCustomer != null);

			// The next line needs to use the customer from the input form and
			// not the customer returned after registration
			// so that we still have the unencoded password for use by the
			// authentication mechanism.
			loginService.loginCustomer(registerCustomerForm.getCustomer());
			customerStateRequestProcessor.process(WebRequestContext
					.getWebRequestContext().getWebRequest());
			cartStateRequestProcessor.process(WebRequestContext
					.getWebRequestContext().getWebRequest());

			String redirectUrl = registerCustomerForm.getRedirectUrl();
			if (StringUtils.isNotBlank(redirectUrl)
					&& redirectUrl.contains(":")) {
				redirectUrl = null;
			}
			
			//注册成功送优惠券
            applicationContext.publishEvent(
					new CouponIssueEvent(CouponIssueEventType.REGISTER_COUPON_EVENT.getType(), false,
							new CouponIssueEventSource(CustomerState.getCustomer().getId(),
									new Date(System.currentTimeMillis())
							)
					)
			);

			return StringUtils.isBlank(redirectUrl) ? getRegisterSuccessView()
					: "redirect:" + redirectUrl;
		} else {
			return getRegisterView();
		}
	}

	public String registerByPhone(RegisterCustomerForm registerCustomerForm,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		String redirectUrl = request.getParameter("successUrl");
		if (StringUtils.isNotBlank(redirectUrl)) {
			registerCustomerForm.setRedirectUrl(redirectUrl);
		}
		return "authentication/registerByPhone";
	}

	public String processRegisterByPhone(
			RegisterCustomerForm registerCustomerForm, BindingResult errors,
			HttpServletRequest request, HttpServletResponse response,
			Model model) throws ServiceException {
		Customer customer = registerCustomerForm.getCustomer();
		if(StringUtils.isEmpty(customer.getName())){
			customer.setName("m" + customer.getPhone());
		}
		customer.setLoginName(customer.getPhone());
		
		registerCustomerByPhoneValidator.validate(registerCustomerForm, errors);
		if (!errors.hasErrors()) {
			registerCustomerForm.getCustomer().setValidationStatus(2);
			Customer newCustomer = customerService.registerCustomer(
					registerCustomerForm.getCustomer(),
					registerCustomerForm.getPassword(),
					registerCustomerForm.getPasswordConfirm(),false);
			assert (newCustomer != null);

			// The next line needs to use the customer from the input form and
			// not the customer returned after registration
			// so that we still have the unencoded password for use by the
			// authentication mechanism.
			loginService.loginCustomer(registerCustomerForm.getCustomer());
			customerStateRequestProcessor.process(WebRequestContext
					.getWebRequestContext().getWebRequest());
			cartStateRequestProcessor.process(WebRequestContext
					.getWebRequestContext().getWebRequest());

			String redirectUrl = registerCustomerForm.getRedirectUrl();
			if (StringUtils.isNotBlank(redirectUrl)
					&& redirectUrl.contains(":")) {
				redirectUrl = null;
			}
			
			//注册成功送优惠券
//			couponEventHandler.handle(CouponEventType.REGISTER_SUCCESS_EVENT, CustomerState.getCustomer().getId());
            applicationContext.publishEvent(
					new CouponIssueEvent(CouponIssueEventType.REGISTER_COUPON_EVENT.getType(), false,
							new CouponIssueEventSource(CustomerState.getCustomer().getId(),
									new Date(System.currentTimeMillis())
							)
					)
			);


			return StringUtils.isBlank(redirectUrl) ? getRegisterSuccessView()
					: "redirect:" + redirectUrl;
		} else {
			return "authentication/registerByPhone";
		}
	}

	public RegisterCustomerForm initCustomerRegistrationForm() {
		Customer customer = CustomerState.getCustomer();
		if (customer == null || !customer.isAnonymous()) {
			customer = customerService.createCustomerFromId(null);
		}
		if (!customer.isPersisted())
			customerService.saveCustomer(customer);
		RegisterCustomerForm customerRegistrationForm = new RegisterCustomerForm();
		customerRegistrationForm.setCustomer(customer);
		return customerRegistrationForm;
	}

	public boolean isUseEmailForLogin() {
		return useEmailForLogin;
	}

	public void setUseEmailForLogin(boolean useEmailForLogin) {
		this.useEmailForLogin = useEmailForLogin;
	}

	/**
	 * Returns the view that will be returned from this controller when the
	 * registration is successful. The success view should be a redirect (e.g.
	 * start with "redirect:" since this will cause the entire SpringSecurity
	 * pipeline to be fulfilled.
	 * 
	 * By default, returns "redirect:/"
	 * 
	 * @return the register success view
	 */
	public String getRegisterSuccessView() {
		return registerSuccessView;
	}

	/**
	 * Returns the view that will be used to display the registration page.
	 * 
	 * By default, returns "/register"
	 * 
	 * @return the register view
	 */
	public String getRegisterView() {
		return registerView;
	}
}
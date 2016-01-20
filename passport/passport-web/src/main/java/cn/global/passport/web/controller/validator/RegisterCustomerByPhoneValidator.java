package cn.global.passport.web.controller.validator;

import org.apache.commons.lang.StringUtils;

import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.passport.web.core.form.RegisterCustomerForm;
import cn.globalph.web.util.WebValidation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

/**
 * @author chet.qiu
 */
@Component("blRegisterCustomerByPhoneValidator")
public class RegisterCustomerByPhoneValidator implements Validator {

	private String validatePasswordExpression = "[0-9A-Za-z]{4,15}";

	@Resource(name = "blCustomerService")
	private CustomerService customerService;

	public RegisterCustomerByPhoneValidator() {
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return clazz.equals(RegisterCustomerForm.class);
	}

	public void validate(Object obj, Errors errors) {
		RegisterCustomerForm form = (RegisterCustomerForm) obj;
		if (StringUtils.isNotEmpty(form.getCustomer().getPhone())) {
			Customer customer = customerService.readCustomerByPhone(form
					.getCustomer().getPhone());
			if(customer == null){
				if(!WebValidation.isPhone(form.getCustomer().getPhone())){
					errors.rejectValue("customer.phone", "phone.invalid", null, null);
				}else{
					errors.rejectValue("customer.phone", "phone.no.exsit", null, null);
				}
			}else if (customer.isRegistered()) {
				errors.rejectValue("customer.phone", "phone.used", null, null);
			} else {
				if (StringUtils.isEmpty(form.getCustomer().getValidationCode())) {
					errors.rejectValue("customer.validationCode", "validationCode.required",
							null, null);
				} else if (!customer.getValidationCode().equals(
						form.getCustomer().getValidationCode())) {
					errors.rejectValue("customer.validationCode", "validationCode.invalid",
							null, null);
				}else{
					customerService.deleteCustomer(customer);
				}
			}
		} else {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customer.phone",
					"phone.required");
		}

		if (!errors.hasErrors()) {
			if (!form.getPassword().matches(getValidatePasswordExpression())) {
				errors.rejectValue("password", "password.invalid", null, null);
			} 
		}
	}

	public String getValidatePasswordExpression() {
		return validatePasswordExpression;
	}

	public void setValidatePasswordExpression(String validatePasswordExpression) {
		this.validatePasswordExpression = validatePasswordExpression;
	}
}

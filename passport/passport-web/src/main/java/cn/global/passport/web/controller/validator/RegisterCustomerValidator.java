package cn.global.passport.web.controller.validator;

import org.apache.commons.validator.GenericValidator;

import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.passport.web.core.form.RegisterCustomerForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.annotation.Resource;

/**
 * @author felix.wu
 */
@Component("blRegisterCustomerValidator")
public class RegisterCustomerValidator implements Validator {

    private String validatePasswordExpression = "[0-9A-Za-z]{4,15}";

    @Resource(name="blCustomerService")
    private CustomerService customerService;

    public RegisterCustomerValidator() {}

    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return clazz.equals(RegisterCustomerForm.class);
    }
    
    public void validate(Object obj, Errors errors) {
        validate(obj, errors, false);
    }
    
    /*
     *         errors.pushNestedPath("customer");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "emailAddress.required");
        errors.popNestedPath();

        if (errors.hasErrors()){
            if (!passwordConfirm.equals(password)) {
                errors.rejectValue("passwordConfirm", "invalid"); 
            }
            if (!customer.getFirstName().matches(validNameRegex)) {
                errors.rejectValue("firstName", "firstName.invalid", null, null);
            }

            if (!customer.getLastName().matches(validNameRegex)) {
                errors.rejectValue("lastName", "lastName.invalid", null, null);
            }

            if (!customer.getPassword().matches(validPasswordRegex)) {
                errors.rejectValue("password", "password.invalid", null, null);
            }

            if (!password.equals(passwordConfirm)) {
                errors.rejectValue("password", "passwordConfirm.invalid", null, null);
            }

            if (!GenericValidator.isEmail(customer.getEmailAddress())) {
                errors.rejectValue("emailAddress", "emailAddress.invalid", null, null);
            }
        }
     */

    public void validate(Object obj, Errors errors, boolean useEmailForUsername) {
        RegisterCustomerForm form = (RegisterCustomerForm) obj;

        Customer customerFromDb = customerService.readCustomerByUsername(form.getCustomer().getLoginName());

        if (customerFromDb != null) {
            if (useEmailForUsername) {
                errors.rejectValue("customer.emailAddress", "emailAddress.used", null, null);
            } else {
                errors.rejectValue("customer.username", "username.used", null, null);
            }
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "passwordConfirm.required");
        
        errors.pushNestedPath("customer");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "emailAddress.required");
        errors.popNestedPath();


        if (!errors.hasErrors()) {

            if (!form.getPassword().matches(getValidatePasswordExpression())) {
                errors.rejectValue("password", "password.invalid", null, null);
            }

            if (!form.getPassword().equals(form.getPasswordConfirm())) {
                errors.rejectValue("password", "passwordConfirm.invalid", null, null);
            }

            if (!GenericValidator.isEmail(form.getCustomer().getEmailAddress())) {
                errors.rejectValue("customer.emailAddress", "emailAddress.invalid", null, null);
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

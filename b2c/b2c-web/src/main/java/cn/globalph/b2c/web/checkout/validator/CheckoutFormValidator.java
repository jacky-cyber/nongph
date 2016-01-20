package cn.globalph.b2c.web.checkout.validator;

import org.apache.commons.validator.GenericValidator;

import cn.globalph.b2c.web.checkout.model.CheckoutForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("blCheckoutFormValidator")
public class CheckoutFormValidator implements Validator {

    @SuppressWarnings("rawtypes")
    public boolean supports(Class clazz) {
        return clazz.equals(CheckoutForm.class);
    }

    public void validate(Object obj, Errors errors) {
        CheckoutForm checkoutForm = (CheckoutForm) obj;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billingAddress.addressLine1", "addressLine1.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billingAddress.phonePrimary", "phone.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billingAddress.city", "city.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billingAddress.postalCode", "postalCode.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billingAddress.firstName", "firstName.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "billingAddress.lastName", "lastName.required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingAddress.addressLine1", "addressLine1.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingAddress.city", "city.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingAddress.postalCode", "postalCode.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingAddress.firstName", "firstName.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "shippingAddress.lastName", "lastName.required");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "emailAddress.required");
        
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardNumber", "creditCardNumber.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardCvvCode", "creditCardCvvCode.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardExpMonth", "creditCardExpMonth.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "creditCardExpYear", "creditCardExpYear.required");

        if (!errors.hasErrors()) {
            if (!GenericValidator.isEmail(checkoutForm.getEmailAddress())) {
                errors.rejectValue("emailAddress", "emailAddress.invalid", null, null);
            }
        }
    }
}


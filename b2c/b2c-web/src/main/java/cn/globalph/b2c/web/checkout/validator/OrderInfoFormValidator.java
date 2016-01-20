package cn.globalph.b2c.web.checkout.validator;

import org.apache.commons.validator.EmailValidator;

import cn.globalph.b2c.web.checkout.model.OrderInfoForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("blOrderInfoFormValidator")
public class OrderInfoFormValidator implements Validator {

    @SuppressWarnings("rawtypes")
    public boolean supports(Class clazz) {
        return clazz.equals(OrderInfoForm.class);
    }

    public void validate(Object obj, Errors errors) {
        OrderInfoForm orderInfoForm = (OrderInfoForm) obj;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "emailAddress.required");
        
        if (!errors.hasErrors()) {
            if (!EmailValidator.getInstance().isValid(orderInfoForm.getEmailAddress())) {
                errors.rejectValue("emailAddress", "emailAddress.invalid", null, null);
            }
        }
    }
}

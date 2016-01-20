package cn.globalph.b2c.web.checkout.validator;

import cn.globalph.b2c.web.checkout.model.ShippingInfoForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("blShippingInfoFormValidator")
public class ShippingInfoFormValidator implements Validator {

    @SuppressWarnings("rawtypes")
    public boolean supports(Class clazz) {
        return clazz.equals(ShippingInfoForm.class);
    }

    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.postalCode", "postalCode.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.address", "address.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.phone", "phone.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.receiver", "receiver.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.firstLevelCommunity", "firstLevelCommunity.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.secondLevelCommunity", "secondLevelCommunity.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fulfillmentOptionId", "fulfillmentOptionId.required");
    }
}

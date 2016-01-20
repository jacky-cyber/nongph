package cn.globalph.b2c.web.checkout.validator;

import cn.globalph.b2c.web.checkout.model.ShippingInfoForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component("blUSShippingInfoFormValidator")
public class USShippingInfoFormValidator extends ShippingInfoFormValidator {

    @SuppressWarnings("rawtypes")
    public boolean supports(Class clazz) {
        return clazz.equals(ShippingInfoForm.class);
    }

    public void validate(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.receiver", "receiver.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.phone", "phone.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.province", "province.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.city", "city.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.district", "district.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address.address", "address.required");
    }
}

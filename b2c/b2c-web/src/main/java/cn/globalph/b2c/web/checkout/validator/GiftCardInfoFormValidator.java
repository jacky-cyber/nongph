package cn.globalph.b2c.web.checkout.validator;

import cn.globalph.b2c.web.checkout.model.GiftCardInfoForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Jerry Ocanas (jocanas)
 */
@Component("blGiftCardInfoFormValidator")
public class GiftCardInfoFormValidator implements Validator {

    @Override
    @SuppressWarnings("rawtypes")
    public boolean supports(Class clazz) {
        return clazz.equals(GiftCardInfoForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GiftCardInfoForm giftCardInfoForm = (GiftCardInfoForm) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "giftCardNumber", "giftCardNumber.required");
        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "giftCardEmailAddress", "giftCardEmailAddress.required");

        //if (!errors.hasErrors()) {
        //    if (!GenericValidator.isEmail(giftCardInfoForm.getGiftCardEmailAddress())) {
        //        errors.rejectValue("giftCardEmailAddress", "giftCardEmailAddress.invalid", null, null);
        //    }
        //}
    }
}


package cn.global.passport.web.controller.validator;

import org.apache.commons.validator.GenericValidator;

import cn.global.passport.web.controller.UpdateAccountForm;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.service.CustomerService;
import cn.globalph.passport.web.core.CustomerState;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.annotation.Resource;


@Component("blUpdateAccountValidator")
public class UpdateAccountValidator implements Validator {

    @Resource(name="blCustomerService")
    protected CustomerService customerService;

    public void validate(UpdateAccountForm form, Errors errors) {

//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress", "emailAddress.required");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "phone.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required");

//        if (!errors.hasErrors()) {

            //is this a valid email address?
//            if (!GenericValidator.isEmail(form.getEmailAddress())) {
//                errors.rejectValue("emailAddress", "emailAddress.invalid");
//            }

//            //验证新的邮箱地址是否已被使用
//            Customer customerMatchingNewEmail = customerService.readCustomerByEmail(form.getEmailAddress());
//
//            if (customerMatchingNewEmail != null && CustomerState.getCustomer().getId() != customerMatchingNewEmail.getId()) {
//                errors.rejectValue("emailAddress", "emailAddress.used");
//            }
            //验证新的手机号码是否已被使用
//            Customer customerMatchingNewPhone = customerService.readCustomerByPhone(form.getPhone());
//            if (customerMatchingNewPhone != null && CustomerState.getCustomer().getId() != customerMatchingNewPhone.getId()) {
//                errors.rejectValue("phone", "phone.used");
//            }
//
//        }

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {

    }

}

package cn.global.passport.web.controller.validator;

import cn.global.passport.web.controller.CustomerAddressForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("blCustomerAddressValidator")
public class CustomerAddressValidator implements Validator {

    @SuppressWarnings("rawtypes")
    public boolean supports(Class clazz) {
        return clazz.equals(CustomerAddressValidator.class);
    }

    public void validate(Object obj, Errors errors) {
    	CustomerAddressForm customerAddressForm = (CustomerAddressForm)obj;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "province", "province.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "city.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "district", "district.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "receiver", "receiver.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "address", "address.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "phone.required");

        if (!errors.hasErrors()) {
        	if(!isPhone(customerAddressForm.getPhone())){
        		errors.rejectValue("phone", "phone.error",null,null);
        	}

			if (customerAddressForm.getReceiver().length() > 20) {
				errors.rejectValue("receiver", "receiver.error", null, null);
			}

			if (customerAddressForm.getAddress().length() > 60) {
				errors.rejectValue("address", "address.too.long", null, null);
			}

			if (StringUtils.isNotEmpty(customerAddressForm.getPostalCode()) && !isPostalCode(customerAddressForm.getPostalCode())) {
				errors.rejectValue("postalCode", "postalCode.error",null,null);
        	}
        }
    }
    
    private boolean isPhone(String s){
    	if(s.length()>11)
    		return false;
    	char[] arr = s.toCharArray();
    	
    	for(char c : arr){
    		if(!Character.isDigit(c)){
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    private boolean isPostalCode(String s){
    	if(s.length()>10)
    		return false;
    	char[] arr = s.toCharArray();
    	
    	for(char c : arr){
    		if(!Character.isDigit(c)){
    			return false;
    		}
    	}
    	
    	return true;
    }
}


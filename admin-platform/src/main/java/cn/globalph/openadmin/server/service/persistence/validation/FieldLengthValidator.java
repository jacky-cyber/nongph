package cn.globalph.openadmin.server.service.persistence.validation;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.common.web.WebRequestContext;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;


/**
 * Ensures that field values submitted in the admin are less than or equal to the length specified in the metadata
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("blFieldLengthValidator")
public class FieldLengthValidator implements GlobalPropertyValidator {

    @Override
    public PropertyValidationResult validate(Entity entity,
            Serializable instance,
            Map<String, FieldMetadata> entityFieldMetadata,
            BasicFieldMetadata propertyMetadata,
            String propertyName,
            String value) {
        boolean valid = true;
        String errorMessage = "";
        if (propertyMetadata.getLength() != null) {
            valid = StringUtils.length(value) <= propertyMetadata.getLength();
        }
        
        if (!valid) {
            WebRequestContext context = WebRequestContext.getWebRequestContext();
            MessageSource messages = context.getMessageSource();
            errorMessage = messages.getMessage("fieldLengthValidationFailure",
                    new Object[] {propertyMetadata.getLength(), StringUtils.length(value) },
                    Locale.CHINA);
        }
        
        return new PropertyValidationResult(valid, errorMessage);
    }

}

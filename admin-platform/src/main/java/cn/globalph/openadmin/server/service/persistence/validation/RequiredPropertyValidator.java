package cn.globalph.openadmin.server.service.persistence.validation;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;


/**
 * Ensures that every property that is required from {@link BasicFieldMetadata#getRequired()} has a non-empty value being
 * set.
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("blRequiredPropertyValidator")
public class RequiredPropertyValidator implements GlobalPropertyValidator {

    public static String ERROR_MESSAGE = "requiredValidationFailure";
    
    @Override
    public PropertyValidationResult validate(Entity entity,
                            Serializable instance,
                            Map<String, FieldMetadata> entityFieldMetadata,
                            BasicFieldMetadata propertyMetadata,
                            String propertyName,
                            String value) {
        boolean required = BooleanUtils.isTrue(propertyMetadata.getRequired());
        if (propertyMetadata.getRequiredOverride() != null) {
            required = propertyMetadata.getRequiredOverride();
        }
        boolean valid = !(required && StringUtils.isEmpty(value));
        return new PropertyValidationResult(valid, ERROR_MESSAGE);
    }

}

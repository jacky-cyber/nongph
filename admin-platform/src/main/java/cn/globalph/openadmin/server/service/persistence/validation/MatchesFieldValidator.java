package cn.globalph.openadmin.server.service.persistence.validation;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;


/**
 * Checks for equality between this field and a configured 'otherField'
 * 
 * @author Phillip Verheyden
 */
@Component("blMatchesFieldValidator")
public class MatchesFieldValidator extends ValidationConfigurationBasedPropertyValidator implements FieldNamePropertyValidator {

    @Override
    public boolean validateInternal(Entity entity,
            Serializable instance,
            Map<String, FieldMetadata> entityFieldMetadata,
            Map<String, String> validationConfiguration,
            BasicFieldMetadata propertyMetadata,
            String propertyName,
            String value) {
        String otherField = validationConfiguration.get("otherField");
        return StringUtils.equals(entity.getPMap().get(otherField).getValue(), value);
    }

}

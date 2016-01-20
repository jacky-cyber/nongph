package cn.globalph.openadmin.server.service.persistence.validation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.presentation.ConfigurationItem;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;
import java.util.regex.PatternSyntaxException;


/**
 * Validates a field against a configured 'regularExpression' item
 * 
 * @author Phillip Verheyden
 */
@Component("blRegexPropertyValidator")
public class RegexPropertyValidator extends ValidationConfigurationBasedPropertyValidator {

    protected static final Log LOG = LogFactory.getLog(RegexPropertyValidator.class);

    /**
     * Denotes what should occur when this validator encounters a null value to validate against. Default behavior is to
     * allow them, which means that this validator will always return true with null values
     */
    protected boolean succeedForNullValues = true;
    
    /**
     * Whether or not this validator should succeed (and thus pass validation) even when the configured regular expression
     * pattern is invalid. While this value defaults to true, it might be beneficial to set this to false in a development
     * environment to debug problems in regular expressions. In either case, this validator will log that there is an invalid
     * regular expression
     */
    protected boolean suceedForInvalidRegex = true;
    
    @Override
    public PropertyValidationResult validate(Entity entity,
            Serializable instance,
            Map<String, FieldMetadata> entityFieldMetadata,
            Map<String, String> validationConfiguration,
            BasicFieldMetadata propertyMetadata,
            String propertyName,
            String value) {
        String expression = validationConfiguration.get("regularExpression");
        
        if (value == null) {
            return new PropertyValidationResult(succeedForNullValues, validationConfiguration.get(ConfigurationItem.ERROR_MESSAGE));
        }
        
        try {
            return new PropertyValidationResult(value.matches(expression), validationConfiguration.get(ConfigurationItem.ERROR_MESSAGE));
        } catch (PatternSyntaxException e) {
            String message = "Invalid regular expression pattern '" + expression + "' for " + propertyName;
            LOG.error(message, e);
            return new PropertyValidationResult(suceedForInvalidRegex, "Invalid regular expression pattern for " + propertyName);
        }
    }
    
    public boolean isSucceedForNullValues() {
        return succeedForNullValues;
    }

    public void setSucceedForNullValues(boolean succeedForNullValues) {
        this.succeedForNullValues = succeedForNullValues;
    }

    public boolean isSuceedForInvalidRegex() {
        return suceedForInvalidRegex;
    }

    public void setSuceedForInvalidRegex(boolean suceedForInvalidRegex) {
        this.suceedForInvalidRegex = suceedForInvalidRegex;
    }

}

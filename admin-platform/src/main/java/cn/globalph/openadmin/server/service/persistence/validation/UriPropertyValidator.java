package cn.globalph.openadmin.server.service.persistence.validation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.util.BLCSystemProperty;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * Validates a field as being a valid URI to ensure compatibility with Broadleaf handlers including
 * PageHandler, ProductHandler, and CategoryHandlers.
 * 
 * Component can be overridden with the following properties:
 * 
 * This component was introduced instead of using RegEx because most site have simple url needs and BLC out of 
 * box simply requires that the URL start with a / and use valid url characters.
 * 
 * Replace if needed for your implementation.
 * 
 * 
 * @author Brian Polster
 */
@Component("blUriPropertyValidator")
public class UriPropertyValidator extends ValidationConfigurationBasedPropertyValidator {

    protected static final Log LOG = LogFactory.getLog(UriPropertyValidator.class);

    protected String ERROR_KEY_BEGIN_WITH_SLASH = "uriPropertyValidatorMustBeginWithSlashError";
    protected String ERROR_KEY_CANNOT_END_WITH_SLASH = "uriPropertyValidatorCannotEndWithSlashError";

    protected boolean getIgnoreFullUrls() {
        return BLCSystemProperty.resolveBooleanSystemProperty("uriPropertyValidator.ignoreFullUrls");
    }

    protected boolean getRequireLeadingSlash() {
        return BLCSystemProperty.resolveBooleanSystemProperty("uriPropertyValidator.requireLeadingSlash");
    }

    protected boolean getAllowTrailingSlash() {
        return BLCSystemProperty.resolveBooleanSystemProperty("uriPropertyValidator.allowTrailingSlash");
    }

    public boolean isFullUrl(String url) {
        return (url.startsWith("http") || url.startsWith("ftp"));
    }

    /**
     * Denotes what should occur when this validator encounters a null value to validate against. Default behavior is to
     * allow them, which means that this validator will always return true with null values
     */
    protected boolean succeedForNullValues = true;
    
    @Override
    public PropertyValidationResult validate(Entity entity,
            Serializable instance,
            Map<String, FieldMetadata> entityFieldMetadata,
            Map<String, String> validationConfiguration,
            BasicFieldMetadata propertyMetadata,
            String propertyName,
            String value) {
        
        if (value == null) {
            return new PropertyValidationResult(succeedForNullValues);
        }
        
        if (isFullUrl(value) && getIgnoreFullUrls()) {
            return new PropertyValidationResult(true);
        }

        if (getRequireLeadingSlash() && !value.startsWith("/")) {
            return new PropertyValidationResult(false, ERROR_KEY_BEGIN_WITH_SLASH);
        }

        if (!getAllowTrailingSlash() && value.endsWith("/") && value.length() > 1) {
            return new PropertyValidationResult(false, ERROR_KEY_CANNOT_END_WITH_SLASH);
        }

        return new PropertyValidationResult(true);
    }
    
    public boolean isSucceedForNullValues() {
        return succeedForNullValues;
    }

    public void setSucceedForNullValues(boolean succeedForNullValues) {
        this.succeedForNullValues = succeedForNullValues;
    }
}

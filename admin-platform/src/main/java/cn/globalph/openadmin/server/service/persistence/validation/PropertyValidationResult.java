package cn.globalph.openadmin.server.service.persistence.validation;


/**
 * Empty DTO for now that just denotes that this validation error is from a property
 *
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link PropertyValidator}
 */
public class PropertyValidationResult extends GlobalValidationResult {

    public PropertyValidationResult(boolean valid, String errorMessage) {
        super(valid, errorMessage);
    }
    
    public PropertyValidationResult(boolean valid) {
        super(valid);
    }
    
}

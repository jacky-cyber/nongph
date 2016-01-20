package cn.globalph.openadmin.server.service.persistence.validation;

import cn.globalph.common.presentation.ConfigurationItem;
import cn.globalph.common.presentation.ValidationConfiguration;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;

import java.io.Serializable;
import java.util.Map;


/**
 * <p>Interface for performing validation on a property. If you are attempting to write a validator based on the
 * @ValidationConfiguration component, (which is the normal use case) consider subclassing
 * {@link ValidationConfigurationBasedPropertyValidator} and overriding
 * {@link ValidationConfigurationBasedPropertyValidator#validateInternal(Entity, Serializable, Map, Map, BasicFieldMetadata, String, String)}
 * as it provides a slightly more convenient step for getting the error message from the given configuration.</p>
 * 
 * <p>If instead you need to validate based on something else (like the field type, for instance) then you should instead
 * implement this interface directly so that you can provide your own error message.</p>
 * 
 * <p>Property validators are designed to be executed after an entity has been fully populated. If instead you would like
 * to validate {@link PopulationRequests} (which will be invoked immediately prior to populating a particular field on an
 * entity) then instead look at {@link PopulateValueRequestValidator}.</p>
 * 
 * @author Phillip Verheyden
 * @see {@link ValidationConfigurationBasedPropertyValidator}
 * @see {@link EntityValidatorService}
 * @see {@link GlobalPropertyValidator}
 */
public interface PropertyValidator {

    /**
     * Validates a property for an entity
     * 
     * @param entity Entity DTO of the entity attempting to save
     * @param instance actual object representation of <b>entity</b>. This can be cast to entity interfaces (like Sku or
     * Product)
     * @param entityFieldMetadata complete field metadata for all properties in <b>entity</b>
     * @param validationConfiguration the map represented by the set of {@link ConfigurationItem} for a
     * {@link ValidationConfiguration} on a property. This map could be null if this {@link PropertyValidator} is being
     * invoked outside of the context of a particular property (like a global validator)
     * @param propertyMetadata {@link BasicFieldMetadata} corresponding to the property that is being valid
     * @param propertyName the property name of the value attempting to be saved (could be a sub-entity obtained via dot
     * notation like 'defaultSku.name')
     * @param value the value attempted to be saved
     * @return <b>true</b> if this passes validation, <b>false</b> otherwise.
     */
    public PropertyValidationResult validate(Entity entity,
                                            Serializable instance,
                                            Map<String, FieldMetadata> entityFieldMetadata,
                                            Map<String, String> validationConfiguration,
                                            BasicFieldMetadata propertyMetadata,
                                            String propertyName,
                                            String value);

}

package cn.globalph.openadmin.server.service.persistence.validation;

import cn.globalph.common.presentation.ValidationConfiguration;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.server.service.persistence.module.BasicPersistenceModule;

import java.io.Serializable;
import java.util.Map;


/**
 * Analagous to {@link PropertyValidator} except this does not attempt to use any {@link ValidationConfiguration} from an
 * {@link AdminPresentation} annotation. These global validators will execute on every field of every entity that is
 * attempted to be populated by the admin
 *
 * @author Phillip Verheyden (phillipuniverse)
 * @see {@link PropertyValidator}
 * @see {@link EntityValidatorService#getGlobalEntityValidators()}
 * @see {@link BasicPersistenceModule#createPopulatedInstance(Serializable, Entity, Map, Boolean)}
 */
public interface GlobalPropertyValidator {

    /**
     * Validates a property for an entity
     * 
     * @param entity Entity DTO of the entity attempting to save
     * @param instance actual object representation of <b>entity</b>. This can be cast to entity interfaces (like Sku or
     * Product)
     * @param entityFieldMetadata complete field metadata for all properties in <b>entity</b>
     * @param propertyMetadata {@link BasicFieldMetadata} corresponding to the property that is being valid
     * @param propertyName the property name of the value attempting to be saved (could be a sub-entity obtained via dot
     * notation like 'defaultSku.name')
     * @param value the value attempted to be saved
     * @return <b>true</b> if this passes validation, <b>false</b> otherwise.
     */
    public PropertyValidationResult validate(Entity entity,
                                            Serializable instance,
                                            Map<String, FieldMetadata> entityFieldMetadata,
                                            BasicFieldMetadata propertyMetadata,
                                            String propertyName,
                                            String value);

}

package cn.globalph.openadmin.server.service.persistence.validation;

import org.apache.commons.collections.CollectionUtils;

import cn.globalph.common.presentation.ValidationConfiguration;
import cn.globalph.common.presentation.client.OperationScope;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.security.service.RowLevelSecurityService;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationException;
import cn.globalph.openadmin.server.service.persistence.module.BasicPersistenceModule;
import cn.globalph.openadmin.server.service.persistence.module.FieldNotAvailableException;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;


/**
 * This implementation validates each {@link Property} from the given {@link Entity} according to the
 * {@link ValidationConfiguration}s associated with it.
 * 
 * @author Phillip Verheyden
 * @see {@link EntityValidatorService}
 * @see {@link ValidationConfiguration}
 */
@Service("blEntityValidatorService")
public class EntityValidatorServiceImpl implements EntityValidatorService, ApplicationContextAware {
    
    @Resource(name = "blGlobalEntityPropertyValidators")
    protected List<GlobalPropertyValidator> globalEntityValidators;
    
    protected ApplicationContext applicationContext;

    @Resource(name = "blRowLevelSecurityService")
    protected RowLevelSecurityService securityService;
    
    @Override
    public void validate(Entity submittedEntity, Serializable instance, Map<String, FieldMetadata> propertiesMetadata,
            RecordHelper recordHelper, boolean validateUnsubmittedProperties) {
        Object idValue = null;
        if (instance != null) {
            String idField = (String) ((BasicPersistenceModule) recordHelper.getCompatibleModule(OperationScope.BASIC)).
                getPersistenceManager().getDynamicEntityDao().getIdMetadata(instance.getClass()).get("name");
            try {
                idValue = recordHelper.getFieldManager().getFieldValue(instance, idField);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (FieldNotAvailableException e) {
                throw new RuntimeException(e);
            }
        }
        Entity entity;
        boolean isUpdateRequest;
        if (idValue == null) {
            //This is for an add, or if the instance variable is null (e.g. PageTemplateCustomPersistenceHandler)
            entity = submittedEntity;
            isUpdateRequest = false;
        } else {
            //This is for an update, as the submittedEntity instance will likely only contain the dirty properties
            entity = recordHelper.getRecord(propertiesMetadata, instance, null, null);
            //acquire any missing properties not harvested from the instance and add to the entity. A use case for this
            //would be the confirmation field for a password validation
            for (Map.Entry<String, FieldMetadata> entry : propertiesMetadata.entrySet()) {
                if (entity.findProperty(entry.getKey()) == null) {
                    Property myProperty = submittedEntity.findProperty(entry.getKey());
                    if (myProperty != null) {
                        entity.addProperty(myProperty);
                    }
                }
            }
            isUpdateRequest = true;
        }
            
        List<String> types = getTypeHierarchy(entity);
        //validate each individual property according to their validation configuration
        for (Entry<String, FieldMetadata> metadataEntry : propertiesMetadata.entrySet()) {
            FieldMetadata metadata = metadataEntry.getValue();

            //Don't test this field if it was not inherited from our polymorphic type (or supertype)
            if (types.contains(metadata.getInheritedFromType())) {
                Property property = entity.getPMap().get(metadataEntry.getKey());

                // This property should be set to false only in the case where we are adding a member to a collection
                // that has type of lookup. In this case, we don't have the properties from the target in our entity,
                // and we don't need to validate them.
                if (!validateUnsubmittedProperties && property == null) {
                    continue;
                }

                //for radio buttons, it's possible that the entity property was never populated in the first place from the POST
                //and so it will be null
                String propertyName = metadataEntry.getKey();
                String propertyValue = (property == null) ? null : property.getValue();

                if (metadata instanceof BasicFieldMetadata) {
                    //First execute the global field validators
                    if (CollectionUtils.isNotEmpty(globalEntityValidators)) {
                        for (GlobalPropertyValidator validator : globalEntityValidators) {
                            PropertyValidationResult result = validator.validate(entity,
                                    instance,
                                    propertiesMetadata,
                                    (BasicFieldMetadata)metadata,
                                    propertyName,
                                    propertyValue);
                            if (!result.isValid()) {
                                submittedEntity.addValidationError(propertyName, result.getErrorMessage());
                            }
                        }
                    }

                    //Now execute the validators configured for this particular field
                    Map<String, Map<String, String>> validations =
                            ((BasicFieldMetadata) metadata).getValidationConfigurations();
                    for (Map.Entry<String, Map<String, String>> validation : validations.entrySet()) {
                        String validationImplementation = validation.getKey();
                        Map<String, String> configuration = validation.getValue();

                        PropertyValidator validator = null;

                        //attempt bean resolution to find the validator
                        if (applicationContext.containsBean(validationImplementation)) {
                            validator = applicationContext.getBean(validationImplementation, PropertyValidator.class);
                        }

                        //not a bean, attempt to instantiate the class
                        if (validator == null) {
                            try {
                                validator = (PropertyValidator) Class.forName(validationImplementation).newInstance();
                            } catch (Exception e) {
                                //do nothing
                            }
                        }

                        if (validator == null) {
                            throw new PersistenceOperationException("Could not find validator: " + validationImplementation +
                                    " for property: " + propertyName);
                        }

                        PropertyValidationResult result = validator.validate(entity,
                                                                        instance,
                                                                        propertiesMetadata,
                                                                        configuration,
                                                                        (BasicFieldMetadata)metadata,
                                                                        propertyName,
                                                                        propertyValue);
                        if (!result.isValid()) {
                            for (String message : result.getErrorMessages()) {
                                submittedEntity.addValidationError(propertyName, message);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Returns the type hierarchy of the given <b>entity</b> in ascending order of type, stopping at Object
     * 
     * <p>
     * For instance, if this entity's {@link Entity#getType()} is {@link ProductBundleImpl}, then the result will be:
     * 
     * [cn.globalph.b2c.product.domain.ProductBundleImpl, cn.globalph.b2c.product.domain.ProductImpl]
     * 
     * @param entity
     * @return
     */
    protected List<String> getTypeHierarchy(Entity entity) {
        List<String> types = new ArrayList<String>();
        Class<?> myType;
        try {
            myType = Class.forName(entity.getType()[0]);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        types.add(myType.getName());
        boolean eof = false;
        while (!eof) {
            myType = myType.getSuperclass();
            if (myType != null && !myType.getName().equals(Object.class.getName())) {
                types.add(myType.getName());
            } else {
                eof = true;
            }
        }
        return types;
    }

    @Override
    public List<GlobalPropertyValidator> getGlobalEntityValidators() {
        return globalEntityValidators;
    }

    @Override
    public void setGlobalEntityValidators(List<GlobalPropertyValidator> globalEntityValidators) {
        this.globalEntityValidators = globalEntityValidators;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}

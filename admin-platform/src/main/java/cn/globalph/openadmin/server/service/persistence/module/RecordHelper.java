package cn.globalph.openadmin.server.service.persistence.module;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.presentation.client.OperationScope;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.EntityResult;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.service.ValidationException;
import cn.globalph.openadmin.server.service.persistence.module.criteria.FilterMapping;
import cn.globalph.openadmin.server.service.persistence.module.criteria.RestrictionFactory;
import cn.globalph.openadmin.server.service.persistence.validation.EntityValidatorService;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Helper interface for serializing/deserializing the generic {@link Entity} DTO to/from its actual domain object
 * representation. 
 * 
 * @felix.wu
 * @see {@link BasicPersistenceModule}
 * @see {@link MapStructurePersistenceModule}
 * @see {@link AdornedTargetListPersistenceModule}
 */
public interface RecordHelper extends DataFormatProvider {

    public List<FilterMapping> getFilterMappings(PersistenceAssociation persistencePerspective, CriteriaTransferObject cto,
                                                 String ceilingEntityFullyQualifiedClassname,
                                                 Map<String, FieldMetadata> mergedProperties);

    public List<FilterMapping> getFilterMappings(PersistenceAssociation persistencePerspective, CriteriaTransferObject cto,
                                                     String ceilingEntityFullyQualifiedClassname,
                                                     Map<String, FieldMetadata> mergedUnfilteredProperties,
                                                     RestrictionFactory customRestrictionFactory);

    public Entity[] getRecords(Map<String, FieldMetadata> primaryMergedProperties, List<? extends Serializable> records, Map<String, FieldMetadata> alternateMergedProperties, String pathToTargetObject);

    public Entity[] getRecords(Map<String, FieldMetadata> primaryMergedProperties, List<? extends Serializable> records);
    
    public Entity[] getRecords(Class<?> ceilingEntityClass, PersistenceAssociation persistencePerspective, List<? extends Serializable> records);
    
    public Entity getRecord(Map<String, FieldMetadata> primaryMergedProperties, Serializable record, Map<String, FieldMetadata> alternateMergedProperties, String pathToTargetObject);
    
    public Entity getRecord(Class<?> ceilingEntityClass, PersistenceAssociation persistencePerspective, Serializable record);

    /**
     * <p>Populates a Hibernate entity <b>instance</b> based on the values from <b>entity</b> (the DTO representation of
     * <b>instance</b>) and the metadata from <b>mergedProperties</b>.</p>
     * <p>While populating <b>instance</b>, validation is also performed using the {@link EntityValidatorService}. If this
     * validation fails, then the instance is left unchanged and a {@link ValidationExcpetion} is thrown. In the common
     * case, this exception bubbles up to the {@link DynamicRemoteService} which catches the exception and communicates
     * appropriately to the invoker</p>
     * 
     * @param instance
     * @param entity
     * @param mergedProperties
     * @param setId
     * @param validateUnsubmittedProperties if set to true, will ignore validation for properties that weren't submitted
     *                                      along with the entity
     * @throws ValidationException if after populating <b>instance</b> via the values in <b>entity</b> then
     * {@link EntityValidatorService#validate(Entity, Serializable, Map)} returns false
     * @return <b>instance</b> populated with the property values from <b>entity</b> according to the metadata specified
     * in <b>mergedProperties</b>
     * @see {@link EntityValidatorService}
     */
    public Serializable createPopulatedInstance(Serializable instance, Entity entity, 
            Map<String, FieldMetadata> mergedProperties, Boolean setId, Boolean validateUnsubmittedProperties) throws ValidationException;

    /**
     * Delegates to the overloaded method with validateUnsubmittedProperties set to true.
     * 
     * @see #createPopulatedInstance(Serializable, Entity, Map, Boolean, Boolean)
     */
    public Serializable createPopulatedInstance(Serializable instance, Entity entity, 
            Map<String, FieldMetadata> unfilteredProperties, Boolean setId) throws ValidationException;
    
    public Object getPrimaryKey(Entity entity, Map<String, FieldMetadata> mergedProperties);
    
    public Map<String, FieldMetadata> getSimpleMergedProperties(String entityName, PersistenceAssociation persistencePerspective);
    
    public FieldManager getFieldManager();

    public PersistenceModule getCompatibleModule(OperationScope operationType);

    /**
     * Validates the {@link Entity} based on the validators associated with each property
     * @param entity the instance that is attempted to be saved from. Implementers should set {@link Entity#isValidationFailure()}
     * accordingly as a result of the validation
     * @param populatedInstance
     * @param mergedProperties TODO
     * @param validateUnsubmittedProperties if set to true, will ignore validation for properties that weren't submitted
     *                                      along with the entity
     * @return whether or not the entity passed validation. This yields the same result as calling !{@link Entity#isValidationFailure()}
     * after invoking this method
     */
    public boolean validate(Entity entity, Serializable populatedInstance, Map<String, FieldMetadata> mergedProperties, boolean validateUnsubmittedProperties);

    /**
     * Delegates to the overloaded method with validateUnsubmittedProperties set to true.
     * 
     * @see #validate(Entity, Serializable, Map, boolean)
     */
    public boolean validate(Entity entity, Serializable populatedInstance, Map<String, FieldMetadata> mergedProperties);

    public Integer getTotalRecords(String ceilingEntity, List<FilterMapping> filterMappings);

    public Serializable getMaxValue(String ceilingEntity, List<FilterMapping> filterMappings, String maxField);

    public List<Serializable> getPersistentRecords(String ceilingEntity, List<FilterMapping> filterMappings, Integer firstResult, Integer maxResults);

    public EntityResult update(PersistencePackage persistencePackage, boolean includeRealEntityObject) throws ServiceException;

    public EntityResult add(PersistencePackage persistencePackage, boolean includeRealEntityObject) throws ServiceException;

    /**
     * Returns a string representation of the field on the given instance specified by the property name. The propertyName
     * should start from the root of the given instance
     * 
     * @param instance
     * @param propertyName
     * @return
     */
    public String getStringValueFromGetter(Serializable instance, String propertyName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;

}

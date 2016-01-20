package cn.globalph.openadmin.server.service.persistence.module;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.exception.SecurityServiceException;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.client.OperationScope;
import cn.globalph.common.presentation.client.PersistenceAssociationItemType;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.common.util.FormatUtil;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.EntityResult;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.ForeignKey;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.ValidationException;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationException;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationExecutor;
import cn.globalph.openadmin.server.service.persistence.module.criteria.CriteriaTranslator;
import cn.globalph.openadmin.server.service.persistence.module.criteria.FilterMapping;
import cn.globalph.openadmin.server.service.persistence.module.criteria.RestrictionFactory;
import cn.globalph.openadmin.server.service.persistence.module.provider.FieldPersistenceProvider;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.AddFilterPropertiesRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.AddSearchMappingRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.PopulateValueRequest;
import cn.globalph.openadmin.server.service.persistence.validation.EntityValidatorService;
import cn.globalph.openadmin.server.service.persistence.validation.PopulateValueRequestValidator;
import cn.globalph.openadmin.server.service.persistence.validation.PropertyValidationResult;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.annotation.Resource;

/**
 * @author felix.wu
 */
@Component("blBasicPersistenceModule")
@Scope("prototype")
public class BasicPersistenceModule implements PersistenceModule, RecordHelper, ApplicationContextAware {
    
    private static final Log LOG = LogFactory.getLog(BasicPersistenceModule.class);

    public static final String MAIN_ENTITY_NAME_PROPERTY = "MAIN_ENTITY_NAME";
    public static final String ALTERNATE_ID_PROPERTY = "ALTERNATE_ID";

    protected ApplicationContext applicationContext;
    protected PersistenceOperationExecutor persistenceOperationExecutor;

    @Resource(name = "blEntityValidatorService")
    protected EntityValidatorService entityValidatorService;

    @Resource(name="blPersistenceProviders")
    protected List<FieldPersistenceProvider> fieldPersistenceProviders = new ArrayList<FieldPersistenceProvider>();
    
    @Resource(name="blPopulateValueRequestValidators")
    protected List<PopulateValueRequestValidator> populateValidators;

    @Resource(name= "blDefaultFieldPersistenceProvider")
    protected FieldPersistenceProvider defaultFieldPersistenceProvider;

    @Resource(name="blCriteriaTranslator")
    protected CriteriaTranslator criteriaTranslator;

    @Resource(name="blRestrictionFactory")
    protected RestrictionFactory restrictionFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean isCompatible(OperationScope operationType) {
        return OperationScope.BASIC == operationType || OperationScope.NONDESTRUCTIVEREMOVE == operationType;
    }

    @Override
    public FieldManager getFieldManager() {
        return persistenceOperationExecutor.getDynamicEntityDao().getFieldManager();
    }
    
    @Override
    public DecimalFormat getDecimalFormatter()  {
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.CHINA);
        format.applyPattern("0.########");
        return format;
    }

    @Override
    public SimpleDateFormat getSimpleDateFormatter() {
        return FormatUtil.getDateFormat();
    }

    protected Map<String, FieldMetadata> filterOutCollectionMetadata(Map<String, FieldMetadata> metadata) {
        if (metadata == null) {
            return null;
        }
        Map<String, FieldMetadata> newMap = new HashMap<String, FieldMetadata>();
        for (Map.Entry<String, FieldMetadata> entry : metadata.entrySet()) {
            if (entry.getValue() instanceof BasicFieldMetadata) {
                newMap.put(entry.getKey(), entry.getValue());
            }
        }

        return newMap;
    }

    protected Class<?> getBasicBroadleafType(SupportedFieldType fieldType) {
        Class<?> response;
        switch (fieldType) {
            case BOOLEAN:
                response = Boolean.TYPE;
                break;
            case DATE:
                response = Date.class;
                break;
            case DECIMAL:
                response = BigDecimal.class;
                break;
            case MONEY:
                response = Money.class;
                break;
            case INTEGER:
                response = Integer.TYPE;
                break;
            case UNKNOWN:
                response = null;
                break;
            default:
                response = String.class;
                break;
        }

        return response;
    }

    @Override
    public Serializable createPopulatedInstance(Serializable instance, Entity entity, 
            Map<String, FieldMetadata> unfilteredProperties, Boolean setId) throws ValidationException {
        return createPopulatedInstance(instance, entity, unfilteredProperties, setId, true);
    }

    @Override
    public Serializable createPopulatedInstance(Serializable instance, Entity entity, 
            Map<String, FieldMetadata> unfilteredProperties, Boolean setId, Boolean validateUnsubmittedProperties) throws ValidationException {
        Map<String, FieldMetadata> mergedProperties = filterOutCollectionMetadata(unfilteredProperties);
        FieldManager fieldManager = getFieldManager();
        boolean handled = false;
        for (FieldPersistenceProvider fieldPersistenceProvider : fieldPersistenceProviders) {
            FieldProviderResponse response = fieldPersistenceProvider.filterProperties(new AddFilterPropertiesRequest(entity), unfilteredProperties);
            if (FieldProviderResponse.NOT_HANDLED != response) {
                handled = true;
            }
            if (FieldProviderResponse.HANDLED_BREAK == response) {
                break;
            }
        }
        if (!handled) {
            defaultFieldPersistenceProvider.filterProperties(new AddFilterPropertiesRequest(entity), unfilteredProperties);
        }
        try {
            for (Property property : entity.getProperties()) {
                BasicFieldMetadata metadata = (BasicFieldMetadata) mergedProperties.get(property.getName());
                Class<?> returnType;
                if (!property.getName().contains(FieldManager.MAPFIELDSEPARATOR) && !property.getName().startsWith("__")) {
                    Field field = fieldManager.getField(instance.getClass(), property.getName());
                    if (field == null) {
                        LOG.debug("Unable to find a bean property for the reported property: " + property.getName() + ". Ignoring property.");
                        continue;
                    }
                    returnType = field.getType();
                } else {
                    if (metadata == null) {
                        LOG.debug("Unable to find a metadata property for the reported property: " + property.getName() + ". Ignoring property.");
                        continue;
                    }
                    returnType = getMapFieldType(instance, fieldManager, property);
                    if (returnType == null) {
                        returnType = getBasicBroadleafType(metadata.getFieldType());
                    }
                }
                if (returnType == null) {
                    throw new IllegalAccessException("Unable to determine the value type for the property ("+property.getName()+")");
                }
                String value = property.getValue();
                if (metadata != null) {
                    Boolean mutable = metadata.getMutable();
                    Boolean readOnly = metadata.getReadOnly();

                    if (metadata.getFieldType().equals(SupportedFieldType.BOOLEAN)) {
                        if (value == null) {
                            value = "false";
                        }
                    }

                    if ((mutable == null || mutable) && (readOnly == null || !readOnly)) {
                        if (value != null) {
                            handled = false;
                            PopulateValueRequest request = new PopulateValueRequest(setId,
                                    fieldManager, property, metadata, returnType, value, persistenceOperationExecutor, this);
                            
                            boolean attemptToPopulate = true;
                            for (PopulateValueRequestValidator validator : populateValidators) {
                                PropertyValidationResult validationResult = validator.validate(request, instance);
                                if (!validationResult.isValid()) {
                                    entity.addValidationError(property.getName(), validationResult.getErrorMessage());
                                    attemptToPopulate = false;
                                }
                            }
                            
                            if (attemptToPopulate) {
                                for (FieldPersistenceProvider fieldPersistenceProvider : fieldPersistenceProviders) {
                                    FieldProviderResponse response = fieldPersistenceProvider.populateValue(request, instance);
                                    if (FieldProviderResponse.NOT_HANDLED != response) {
                                        handled = true;
                                    }
                                    if (FieldProviderResponse.HANDLED_BREAK == response) {
                                        break;
                                    }
                                }
                                if (!handled) {
                                    defaultFieldPersistenceProvider.populateValue(new PopulateValueRequest(setId,
                                            fieldManager, property, metadata, returnType, value, persistenceOperationExecutor, this), instance);
                                }
                            }
                        } else {
                            try {
                                if (fieldManager.getFieldValue(instance, property.getName()) != null && (metadata.getFieldType() != SupportedFieldType.ID || setId) && metadata.getFieldType() != SupportedFieldType.PASSWORD) {
                                    if (fieldManager.getFieldValue(instance, property.getName()) != null) {
                                        property.setIsDirty(true);
                                    }
                                    fieldManager.setFieldValue(instance, property.getName(), null);
                                }
                            } catch (FieldNotAvailableException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    }
                }
            }
            validate(entity, instance, mergedProperties, validateUnsubmittedProperties);
            //if validation failed, refresh the current instance so that none of the changes will be persisted
            if (entity.isValidationFailure()) {
                //only refresh the instance if it was managed to begin with
                if (persistenceOperationExecutor.getDynamicEntityDao().getStandardEntityManager().contains(instance)) {
                    persistenceOperationExecutor.getDynamicEntityDao().refresh(instance);
                }
                
                //re-initialize the valid properties for the entity in order to deal with the potential of not
                //completely sending over all checkbox/radio fields
                List<Serializable> entityList = new ArrayList<Serializable>(1);
                entityList.add(instance);
                Entity invalid = getRecords(mergedProperties, entityList, null, null)[0];
                invalid.setPropertyValidationErrors(entity.getPropertyValidationErrors());
                invalid.overridePropertyValues(entity);

                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, List<String>> entry : invalid.getPropertyValidationErrors().entrySet()) {
                    Iterator<String> itr = entry.getValue().iterator();
                    while(itr.hasNext()) {
                        sb.append(entry.getKey());
                        sb.append(" : ");
                        sb.append(itr.next());
                        if (itr.hasNext()) {
                            sb.append(" / ");
                        }
                    }
                }
                
                throw new ValidationException(invalid, "The entity has failed validation - " + sb.toString());
            }
            else {
                fieldManager.persistMiddleEntities();
            }
        } catch (IllegalAccessException e) {
            throw new PersistenceOperationException(e);
        } catch (InstantiationException e) {
            throw new PersistenceOperationException(e);
        }
        return instance;
    }

    protected Class<?> getMapFieldType(Serializable instance, FieldManager fieldManager, Property property) {
        Class<?> returnType = null;
        Field field = fieldManager.getField(instance.getClass(), property.getName().substring(0, property.getName().indexOf(FieldManager.MAPFIELDSEPARATOR)));
        java.lang.reflect.Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Class<?> clazz = (Class<?>) pType.getActualTypeArguments()[1];
            Class<?>[] entities = persistenceOperationExecutor.getDynamicEntityDao().getAllPolymorphicEntitiesFromCeiling(clazz);
            if (!ArrayUtils.isEmpty(entities)) {
                returnType = entities[entities.length-1];
            }
        }
        return returnType;
    }

    @Override
    public Entity getRecord(Map<String, FieldMetadata> primaryMergedProperties, Serializable record, Map<String, FieldMetadata> alternateMergedProperties, String pathToTargetObject) {
        List<Serializable> records = new ArrayList<Serializable>(1);
        records.add(record);
        Entity[] productEntities = getRecords(primaryMergedProperties, records, alternateMergedProperties,
                pathToTargetObject);
        return productEntities[0];
    }

    @Override
    public Entity getRecord(Class<?> ceilingEntityClass, PersistenceAssociation persistencePerspective, Serializable record) {
        Map<String, FieldMetadata> mergedProperties = getSimpleMergedProperties(ceilingEntityClass.getName(), persistencePerspective);
        return getRecord(mergedProperties, record, null, null);
    }

    @Override
    public Entity[] getRecords(Class<?> ceilingEntityClass, PersistenceAssociation persistencePerspective, List<? extends Serializable> records) {
        Map<String, FieldMetadata> mergedProperties = getSimpleMergedProperties(ceilingEntityClass.getName(), persistencePerspective);
        return getRecords(mergedProperties, records, null, null);
    }

    @Override
    public Map<String, FieldMetadata> getSimpleMergedProperties(String entityName, PersistenceAssociation persistencePerspective) {
        return persistenceOperationExecutor.getDynamicEntityDao().getSimpleMergedProperties(entityName, persistencePerspective);
    }

    @Override
    public Entity[] getRecords(Map<String, FieldMetadata> primaryMergedProperties, List<? extends Serializable> records) {
        return getRecords(primaryMergedProperties, records, null, null);
    }

    @Override
    public Entity[] getRecords(Map<String, FieldMetadata> primaryUnfilteredMergedProperties, List<? extends Serializable> records, 
    		                   Map<String, FieldMetadata> alternateUnfilteredMergedProperties, String pathToTargetObject) {
        Map<String, FieldMetadata> primaryMergedProperties = filterOutCollectionMetadata(primaryUnfilteredMergedProperties);
        Map<String, FieldMetadata> alternateMergedProperties = filterOutCollectionMetadata(alternateUnfilteredMergedProperties);
        Entity[] entities = new Entity[records.size()];
        int j = 0;
        for (Serializable recordEntity : records) {
            Serializable entity;
            if (pathToTargetObject != null) {
                try {
                    entity = (Serializable) getFieldManager().getFieldValue(recordEntity, pathToTargetObject);
                } catch (Exception e) {
                    throw new PersistenceOperationException(e);
                }
            } else {
                entity = recordEntity;
            }
            Entity entityItem = new Entity();
            entityItem.setType(new String[]{entity.getClass().getName()});
            entities[j] = entityItem;

            List<Property> props = new ArrayList<Property>(primaryMergedProperties.size());
            extractPropertiesFromPersistentEntity(primaryMergedProperties, entity, props);
            if (alternateMergedProperties != null) {
                extractPropertiesFromPersistentEntity(alternateMergedProperties, recordEntity, props);
            }
            
            // Try to add the "main name" property. Log a debug message if we can't
            try {
                Property p = new Property();
                p.setName(MAIN_ENTITY_NAME_PROPERTY);
                String mainEntityName = (String) MethodUtils.invokeMethod(entity, "getMainEntityName");
                p.setValue(mainEntityName);
                props.add(p);
            } catch (Exception e) {
                LOG.debug(String.format("Could not execute the getMainEntityName() method for [%s]", 
                        entity.getClass().getName()), e);
            }
            
            // Try to add the alternate id property if available
            if (alternateMergedProperties != null) {
                for (Entry<String, FieldMetadata> entry : alternateMergedProperties.entrySet()) {
                    if (entry.getValue() instanceof BasicFieldMetadata) {
                        if (((BasicFieldMetadata) entry.getValue()).getFieldType() == SupportedFieldType.ID) {
                            Map<String, FieldMetadata> alternateOnEntity = new HashMap<String, FieldMetadata>();
                            alternateOnEntity.put(entry.getKey(), entry.getValue());
                            List<Property> props2 = new ArrayList<Property>();
                            extractPropertiesFromPersistentEntity(alternateOnEntity, recordEntity, props2);
                            if (props2.size() == 1) {
                                Property alternateIdProp = props2.get(0);
                                alternateIdProp.setName(ALTERNATE_ID_PROPERTY);
                                props.add(alternateIdProp);
                            }
                        }
                    }
                }
            }
            
            Property[] properties = new Property[props.size()];
            properties = props.toArray(properties);
            entityItem.setProperties(properties);
            j++;
        }

        return entities;
    }

    protected void extractPropertiesFromPersistentEntity(Map<String, FieldMetadata> mergedProperties, Serializable entity, List<Property> props) {
        FieldManager fieldManager = getFieldManager();
        try {
            if (entity instanceof AdminMainEntity) {
                //Create an invisible property for the admin main entity name, if applicable.
                //This is useful for ToOneLookups if that ToOneLookup uses AdminMainEntity to drive
                //its display name.
                try {
                    Property propertyItem = new Property();
                    propertyItem.setName(AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY);
                    propertyItem.setValue(((AdminMainEntity) entity).getMainEntityName());
                    props.add(propertyItem);
                } catch (Exception e) {
                    //do nothing here except for not add the property. Exceptions could occur when there is a validation
                    //issue and some properties/relationships that are used for gleaning the main entity name end up
                    //not being set
                }
            }
            for (Entry<String, FieldMetadata> entry : mergedProperties.entrySet()) {
                String property = entry.getKey();
                BasicFieldMetadata metadata = (BasicFieldMetadata) entry.getValue();
                if (Class.forName(metadata.getInheritedFromType()).isAssignableFrom(entity.getClass()) || entity.getClass().isAssignableFrom(Class.forName(metadata.getInheritedFromType()))) {
                    boolean proceed = true;
                    if (property.contains(".")) {
                        StringTokenizer tokens = new StringTokenizer(property, ".");
                        Object testObject = entity;
                        while (tokens.hasMoreTokens()) {
                            String token = tokens.nextToken();
                            if (tokens.hasMoreTokens()) {
                                try {
                                    testObject = fieldManager.getFieldValue(testObject, token);
                                } catch (FieldNotAvailableException e) {
                                    proceed = false;
                                    break;
                                }
                                if (testObject == null) {
                                    Property propertyItem = new Property();
                                    propertyItem.setName(property);
                                    if (props.contains(propertyItem)) {
                                        proceed = false;
                                        break;
                                    }
                                    propertyItem.setValue(null);
                                    props.add(propertyItem);
                                    proceed = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (!proceed) {
                        continue;
                    }
                    
                    boolean isFieldAccessible = true;
                    Object value = null;
                    try {
                        value = fieldManager.getFieldValue(entity, property);
                    } catch (FieldNotAvailableException e) {
                        isFieldAccessible = false;
                    }
                    checkField:
                    {
                        if (isFieldAccessible) {
                            Property propertyItem = new Property();
                            propertyItem.setName(property);
                            if (props.contains(propertyItem)) {
                                continue;
                            }
                            props.add(propertyItem);
                            String displayVal = propertyItem.getDisplayValue();
                            boolean handled = false;
                            for (FieldPersistenceProvider fieldPersistenceProvider : fieldPersistenceProviders) {
                                FieldProviderResponse response = fieldPersistenceProvider.extractValue(
                                        new ExtractValueRequest(props, fieldManager, metadata, value, displayVal, 
                                                persistenceOperationExecutor, this, entity), propertyItem);
                                if (FieldProviderResponse.NOT_HANDLED != response) {
                                    handled = true;
                                }
                                if (FieldProviderResponse.HANDLED_BREAK == response) {
                                    break;
                                }
                            }
                            if (!handled) {
                                defaultFieldPersistenceProvider.extractValue(
                                        new ExtractValueRequest(props, fieldManager, metadata, value, displayVal, 
                                                persistenceOperationExecutor, this, entity), propertyItem);
                            }
                            break checkField;
                        }
                        //try a direct property acquisition via reflection
                        try {
                            String strVal = null;
                            Method method;
                            try {
                                //try a 'get' prefixed mutator first
                                String temp = "get" + property.substring(0, 1).toUpperCase() + property.substring(1, property.length());
                                method = entity.getClass().getMethod(temp, new Class[]{});
                            } catch (NoSuchMethodException e) {
                                method = entity.getClass().getMethod(property, new Class[]{});
                            }
                            value = method.invoke(entity, new String[]{});
                            Property propertyItem = new Property();
                            propertyItem.setName(property);
                            if (props.contains(propertyItem)) {
                                continue;
                            }
                            props.add(propertyItem);
                            if (value == null) {
                                strVal = null;
                            } else {
                                if (Date.class.isAssignableFrom(value.getClass())) {
                                    strVal = getSimpleDateFormatter().format((Date) value);
                                } else if (Timestamp.class.isAssignableFrom(value.getClass())) {
                                    strVal = getSimpleDateFormatter().format(new Date(((Timestamp) value).getTime()));
                                } else if (Calendar.class.isAssignableFrom(value.getClass())) {
                                    strVal = getSimpleDateFormatter().format(((Calendar) value).getTime());
                                } else if (Double.class.isAssignableFrom(value.getClass())) {
                                    strVal = getDecimalFormatter().format(value);
                                } else if (BigDecimal.class.isAssignableFrom(value.getClass())) {
                                    strVal = getDecimalFormatter().format(value);
                                } else {
                                    strVal = value.toString();
                                }
                            }
                            propertyItem.setValue(strVal);
                        } catch (NoSuchMethodException e) {
                            LOG.debug("Unable to find a specified property in the entity: " + property);
                            //do nothing - this property is simply not in the bean
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new PersistenceOperationException(e);
        } catch (IllegalAccessException e) {
            throw new PersistenceOperationException(e);
        } catch (InvocationTargetException e) {
            throw new PersistenceOperationException(e);
        }
    }
    
    @Override
    public String getStringValueFromGetter(Serializable instance, String propertyName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object value = PropertyUtils.getProperty(instance, propertyName);
        return formatValue(value);
    }
    
    @Override
    public String formatValue(Object value) {
        String strVal;
        if (value == null) {
            strVal = null;
        } else {
            if (Date.class.isAssignableFrom(value.getClass())) {
                strVal = getSimpleDateFormatter().format((Date) value);
            } else if (Timestamp.class.isAssignableFrom(value.getClass())) {
                strVal = getSimpleDateFormatter().format(new Date(((Timestamp) value).getTime()));
            } else if (Calendar.class.isAssignableFrom(value.getClass())) {
                strVal = getSimpleDateFormatter().format(((Calendar) value).getTime());
            } else if (Double.class.isAssignableFrom(value.getClass())) {
                strVal = getDecimalFormatter().format(value);
            } else if (BigDecimal.class.isAssignableFrom(value.getClass())) {
                strVal = getDecimalFormatter().format(value);
            } else {
                strVal = value.toString();
            }
        }
        return strVal;
    }

    protected EntityResult update(PersistencePackage persistencePackage, Object primaryKey, boolean includeRealEntity) throws ServiceException {
        EntityResult entityResult = new EntityResult();
        Entity entity = persistencePackage.getEntity();
        PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
        ForeignKey foreignKey = (ForeignKey) persistencePerspective.getPersistenceAssociationItems().get(PersistenceAssociationItemType.FOREIGNKEY);
        if (foreignKey != null && !foreignKey.getMutable()) {
            throw new SecurityServiceException("Entity not mutable");
        }
        try {
            Class<?>[] entities = persistenceOperationExecutor.getPolymorphicEntities(persistencePackage.getRootEntityClassname());
            Map<String, FieldMetadata> mergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getMergedProperties(
                persistencePackage.getRootEntityClassname(),
                entities,
                foreignKey,
                persistencePerspective.getAdditionalNonPersistentProperties(),
                persistencePerspective.getAdditionalForeignKeys(),
                MergedPropertyType.PRIMARY,
                persistencePerspective.getPopulateToOneFields(),
                persistencePerspective.getIncludeFields(),
                persistencePerspective.getExcludeFields(),
                persistencePerspective.getConfigurationKey(),
                ""
            );
            if (primaryKey == null) {
                primaryKey = getPrimaryKey(entity, mergedProperties);
            }
            Serializable instance = persistenceOperationExecutor.getDynamicEntityDao().retrieve(Class.forName(entity.getType()[0]), primaryKey);

            Assert.isTrue(instance != null, "Entity not found");

            instance = createPopulatedInstance(instance, entity, mergedProperties, false, persistencePackage.isValidateUnsubmittedProperties());
            if (!entity.isValidationFailure()) {
                instance = persistenceOperationExecutor.getDynamicEntityDao().merge(instance);
                if (includeRealEntity) {
                    entityResult.setEntityBackingObject(instance);
                }

                List<Serializable> entityList = new ArrayList<Serializable>(1);
                entityList.add(instance);

                entity = getRecords(mergedProperties, entityList, null, null)[0];
                entityResult.setEntity(entity);
                return entityResult;
            } else {
                entityResult.setEntity(entity);
                return entityResult;
            }
        } catch (Exception e) {
            throw new ServiceException("Problem updating entity : " + e.getMessage(), e);
        }
    }

    @Override
    public Object getPrimaryKey(Entity entity, Map<String, FieldMetadata> mergedUnfilteredProperties) {
        Map<String, FieldMetadata> mergedProperties = filterOutCollectionMetadata(mergedUnfilteredProperties);
        Object primaryKey = null;
        String idPropertyName = null;
        BasicFieldMetadata metaData = null;
        for (String property : mergedProperties.keySet()) {
            BasicFieldMetadata temp = (BasicFieldMetadata) mergedProperties.get(property);
            if (temp.getFieldType() == SupportedFieldType.ID && !property.contains(".")) {
                idPropertyName = property;
                metaData = temp;
                break;
            }
        }
        if (idPropertyName == null) {
            throw new RuntimeException("Could not find a primary key property in the passed entity with type: " + entity.getType()[0]);
        }
        for (Property property : entity.getProperties()) {
            if (property.getName().equals(idPropertyName)) {
                switch(metaData.getSecondaryType()) {
                case INTEGER:
                    primaryKey = (property.getValue() == null) ? null : Long.valueOf(property.getValue());
                    break;
                case STRING:
                    primaryKey = property.getValue();
                    break;
                }
                break;
            }
        }
        if (primaryKey == null) {
            throw new RuntimeException("Could not find the primary key property (" + idPropertyName + ") in the passed entity with type: " + entity.getType()[0]);
        }
        return primaryKey;
    }

    @Override
    public List<FilterMapping> getFilterMappings(PersistenceAssociation pa,
                                                 CriteriaTransferObject cto,
                                                 String rootClassname,
                                                 Map<String, FieldMetadata> mergedUnfilteredProperties,
                                                 RestrictionFactory customRestrictionFactory) {
        Map<String, FieldMetadata> mergedProperties = filterOutCollectionMetadata(mergedUnfilteredProperties);
        List<FilterMapping> filterMappings = new ArrayList<FilterMapping>();
        for (String propertyId : cto.getCriteriaMap().keySet()) {
            if ( mergedProperties.containsKey(propertyId) ) {
                boolean handled = false;
                AddSearchMappingRequest asmr = new AddSearchMappingRequest(pa, 
							                        cto,
							                        rootClassname, 
							                        mergedProperties,
							                        propertyId, 
							                        getFieldManager(), 
							                        this, 
							                        customRestrictionFactory==null?restrictionFactory:customRestrictionFactory);
                for (FieldPersistenceProvider fieldPersistenceProvider : fieldPersistenceProviders) {
                	FieldProviderResponse response = fieldPersistenceProvider.addSearchMapping(asmr, filterMappings);
                    if (FieldProviderResponse.NOT_HANDLED != response) {
                        handled = true;
                    }
                    if (FieldProviderResponse.HANDLED_BREAK == response) {
                        break;
                    }
                }
                if (!handled) {
                    defaultFieldPersistenceProvider.addSearchMapping(asmr, filterMappings);
                }
            }
        }
        return filterMappings;
    }

    @Override
    public List<FilterMapping> getFilterMappings(PersistenceAssociation pa,
                                                 CriteriaTransferObject cto,
                                                 String rootClassname,
                                                 Map<String, FieldMetadata> mergedUnfilteredProperties) {
        return getFilterMappings(pa, cto, rootClassname, mergedUnfilteredProperties, null);
    }

    @Override
    public void extractProperties(Class<?>[] inheritanceLine, Map<MergedPropertyType, Map<String, FieldMetadata>> mergedProperties, List<Property> properties) {
        extractPropertiesFromMetadata(inheritanceLine, mergedProperties.get(MergedPropertyType.PRIMARY), properties, false, MergedPropertyType.PRIMARY);
    }

    protected void extractPropertiesFromMetadata(Class<?>[] inheritanceLine, Map<String, FieldMetadata> mergedProperties, List<Property> properties, Boolean isHiddenOverride, MergedPropertyType type) {
        for (Map.Entry<String, FieldMetadata> entry : mergedProperties.entrySet()) {
            String property = entry.getKey();
            Property prop = new Property();
            FieldMetadata metadata = mergedProperties.get(property);
            prop.setName(property);
            Comparator<Property> comparator = new Comparator<Property>() {
                @Override
                public int compare(Property o1, Property o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            };
            Collections.sort(properties, comparator);
            int pos = Collections.binarySearch(properties, prop, comparator);
            if (pos >= 0 && MergedPropertyType.MAPSTRUCTUREKEY != type && MergedPropertyType.MAPSTRUCTUREVALUE != type) {
                logWarn: {
                    if ((metadata instanceof BasicFieldMetadata) && SupportedFieldType.ID.equals(((BasicFieldMetadata) metadata).getFieldType())) {
                        //don't warn for id field collisions, but still ignore the colliding fields
                        break logWarn;
                    }
                    LOG.warn("Detected a field name collision (" + metadata.getTargetClass() + "." + property + ") during inspection for the inheritance line starting with (" + inheritanceLine[0].getName() + "). Ignoring the additional field. This can occur most commonly when using the @AdminPresentationAdornedTargetCollection and the collection type and target class have field names in common. This situation should be avoided, as the system will strip the repeated fields, which can cause unpredictable behavior.");
                }
                continue;
            }
            properties.add(prop);
            prop.setFieldMetadata(metadata);
            if (isHiddenOverride && prop.getFieldMetadata() instanceof BasicFieldMetadata) {
                //this only makes sense for non collection types
                ((BasicFieldMetadata) prop.getFieldMetadata()).setVisibility(VisibilityEnum.HIDDEN_ALL);
            }
        }
    }

    @Override
    public void updateMergedProperties(PersistencePackage persistencePackage, Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties) throws ServiceException {
        String ceilingEntityClassname = persistencePackage.getRootEntityClassname();
        try {
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            Class<?>[] entities = persistenceOperationExecutor.getPolymorphicEntities(ceilingEntityClassname);
            Map<String, FieldMetadata> mergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getMergedProperties(
                ceilingEntityClassname,
                entities,
                (ForeignKey) persistencePerspective.getPersistenceAssociationItems().get(PersistenceAssociationItemType.FOREIGNKEY),
                persistencePerspective.getAdditionalNonPersistentProperties(),
                persistencePerspective.getAdditionalForeignKeys(),
                MergedPropertyType.PRIMARY,
                persistencePerspective.getPopulateToOneFields(),
                persistencePerspective.getIncludeFields(),
                persistencePerspective.getExcludeFields(),
                persistencePerspective.getConfigurationKey(),
                ""
            );
            allMergedProperties.put(MergedPropertyType.PRIMARY, mergedProperties);
        } catch (Exception e) {
            throw new ServiceException("Unable to fetch results for " + ceilingEntityClassname, e);
        }
    }

    @Override
    public EntityResult update(PersistencePackage persistencePackage, boolean includeRealEntityObject) throws ServiceException {
        return update(persistencePackage, null, true);
    }

    @Override
    public Entity update(PersistencePackage persistencePackage) throws ServiceException {
        EntityResult er = update(persistencePackage, null, false);
        return er.getEntity();
    }


    @Override
    public Entity add(PersistencePackage persistencePackage) throws ServiceException {
        EntityResult entityResult = add(persistencePackage, false);
        return entityResult.getEntity();
    }

    @Override
    public EntityResult add(PersistencePackage persistencePackage, boolean includeRealEntityObject) throws ServiceException {
        EntityResult entityResult = new EntityResult();
        Entity entity = persistencePackage.getEntity();
        PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
        ForeignKey foreignKey = (ForeignKey) persistencePerspective.getPersistenceAssociationItems().get(PersistenceAssociationItemType.FOREIGNKEY);
        if (foreignKey != null && !foreignKey.getMutable()) {
            throw new SecurityServiceException("Entity not mutable");
        }
        try {
            Class<?>[] entities = persistenceOperationExecutor.getPolymorphicEntities(persistencePackage.getRootEntityClassname());
            Map<String, FieldMetadata> mergedUnfilteredProperties = persistenceOperationExecutor.getDynamicEntityDao().getMergedProperties(
                persistencePackage.getRootEntityClassname(),
                entities,
                foreignKey,
                persistencePerspective.getAdditionalNonPersistentProperties(),
                persistencePerspective.getAdditionalForeignKeys(),
                MergedPropertyType.PRIMARY,
                persistencePerspective.getPopulateToOneFields(),
                persistencePerspective.getIncludeFields(),
                persistencePerspective.getExcludeFields(),
                persistencePerspective.getConfigurationKey(),
                ""
            );
            Map<String, FieldMetadata> mergedProperties = filterOutCollectionMetadata(mergedUnfilteredProperties);

            String idProperty = null;
            for (String property : mergedProperties.keySet()) {
                if (((BasicFieldMetadata) mergedProperties.get(property)).getFieldType() == SupportedFieldType.ID) {
                    idProperty = property;
                    break;
                }
            }
            if (idProperty == null) {
                throw new RuntimeException("Could not find a primary key property in the passed entity with type: " + entity.getType()[0]);
            }
            Object primaryKey = null;
            try {
                primaryKey = getPrimaryKey(entity, mergedProperties);
            } catch (Exception e) {
                //don't do anything - this is a valid case
            }
            if (primaryKey == null) {
                Serializable instance = (Serializable) Class.forName(entity.getType()[0]).newInstance();
                instance = createPopulatedInstance(instance, entity, mergedProperties, false);

                instance = persistenceOperationExecutor.getDynamicEntityDao().merge(instance);
                if (includeRealEntityObject) {
                    entityResult.setEntityBackingObject(instance);
                }
                List<Serializable> entityList = new ArrayList<Serializable>(1);
                entityList.add(instance);

                entity = getRecords(mergedProperties, entityList, null, null)[0];
                entityResult.setEntity(entity);
                return entityResult;
            } else {
                return update(persistencePackage, primaryKey, includeRealEntityObject);
            }
        } catch (Exception e) {
            throw new ServiceException("Problem adding new entity : " + e.getMessage(), e);
        }
    }

    @Override
    public void remove(PersistencePackage persistencePackage) throws ServiceException {
        Entity entity = persistencePackage.getEntity();
        PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
        ForeignKey foreignKey = (ForeignKey) persistencePerspective.getPersistenceAssociationItems().get(PersistenceAssociationItemType.FOREIGNKEY);
        if (foreignKey != null && !foreignKey.getMutable()) {
            throw new SecurityServiceException("Entity not mutable");
        }
        try {
            Class<?>[] entities = persistenceOperationExecutor.getPolymorphicEntities(persistencePackage.getRootEntityClassname());
            Map<String, FieldMetadata> mergedUnfilteredProperties = persistenceOperationExecutor.getDynamicEntityDao().getMergedProperties(
                persistencePackage.getRootEntityClassname(),
                entities,
                foreignKey,
                persistencePerspective.getAdditionalNonPersistentProperties(),
                persistencePerspective.getAdditionalForeignKeys(),
                MergedPropertyType.PRIMARY,
                persistencePerspective.getPopulateToOneFields(),
                persistencePerspective.getIncludeFields(),
                persistencePerspective.getExcludeFields(),
                persistencePerspective.getConfigurationKey(),
                ""
            );
            Map<String, FieldMetadata> mergedProperties = filterOutCollectionMetadata(mergedUnfilteredProperties);
            Object primaryKey = getPrimaryKey(entity, mergedProperties);
            Serializable instance = persistenceOperationExecutor.getDynamicEntityDao().retrieve(Class.forName(entity.getType()[0]), primaryKey);

            Assert.isTrue(instance != null, "Entity not found");

            switch (persistencePerspective.getPersistenceOperationType().getRemoveType()) {
                case NONDESTRUCTIVEREMOVE:
                    for (Property property : entity.getProperties()) {
                        String originalPropertyName = property.getName();
                        FieldManager fieldManager = getFieldManager();
                        if (fieldManager.getField(instance.getClass(), property.getName()) == null) {
                            LOG.debug("Unable to find a bean property for the reported property: " + originalPropertyName + ". Ignoring property.");
                            continue;
                        }
                        if (SupportedFieldType.FOREIGN_KEY == ((BasicFieldMetadata) mergedProperties.get(originalPropertyName)).getFieldType()) {
                            String value = property.getValue();
                            Serializable foreignInstance = persistenceOperationExecutor.getDynamicEntityDao().retrieve(Class.forName(foreignKey.getForeignKeyClass()), Long.valueOf(value));
                            Collection collection = (Collection) fieldManager.getFieldValue(foreignInstance, foreignKey.getOriginatingField());
                            collection.remove(instance);
                            break;
                        }
                    }
                    break;
                case BASIC:
                    persistenceOperationExecutor.getDynamicEntityDao().remove(instance);
                    break;
            }
        } catch (Exception e) {
            throw new ServiceException("Problem removing entity : " + e.getMessage(), e);
        }
    }

    public Map<String, FieldMetadata> getMergedProperties(PersistencePackage persistencePackage,
            CriteriaTransferObject cto) throws ServiceException {
        PersistenceAssociation pa = persistencePackage.getPersistenceAssociation();
        String rootEntityClassname = persistencePackage.getRootEntityClassname();

        if ( StringUtils.isEmpty(persistencePackage.getFetchTypeClassname())) {
            persistencePackage.setFetchTypeClassname(rootEntityClassname);
        }

        try {
            Class<?>[] entities = persistenceOperationExecutor.getDynamicEntityDao().getAllPolymorphicEntitiesFromCeiling(Class.forName(rootEntityClassname));

            Map<String, FieldMetadata> mergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getMergedProperties(
                rootEntityClassname,
                entities,
                (ForeignKey) pa.getPersistenceAssociationItems().get(PersistenceAssociationItemType.FOREIGNKEY),
                pa.getAdditionalNonPersistentProperties(),
                pa.getAdditionalForeignKeys(),
                MergedPropertyType.PRIMARY,
                pa.getPopulateToOneFields(),
                pa.getIncludeFields(),
                pa.getExcludeFields(),
                pa.getConfigurationKey(),
                ""
            );

            return mergedProperties;
        } catch (Exception e) {
            throw new ServiceException("Unable to fetch results for " + rootEntityClassname, e);
        }
    }

    @Override
    public DynamicResultSet fetch(PersistencePackage pp, CriteriaTransferObject cto) throws ServiceException {
        Entity[] payload;
        int totalRecords;
        PersistenceAssociation pa = pp.getPersistenceAssociation();
        try {
            Map<String, FieldMetadata> mergedProperties = getMergedProperties(pp, cto);

            List<FilterMapping> filterMappings = getFilterMappings(pa, cto, pp
                    .getFetchTypeClassname(), mergedProperties);
            
            if (CollectionUtils.isNotEmpty(cto.getAdditionalFilterMappings())) {
                filterMappings.addAll(cto.getAdditionalFilterMappings());
            }

            List<Serializable> records = getPersistentRecords(pp.getFetchTypeClassname(), filterMappings, cto.getFirstResult(), cto.getMaxResults());
            payload = getRecords(mergedProperties, records, null, null);
            totalRecords = getTotalRecords(pp.getFetchTypeClassname(), filterMappings);
        } catch (Exception e) {
            throw new ServiceException("Unable to fetch results for " + pp.getRootEntityClassname(), e);
        }

        return new DynamicResultSet(null, payload, totalRecords);
    }

    @Override
    public Integer getTotalRecords(String ceilingEntity, List<FilterMapping> filterMappings) {
        return ((Long) criteriaTranslator.translateCountQuery(persistenceOperationExecutor.getDynamicEntityDao(),
                ceilingEntity, filterMappings).getSingleResult()).intValue();
    }

    @Override
    public Serializable getMaxValue(String ceilingEntity, List<FilterMapping> filterMappings, String maxField) {
        return criteriaTranslator.translateMaxQuery(persistenceOperationExecutor.getDynamicEntityDao(),
                        ceilingEntity, filterMappings, maxField).getSingleResult();
    }

    @Override
    public List<Serializable> getPersistentRecords(String ceilingEntity, List<FilterMapping> filterMappings, Integer firstResult, Integer maxResults) {
        return criteriaTranslator.translateQuery(persistenceOperationExecutor.getDynamicEntityDao(), 
        		                                 ceilingEntity, 
        		                                 filterMappings, 
        		                                 firstResult, 
        		                                 maxResults).getResultList();
    }

    @Override
    public boolean validate(Entity entity, Serializable populatedInstance, Map<String, FieldMetadata> mergedProperties) {
        return validate(entity, populatedInstance, mergedProperties, true);
    }

    @Override
    public boolean validate(Entity entity, Serializable populatedInstance, Map<String, FieldMetadata> mergedProperties, 
            boolean validateUnsubmittedProperties) {
        entityValidatorService.validate(entity, populatedInstance, mergedProperties, this, validateUnsubmittedProperties);
        return !entity.isValidationFailure();
    }

    @Override
    public void setPersistenceManager(PersistenceOperationExecutor persistenceManager) {
        this.persistenceOperationExecutor = persistenceManager;
    }

    @Override
    public PersistenceModule getCompatibleModule(OperationScope operationType) {
        return ((InspectHelper) persistenceOperationExecutor).getCompatibleModule(operationType);
    }

    public FieldPersistenceProvider getDefaultFieldPersistenceProvider() {
        return defaultFieldPersistenceProvider;
    }

    public void setDefaultFieldPersistenceProvider(FieldPersistenceProvider defaultFieldPersistenceProvider) {
        this.defaultFieldPersistenceProvider = defaultFieldPersistenceProvider;
    }

    public List<FieldPersistenceProvider> getFieldPersistenceProviders() {
        return fieldPersistenceProviders;
    }

    public void setFieldPersistenceProviders(List<FieldPersistenceProvider> fieldPersistenceProviders) {
        this.fieldPersistenceProviders = fieldPersistenceProviders;
    }

    public CriteriaTranslator getCriteriaTranslator() {
        return criteriaTranslator;
    }

    public void setCriteriaTranslator(CriteriaTranslator criteriaTranslator) {
        this.criteriaTranslator = criteriaTranslator;
    }

    public EntityValidatorService getEntityValidatorService() {
        return entityValidatorService;
    }

    public void setEntityValidatorService(EntityValidatorService entityValidatorService) {
        this.entityValidatorService = entityValidatorService;
    }

    public RestrictionFactory getRestrictionFactory() {
        return restrictionFactory;
    }

    public void setRestrictionFactory(RestrictionFactory restrictionFactory) {
        this.restrictionFactory = restrictionFactory;
    }

    public PersistenceOperationExecutor getPersistenceManager() {
        return persistenceOperationExecutor;
    }

}

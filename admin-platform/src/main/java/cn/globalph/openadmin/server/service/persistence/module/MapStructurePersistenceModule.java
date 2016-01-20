package cn.globalph.openadmin.server.service.persistence.module;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.exception.SecurityServiceException;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.client.OperationScope;
import cn.globalph.common.presentation.client.PersistenceAssociationItemType;
import cn.globalph.common.sandbox.SandBoxHelper;
import cn.globalph.common.value.ValueAssignable;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.ForeignKey;
import cn.globalph.openadmin.dto.MapStructure;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.dto.SimpleValueMapStructure;
import cn.globalph.openadmin.server.service.persistence.module.criteria.FilterMapping;
import cn.globalph.openadmin.server.service.persistence.validation.RequiredPropertyValidator;

import org.hibernate.mapping.PersistentClass;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 
 * @author felix.wu
 *
 */
@Component("blMapStructurePersistenceModule")
@Scope("prototype")
public class MapStructurePersistenceModule extends BasicPersistenceModule {

    @Resource(name="blSandBoxHelper")
    protected SandBoxHelper sandBoxHelper;

    private static final Log LOG = LogFactory.getLog(MapStructurePersistenceModule.class);
    
    @Override
    public boolean isCompatible(OperationScope operationType) {
        return OperationScope.MAP.equals(operationType);
    }
    
    @Override
    public void extractProperties(Class<?>[] inheritanceLine, Map<MergedPropertyType, Map<String, FieldMetadata>> mergedProperties, List<Property> properties) throws NumberFormatException {
        if (mergedProperties.get(MergedPropertyType.MAPSTRUCTUREKEY) != null) {
            extractPropertiesFromMetadata(inheritanceLine, mergedProperties.get(MergedPropertyType.MAPSTRUCTUREKEY), properties, false, MergedPropertyType.MAPSTRUCTUREKEY);
        }
        if (mergedProperties.get(MergedPropertyType.MAPSTRUCTUREVALUE) != null) {
            extractPropertiesFromMetadata(inheritanceLine, mergedProperties.get(MergedPropertyType.MAPSTRUCTUREVALUE), properties, false, MergedPropertyType.MAPSTRUCTUREVALUE);
        }
    }

    @Override
    public void updateMergedProperties(PersistencePackage persistencePackage, Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        try {   
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            MapStructure mapStructure = (MapStructure) persistencePerspective.getPersistenceAssociationItems().get(PersistenceAssociationItemType.MAPSTRUCTURE);
            if (mapStructure != null) {
                PersistentClass persistentClass = persistenceOperationExecutor.getDynamicEntityDao().getPersistentClass(mapStructure.getKeyClassName());
                Map<String, FieldMetadata> keyMergedProperties;
                if (persistentClass == null) {
                    keyMergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getPropertiesForPrimitiveClass(
                        mapStructure.getKeyPropertyName(), 
                        mapStructure.getKeyPropertyFriendlyName(),
                        Class.forName(mapStructure.getKeyClassName()), 
                        Class.forName(ceilingEntityFullyQualifiedClassname), 
                        MergedPropertyType.MAPSTRUCTUREKEY
                    );
                } else {
                    keyMergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getMergedProperties(
                        mapStructure.getKeyClassName(), 
                        new Class[]{Class.forName(mapStructure.getKeyClassName())},
                        null, 
                        new String[]{}, 
                        new ForeignKey[]{}, 
                        MergedPropertyType.MAPSTRUCTUREKEY,
                        persistencePerspective.getPopulateToOneFields(), 
                        persistencePerspective.getIncludeFields(), 
                        persistencePerspective.getExcludeFields(),
                        persistencePerspective.getConfigurationKey(),
                        ""
                    );
                }
                allMergedProperties.put(MergedPropertyType.MAPSTRUCTUREKEY, keyMergedProperties);
                
                persistentClass = persistenceOperationExecutor.getDynamicEntityDao().getPersistentClass(mapStructure.getValueClassName());
                Map<String, FieldMetadata> valueMergedProperties;
                if (persistentClass == null) {
                    if (!SimpleValueMapStructure.class.isAssignableFrom(mapStructure.getClass())) {
                        throw new IllegalStateException("The map structure was determined to not be a simple value, but the system was unable to identify the entity designated for the map structure value(" + mapStructure.getValueClassName() + ")");
                    }
                    valueMergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getPropertiesForPrimitiveClass(
                        ((SimpleValueMapStructure) mapStructure).getValuePropertyName(), 
                        ((SimpleValueMapStructure) mapStructure).getValuePropertyFriendlyName(),
                        Class.forName(mapStructure.getValueClassName()), 
                        Class.forName(ceilingEntityFullyQualifiedClassname), 
                        MergedPropertyType.MAPSTRUCTUREVALUE
                    );
                } else {
                    valueMergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getMergedProperties(
                        mapStructure.getValueClassName(), 
                        new Class[]{Class.forName(mapStructure.getValueClassName())},
                        null, 
                        new String[]{}, 
                        new ForeignKey[]{}, 
                        MergedPropertyType.MAPSTRUCTUREVALUE,
                        persistencePerspective.getPopulateToOneFields(), 
                        persistencePerspective.getIncludeFields(), 
                        persistencePerspective.getExcludeFields(),
                        persistencePerspective.getConfigurationKey(),
                        ""
                    );
                }
                allMergedProperties.put(MergedPropertyType.MAPSTRUCTUREVALUE, valueMergedProperties);
            }
        } catch (Exception e) {
            throw new ServiceException("Unable to fetch results for " + ceilingEntityFullyQualifiedClassname, e);
        }
    }

    @Override
    public Entity add(PersistencePackage persistencePackage) throws ServiceException {
        String[] customCriteria = persistencePackage.getCustomCriteria();
        if (customCriteria != null && customCriteria.length > 0) {
            LOG.warn("custom persistence handlers and custom criteria not supported for add types other than BASIC");
        }
        PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
        Entity entity = persistencePackage.getEntity();
        MapStructure mapStructure = (MapStructure) persistencePerspective.getPersistenceAssociationItems().get(PersistenceAssociationItemType.MAPSTRUCTURE);
        if (!mapStructure.getMutable()) {
            throw new SecurityServiceException("Field not mutable");
        }
        try {
            Map<String, FieldMetadata> ceilingMergedProperties = getSimpleMergedProperties(entity.getType()[0],
                    persistencePerspective);
            String mapKey = entity.findProperty(mapStructure.getKeyPropertyName()).getValue();
            if (StringUtils.isEmpty(mapKey)) {
                entity.addValidationError(mapStructure.getKeyPropertyName(), RequiredPropertyValidator.ERROR_MESSAGE);
                LOG.debug("No key property passed in for map, failing validation");
            }
            
            if (ceilingMergedProperties.containsKey(mapStructure.getMapProperty() + FieldManager.MAPFIELDSEPARATOR + mapKey)) {
                throw new ServiceException("\"" + mapKey + "\" is a reserved property name.");
            }

            Serializable instance = persistenceOperationExecutor.getDynamicEntityDao().retrieve(Class.forName(entity.getType()
                    [0]), Long.valueOf(entity.findProperty("symbolicId").getValue()));

            Assert.isTrue(instance != null, "Entity not found");

            FieldManager fieldManager = getFieldManager();
            Map map = (Map) fieldManager.getFieldValue(instance, mapStructure.getMapProperty());
            
            if (map.containsKey(mapKey)) {
                entity.addValidationError(mapStructure.getKeyPropertyName(), "keyExistsValidationError");
            }
            
            PersistentClass persistentClass = persistenceOperationExecutor.getDynamicEntityDao().getPersistentClass(mapStructure.getValueClassName());
            Map<String, FieldMetadata> valueUnfilteredMergedProperties;
            if (persistentClass == null) {
                valueUnfilteredMergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getPropertiesForPrimitiveClass(
                    ((SimpleValueMapStructure) mapStructure).getValuePropertyName(), 
                    ((SimpleValueMapStructure) mapStructure).getValuePropertyFriendlyName(),
                    Class.forName(mapStructure.getValueClassName()), 
                    Class.forName(entity.getType()[0]), 
                    MergedPropertyType.MAPSTRUCTUREVALUE
                );
            } else {
                valueUnfilteredMergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getMergedProperties(
                    mapStructure.getValueClassName(), 
                    new Class[]{Class.forName(mapStructure.getValueClassName())},
                    null, 
                    new String[]{}, 
                    new ForeignKey[]{}, 
                    MergedPropertyType.MAPSTRUCTUREVALUE,
                    persistencePerspective.getPopulateToOneFields(), 
                    persistencePerspective.getIncludeFields(), 
                    persistencePerspective.getExcludeFields(),
                    persistencePerspective.getConfigurationKey(),
                    ""
                );
            }
            Map<String, FieldMetadata> valueMergedProperties = filterOutCollectionMetadata(valueUnfilteredMergedProperties);
            
            if (persistentClass != null) {
                Serializable valueInstance = (Serializable) Class.forName(mapStructure.getValueClassName()).newInstance();
                valueInstance = createPopulatedInstance(valueInstance, entity, valueMergedProperties, false);
                if (valueInstance instanceof ValueAssignable) {
                    //This is likely a OneToMany map (see productAttributes) whose map key is actually the name field from
                    //the mapped entity.
                    ((ValueAssignable) valueInstance).setName(entity.findProperty(mapStructure.getKeyPropertyName()).getValue());
                }
                if (mapStructure.getManyToField() != null) {
                    //Need to fulfill a bi-directional association back to the parent entity
                    fieldManager.setFieldValue(valueInstance, mapStructure.getManyToField(), instance);
                }
                valueInstance = persistenceOperationExecutor.getDynamicEntityDao().persist(valueInstance);
                /*
                 * TODO this map manipulation code currently assumes the key value is a String. This should be widened to accept
                 * additional types of primitive objects.
                 */
                map.put(mapKey, valueInstance); 
            } else {
                String propertyName = ((SimpleValueMapStructure) mapStructure).getValuePropertyName();
                String value = entity.findProperty(propertyName).getValue();
                Object convertedPrimitive = convertPrimitiveBasedOnType(propertyName, value, valueMergedProperties);
                map.put(mapKey, convertedPrimitive);
            }
            
            Entity[] responses = getMapRecords(instance, mapStructure, ceilingMergedProperties, valueMergedProperties, entity.findProperty("symbolicId"));
            for (Entity response : responses) {
                if (response.findProperty(mapStructure.getKeyPropertyName()).getValue().equals(persistencePackage.getEntity().findProperty(mapStructure.getKeyPropertyName()).getValue())) {
                    return response;
                }
            }
            return responses[0];
        } catch (Exception e) {
            throw new ServiceException("Problem updating entity : " + e.getMessage(), e);
        }
    }
    
    protected Object convertPrimitiveBasedOnType(String valuePropertyName, String value, Map<String, FieldMetadata> valueMergedProperties) throws ParseException {
        switch(((BasicFieldMetadata) valueMergedProperties.get(valuePropertyName)).getFieldType()) {
            case BOOLEAN :
                return Boolean.parseBoolean(value);
            case DATE :
                return getSimpleDateFormatter().parse(value);
            case DECIMAL :
                return new BigDecimal(value);
            case MONEY :
                return new Money(value);
            case INTEGER :
                return Integer.parseInt(value);
            default :
                return value;
        }
    }

    @Override
    public Entity update(PersistencePackage persistencePackage) throws ServiceException {
        String[] customCriteria = persistencePackage.getCustomCriteria();
        if (customCriteria != null && customCriteria.length > 0) {
            LOG.warn("custom persistence handlers and custom criteria not supported for update types other than BASIC");
        }
        PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
        Entity entity = persistencePackage.getEntity();
        MapStructure mapStructure = (MapStructure) persistencePerspective.getPersistenceAssociationItems().get(PersistenceAssociationItemType.MAPSTRUCTURE);
        if (!mapStructure.getMutable()) {
            throw new SecurityServiceException("Field not mutable");
        }
        try {
            Map<String, FieldMetadata> ceilingMergedProperties = getSimpleMergedProperties(entity.getType()[0],
                    persistencePerspective);
            String mapKey = entity.findProperty(mapStructure.getKeyPropertyName()).getValue();
            if (ceilingMergedProperties.containsKey(mapStructure.getMapProperty() + FieldManager.MAPFIELDSEPARATOR + mapKey)) {
                throw new ServiceException("\"" + mapKey + "\" is a reserved property name.");
            }

            Serializable instance = persistenceOperationExecutor.getDynamicEntityDao().retrieve(Class.forName(entity.getType()[0]), Long.valueOf(entity.findProperty("symbolicId").getValue()));

            Assert.isTrue(instance != null, "Entity not found");

            FieldManager fieldManager = getFieldManager();
            Map map = (Map) fieldManager.getFieldValue(instance, mapStructure.getMapProperty());
            
            PersistentClass persistentClass = persistenceOperationExecutor.getDynamicEntityDao().getPersistentClass(mapStructure.getValueClassName());
            Map<String, FieldMetadata> valueUnfilteredMergedProperties;
            if (persistentClass == null) {
                valueUnfilteredMergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getPropertiesForPrimitiveClass(
                    ((SimpleValueMapStructure) mapStructure).getValuePropertyName(), 
                    ((SimpleValueMapStructure) mapStructure).getValuePropertyFriendlyName(),
                    Class.forName(mapStructure.getValueClassName()), 
                    Class.forName(entity.getType()[0]), 
                    MergedPropertyType.MAPSTRUCTUREVALUE
                );
            } else {
                valueUnfilteredMergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getMergedProperties(
                    mapStructure.getValueClassName(), 
                    new Class[]{Class.forName(mapStructure.getValueClassName())},
                    null, 
                    new String[]{}, 
                    new ForeignKey[]{}, 
                    MergedPropertyType.MAPSTRUCTUREVALUE,
                    persistencePerspective.getPopulateToOneFields(), 
                    persistencePerspective.getIncludeFields(), 
                    persistencePerspective.getExcludeFields(),
                    persistencePerspective.getConfigurationKey(),
                    ""
                );
            }
            Map<String, FieldMetadata> valueMergedProperties = filterOutCollectionMetadata(valueUnfilteredMergedProperties);
            
            if (StringUtils.isEmpty(mapKey)) {
                entity.addValidationError(mapStructure.getKeyPropertyName(), RequiredPropertyValidator.ERROR_MESSAGE);
                LOG.debug("No key property passed in for map, failing validation");
            }

            populate: {
                if (persistentClass != null) {
                    Serializable valueInstance = (Serializable) map.get(entity.findProperty("priorKey").getValue());

                    if (valueInstance == null) {
                        valueInstance = procureSandBoxMapValue(mapStructure, entity);
                        if (valueInstance == null) {
                            break populate;
                        }
                    }

                    if (map.get(mapKey) != null && !map.get(mapKey).equals(valueInstance)) {
                        entity.addValidationError(mapStructure.getKeyPropertyName(), "keyExistsValidationError");
                    }

                    if (StringUtils.isNotBlank(mapStructure.getMapKeyValueProperty())) {
                        Property p = entity.findProperty("key");
                        Property newP = new Property();
                        newP.setName(mapStructure.getMapKeyValueProperty());
                        newP.setValue(p.getValue());
                        newP.setIsDirty(p.getIsDirty());
                        entity.addProperty(newP);
                    }

                    //allow validation on other properties in order to show key validation errors along with all the other properties
                    //validation errors
                    valueInstance = createPopulatedInstance(valueInstance, entity, valueMergedProperties, false);

                    if (StringUtils.isNotEmpty(mapKey) && !entity.isValidationFailure()) {
                        if (!entity.findProperty("priorKey").getValue().equals(mapKey)) {
                            map.remove(entity.findProperty("priorKey").getValue());
                        }
                        /*
                         * TODO this map manipulation code currently assumes the key value is a String. This should be widened to accept
                         * additional types of primitive objects.
                         */
                        map.put(entity.findProperty(mapStructure.getKeyPropertyName()).getValue(), valueInstance);
                    }
                } else {
                    if (StringUtils.isNotEmpty(mapKey) && !entity.isValidationFailure()) {
                        map.put(entity.findProperty(mapStructure.getKeyPropertyName()).getValue(), entity.findProperty(((SimpleValueMapStructure) mapStructure).getValuePropertyName()).getValue());
                    }
                }
            }

            instance = persistenceOperationExecutor.getDynamicEntityDao().merge(instance);
            
            Entity[] responses = getMapRecords(instance, mapStructure, ceilingMergedProperties, valueMergedProperties, entity.findProperty("symbolicId"));
            for (Entity response : responses) {
                if (response.findProperty(mapStructure.getKeyPropertyName()).getValue().equals(persistencePackage.getEntity().findProperty(mapStructure.getKeyPropertyName()).getValue())) {
                    return response;
                }
            }
            //could be empty if reverting a sandbox item that has experienced a deletion. make sure to at least return an empty instance of Entity.
            return ArrayUtils.isEmpty(responses)?new Entity():responses[0];
        } catch (Exception e) {
            throw new ServiceException("Problem updating entity : " + e.getMessage(), e);
        }
    }

    @Override
    public void remove(PersistencePackage persistencePackage) throws ServiceException {
        String[] customCriteria = persistencePackage.getCustomCriteria();
        if (customCriteria != null && customCriteria.length > 0) {
            LOG.warn("custom persistence handlers and custom criteria not supported for remove types other than BASIC");
        }
        PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
        Entity entity = persistencePackage.getEntity();
        MapStructure mapStructure = (MapStructure) persistencePerspective.getPersistenceAssociationItems().get(PersistenceAssociationItemType.MAPSTRUCTURE);
        if (!mapStructure.getMutable()) {
            throw new SecurityServiceException("Field not mutable");
        }
        try {
            Map<String, FieldMetadata> ceilingMergedProperties = getSimpleMergedProperties(entity.getType()[0],
                    persistencePerspective);
            String mapKey = entity.findProperty(mapStructure.getKeyPropertyName()).getValue();
            if (ceilingMergedProperties.containsKey(mapStructure.getMapProperty() + FieldManager.MAPFIELDSEPARATOR + mapKey)) {
                throw new ServiceException("\"" + mapKey + "\" is a reserved property name.");
            }

            Serializable instance = persistenceOperationExecutor.getDynamicEntityDao().retrieve(Class.forName(entity.getType()[0]), Long.valueOf(entity.findProperty("symbolicId").getValue()));

            Assert.isTrue(instance != null, "Entity not found");

            FieldManager fieldManager = getFieldManager();
            Map map = (Map) fieldManager.getFieldValue(instance, mapStructure.getMapProperty());
            
            Object value = map.remove(entity.findProperty("priorKey").getValue());
            if (mapStructure.getDeleteValueEntity()) {
                persistenceOperationExecutor.getDynamicEntityDao().remove((Serializable) value);
            }
        } catch (Exception e) {
            throw new ServiceException("Problem removing entity : " + e.getMessage(), e);
        }
    }
    
    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException {
        Entity[] payload;
        int totalRecords;
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        if (StringUtils.isEmpty(persistencePackage.getFetchTypeClassname())) {
            persistencePackage.setFetchTypeClassname(ceilingEntityFullyQualifiedClassname);
        }
        try {
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            Class<?>[] entities = persistenceOperationExecutor.getPolymorphicEntities(ceilingEntityFullyQualifiedClassname);
            Map<String, FieldMetadata> mergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getMergedProperties(
                ceilingEntityFullyQualifiedClassname, 
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
            MapStructure mapStructure = (MapStructure) persistencePerspective.getPersistenceAssociationItems().get(PersistenceAssociationItemType.MAPSTRUCTURE);
            
            PersistentClass persistentClass = persistenceOperationExecutor.getDynamicEntityDao().getPersistentClass(mapStructure.getValueClassName());
            Map<String, FieldMetadata> valueUnfilteredMergedProperties;
            if (persistentClass == null) {
                valueUnfilteredMergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getPropertiesForPrimitiveClass(
                    ((SimpleValueMapStructure) mapStructure).getValuePropertyName(), 
                    ((SimpleValueMapStructure) mapStructure).getValuePropertyFriendlyName(),
                    Class.forName(mapStructure.getValueClassName()), 
                    Class.forName(ceilingEntityFullyQualifiedClassname), 
                    MergedPropertyType.MAPSTRUCTUREVALUE
                );
            } else {
                valueUnfilteredMergedProperties = persistenceOperationExecutor.getDynamicEntityDao().getMergedProperties(
                    mapStructure.getValueClassName(), 
                    new Class[]{Class.forName(mapStructure.getValueClassName())},
                    null, 
                    new String[]{}, 
                    new ForeignKey[]{}, 
                    MergedPropertyType.MAPSTRUCTUREVALUE,
                    false,
                    new String[]{},
                    new String[]{},
                    null,
                    ""
                );
            }
            Map<String, FieldMetadata> valueMergedProperties = filterOutCollectionMetadata(valueUnfilteredMergedProperties);

            List<FilterMapping> filterMappings = getFilterMappings(persistencePerspective, cto, persistencePackage
                    .getFetchTypeClassname(), mergedProperties);
            totalRecords = getTotalRecords(persistencePackage.getFetchTypeClassname(), filterMappings);
            if (totalRecords > 1) {
                throw new ServiceException("Queries to retrieve an entity containing a MapStructure must return only 1 entity. Your query returned ("+totalRecords+") values.");
            }
            List<Serializable> records = getPersistentRecords(persistencePackage.getFetchTypeClassname(), filterMappings, cto.getFirstResult(), cto.getMaxResults());
            Map<String, FieldMetadata> ceilingMergedProperties = getSimpleMergedProperties(ceilingEntityFullyQualifiedClassname,
                    persistencePerspective);
            payload = getMapRecords(records.get(0), mapStructure, ceilingMergedProperties, valueMergedProperties, null);
        } catch (Exception e) {
            throw new ServiceException("Unable to fetch results for " + ceilingEntityFullyQualifiedClassname, e);
        }
        
        DynamicResultSet results = new DynamicResultSet(null, payload, payload.length);
        
        return results;
    }

    protected Serializable procureSandBoxMapValue(MapStructure mapStructure, Entity entity) {
        try {
            Serializable valueInstance = null;
            //this is probably a sync from another sandbox where they've updated a map item for which we've updated the key in our own sandbox
            //(i.e. the map entry key was changed by us in our sandbox, so our map does not have the requested key)
            Class<?> valueClass = Class.forName(mapStructure.getValueClassName());
            Map<String, Object> idMetadata = getPersistenceManager().getDynamicEntityDao().
                    getIdMetadata(valueClass);
            String idProperty = (String) idMetadata.get("name");
            Property prop = entity.findProperty(idProperty);
            if (prop != null) {
                Serializable identifier;
                if (!(((Type) idMetadata.get("type")) instanceof StringType)) {
                    identifier = Long.parseLong(prop.getValue());
                } else {
                    identifier = prop.getValue();
                }
                valueInstance = (Serializable) getPersistenceManager().getDynamicEntityDao().find(valueClass, identifier);
                WebRequestContext context = WebRequestContext.getWebRequestContext();
                if (sandBoxHelper.isSandBoxable(valueInstance.getClass().getName()) &&
                        context != null && !context.isProductionSandBox()) {
                    if (sandBoxHelper.isPromote() && !sandBoxHelper.isReject()) {
                        //if this is a prod record (i.e. the destination map has deleted our record), then duplicate our value
                        //so it's available in this sandbox
                        valueInstance = getPersistenceManager().getDynamicEntityDao().merge(valueInstance);
                    } else {
                        valueInstance = null;
                    }
                }
            }
            return valueInstance;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected Entity[] getMapRecords(Serializable record, MapStructure mapStructure, Map<String, FieldMetadata> ceilingMergedProperties, Map<String, FieldMetadata> valueMergedProperties, Property symbolicIdProperty) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchFieldException {
        //compile a list of mapKeys that were used as mapFields
        List<String> mapFieldKeys = new ArrayList<String>();
        String mapProperty = mapStructure.getMapProperty();
        for (Map.Entry<String, FieldMetadata> entry : ceilingMergedProperties.entrySet()) {
            if (entry.getKey().startsWith(mapProperty + FieldManager.MAPFIELDSEPARATOR)) {
                mapFieldKeys.add(entry.getKey().substring(entry.getKey().indexOf(FieldManager.MAPFIELDSEPARATOR) + FieldManager.MAPFIELDSEPARATOR.length(), entry.getKey().length()));
            }
        }
        Collections.sort(mapFieldKeys);

        FieldManager fieldManager = getFieldManager();
        Map map;
        try {
            map = (Map) fieldManager.getFieldValue(record, mapProperty);
        } catch (FieldNotAvailableException e) {
            throw new IllegalArgumentException(e);
        }
        List<Entity> entities = new ArrayList<Entity>(map.size());
        for (Object key : map.keySet()) {
            if (key instanceof String && mapFieldKeys.contains(key)) {
                continue;
            }
            Entity entityItem = new Entity();
            entityItem.setType(new String[]{record.getClass().getName()});
            entities.add(entityItem);
            List<Property> props = new ArrayList<Property>();

            Property propertyItem = new Property();
            propertyItem.setName(mapStructure.getKeyPropertyName());
            props.add(propertyItem);
            String strVal;
            if (Date.class.isAssignableFrom(key.getClass())) {
                strVal = getSimpleDateFormatter().format((Date) key);
            } else if (Timestamp.class.isAssignableFrom(key.getClass())) {
                strVal = getSimpleDateFormatter().format(new Date(((Timestamp) key).getTime()));
            } else if (Calendar.class.isAssignableFrom(key.getClass())) {
                strVal = getSimpleDateFormatter().format(((Calendar) key).getTime());
            } else if (Double.class.isAssignableFrom(key.getClass())) {
                strVal = getDecimalFormatter().format(key);
            } else if (BigDecimal.class.isAssignableFrom(key.getClass())) {
                strVal = getDecimalFormatter().format(key);
            } else {
                strVal = key.toString();
            }
            propertyItem.setValue(strVal);

            PersistentClass persistentClass = persistenceOperationExecutor.getDynamicEntityDao().getPersistentClass(mapStructure.getValueClassName());
            if (persistentClass == null) {
                Property temp = new Property();
                temp.setName(((SimpleValueMapStructure) mapStructure).getValuePropertyName());
                temp.setValue(String.valueOf(map.get(key)));
                props.add(temp);
            } else {
                extractPropertiesFromPersistentEntity(valueMergedProperties, (Serializable) map.get(key), props);
            }
            if (symbolicIdProperty != null) {
                props.add(symbolicIdProperty);
            }

            Property[] properties = new Property[props.size()];
            properties = props.toArray(properties);
            entityItem.setProperties(properties);
        }

        return entities.toArray(new Entity[entities.size()]);
    }
}

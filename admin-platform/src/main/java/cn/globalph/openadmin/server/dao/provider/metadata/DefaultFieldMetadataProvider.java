package cn.globalph.openadmin.server.dao.provider.metadata;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.override.FieldMetadataOverride;
import cn.globalph.openadmin.server.dao.FieldInfo;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataFromFieldTypeRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataFromMappingDataRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.OverrideViaXmlRequest;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.Property;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author felix.wu
 */
@Component("blDefaultFieldMetadataProvider")
@Scope("prototype")
public class DefaultFieldMetadataProvider extends BasicFieldMetadataProvider {

    private static final Log LOG = LogFactory.getLog(DefaultFieldMetadataProvider.class);

    @Override
    public FieldProviderResponse addMetadata(AddMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata) {
        Map<String, Object> idMetadata = addMetadataRequest.getDynamicEntityDao().getIdMetadata(addMetadataRequest.getTargetClass());
        if (idMetadata != null) {
            String idField = (String) idMetadata.get("name");
            boolean processField;
            //allow id fields without AdminPresentation annotation to pass through
            processField = idField.equals(addMetadataRequest.getRequestedField().getName());
            if (!processField) {
                List<String> propertyNames = addMetadataRequest.getDynamicEntityDao().getPropertyNames(
                        addMetadataRequest.getTargetClass());
                if (!CollectionUtils.isEmpty(propertyNames)) {
                    List<org.hibernate.type.Type> propertyTypes = addMetadataRequest.getDynamicEntityDao().getPropertyTypes(
                            addMetadataRequest.getTargetClass());
                    int index = propertyNames.indexOf(addMetadataRequest.getRequestedField().getName());
                    if (index >= 0) {
                        Type myType = propertyTypes.get(index);
                        //allow OneToOne, ManyToOne and Embeddable fields to pass through
                        processField =  myType.isCollectionType() || myType.isAssociationType() ||
                                myType.isComponentType() || myType.isEntityType();
                    }
                }
            }
            if (processField) {
                FieldInfo info = buildFieldInfo(addMetadataRequest.getRequestedField());
                BasicFieldMetadata basicMetadata = new BasicFieldMetadata();
                basicMetadata.setName(addMetadataRequest.getRequestedField().getName());
                basicMetadata.setExcluded(false);
                metadata.put(addMetadataRequest.getRequestedField().getName(), basicMetadata);
                setClassOwnership(addMetadataRequest.getParentClass(), addMetadataRequest.getTargetClass(), metadata, info);
                return FieldProviderResponse.HANDLED;
            }
        }
        return FieldProviderResponse.NOT_HANDLED;
    }

    public void overrideExclusionsFromXml(OverrideViaXmlRequest overrideViaXmlRequest, Map<String, FieldMetadata> metadata) {
        //override any and all exclusions derived from xml
        Map<String, FieldMetadataOverride> overrides = getTargetedOverride(overrideViaXmlRequest.getDynamicEntityDao(), overrideViaXmlRequest.getRequestedConfigKey(),
                overrideViaXmlRequest.getRequestedCeilingEntity());
        if (overrides != null) {
            for (String propertyName : overrides.keySet()) {
                final FieldMetadataOverride localMetadata = overrides.get(propertyName);
                Boolean excluded = localMetadata.getExcluded();
                for (String key : metadata.keySet()) {
                    String testKey = overrideViaXmlRequest.getPrefix() + key;
                    if ((testKey.startsWith(propertyName + ".") || testKey.equals(propertyName)) && excluded != null &&
                            excluded) {
                        FieldMetadata fieldMetadata = metadata.get(key);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("setExclusionsBasedOnParents:Excluding " + key +
                                    "because an override annotation declared "+ testKey + " to be excluded");
                        }
                        fieldMetadata.setExcluded(true);
                        continue;
                    }
                    if ((testKey.startsWith(propertyName + ".") || testKey.equals(propertyName)) && excluded != null &&
                            !excluded) {
                        FieldMetadata fieldMetadata = metadata.get(key);
                        if (!overrideViaXmlRequest.getParentExcluded()) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("setExclusionsBasedOnParents:Showing " + key +
                                        "because an override annotation declared " + testKey + " to not be excluded");
                            }
                            fieldMetadata.setExcluded(false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public FieldProviderResponse addMetadataFromMappingData(AddMetadataFromMappingDataRequest addMetadataFromMappingDataRequest,
                                                            FieldMetadata metadata) {
        BasicFieldMetadata fieldMetadata = (BasicFieldMetadata) metadata;
        fieldMetadata.setFieldType(addMetadataFromMappingDataRequest.getType());
        fieldMetadata.setSecondaryType(addMetadataFromMappingDataRequest.getSecondaryType());
        if (addMetadataFromMappingDataRequest.getRequestedEntityType() != null &&
                !addMetadataFromMappingDataRequest.getRequestedEntityType().isCollectionType()) {
            Column column = null;
            for (Property property : addMetadataFromMappingDataRequest.getComponentProperties()) {
                if (property.getName().equals(addMetadataFromMappingDataRequest.getPropertyName())) {
                    column = (Column) property.getColumnIterator().next();
                    break;
                }
            }
            if (column != null) {
                fieldMetadata.setLength(column.getLength());
                fieldMetadata.setScale(column.getScale());
                fieldMetadata.setPrecision(column.getPrecision());
                fieldMetadata.setRequired(!column.isNullable());
                fieldMetadata.setUnique(column.isUnique());
            }
            fieldMetadata.setForeignKeyCollection(false);
        } else {
            fieldMetadata.setForeignKeyCollection(true);
        }
        fieldMetadata.setMutable(true);
        fieldMetadata.setMergedPropertyType(addMetadataFromMappingDataRequest.getMergedPropertyType());
        if (SupportedFieldType.BROADLEAF_ENUMERATION.equals(addMetadataFromMappingDataRequest.getType())) {
            try {
                setupBroadleafEnumeration(fieldMetadata.getBroadleafEnumeration(), fieldMetadata,
                        addMetadataFromMappingDataRequest.getDynamicEntityDao());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return FieldProviderResponse.HANDLED;
    }
    
    /**
     *@param metadata  
     */
    @Override
    public FieldProviderResponse addMetadataFromFieldType(AddMetadataFromFieldTypeRequest req,
                                                          Map<String, FieldMetadata> metadata) {
        if ( req.getPresentationAttribute() != null ) {
            if ( req.getExplicitType() != null &&
                 req.getExplicitType() != SupportedFieldType.UNKNOWN &&
                 req.getExplicitType() != SupportedFieldType.BOOLEAN &&
                 req.getExplicitType() != SupportedFieldType.INTEGER &&
                 req.getExplicitType() != SupportedFieldType.DATE &&
                 req.getExplicitType() != SupportedFieldType.STRING &&
                 req.getExplicitType() != SupportedFieldType.MONEY &&
                 req.getExplicitType() != SupportedFieldType.DECIMAL &&
                 req.getExplicitType() != SupportedFieldType.FOREIGN_KEY &&
                 req.getExplicitType() != SupportedFieldType.ADDITIONAL_FOREIGN_KEY ) {
                metadata.put( req.getRequestedPropertyName(),
                              req.getDynamicEntityDao()
                        .getPresentationAnnoInspector().getFieldMetadata(req.getPrefix(),
                                req.getRequestedPropertyName(),
                                req.getComponentProperties(),
                                req.getExplicitType(), req.getType(),
                                req.getTargetClass(),
                                req.getPresentationAttribute(), req.
                                getMergedPropertyType(), req.getDynamicEntityDao()));
            } else if (
                    req.getExplicitType() != null &&
                            req.getExplicitType() == SupportedFieldType.BOOLEAN
                            ||
                            req.getReturnedClass().equals(Boolean.class) ||
                            req.getReturnedClass().equals(Character.class)
                    ) {
                metadata.put(req.getRequestedPropertyName(),
                        req.getDynamicEntityDao()
                        .getPresentationAnnoInspector().getFieldMetadata(req.getPrefix(),
                                req.getRequestedPropertyName(),
                                req.getComponentProperties(),
                                SupportedFieldType.BOOLEAN, req.getType(),
                                req.getTargetClass(),
                                req.getPresentationAttribute(),
                                req.getMergedPropertyType(),
                                req.getDynamicEntityDao()));
            } else if (
                    req.getExplicitType() != null &&
                        req.getExplicitType() == SupportedFieldType.INTEGER
                        ||
                        req.getReturnedClass().equals(Byte.class) ||
                        req.getReturnedClass().equals(Short.class) ||
                        req.getReturnedClass().equals(Integer.class) ||
                        req.getReturnedClass().equals(Long.class)
                    ) {
                if (req.getRequestedPropertyName().equals(req.getIdProperty())) {
                    metadata.put(req.getRequestedPropertyName(),
                        req.getDynamicEntityDao().getPresentationAnnoInspector().getFieldMetadata(
                            req.getPrefix(), req.getRequestedPropertyName(),
                            req.getComponentProperties(),
                            SupportedFieldType.ID, SupportedFieldType.INTEGER, req.getType(),
                            req.getTargetClass(), req.getPresentationAttribute(),
                            req.getMergedPropertyType(), req.getDynamicEntityDao()));
                } else {
                    metadata.put(req.getRequestedPropertyName(),
                        req.getDynamicEntityDao().getPresentationAnnoInspector().getFieldMetadata(req
                            .getPrefix(), req.getRequestedPropertyName(),
                            req.getComponentProperties(),
                            SupportedFieldType.INTEGER, req.getType(),
                            req.getTargetClass(), req.
                            getPresentationAttribute(), req.getMergedPropertyType(),
                            req.getDynamicEntityDao()));
                }
            } else if (
                    req.getExplicitType() != null &&
                            req.getExplicitType() == SupportedFieldType.DATE
                            ||
                            req.getReturnedClass().equals(Calendar.class) ||
                            req.getReturnedClass().equals(Date.class) ||
                            req.getReturnedClass().equals(Timestamp.class)
                    ) {
                metadata.put(req.getRequestedPropertyName(),
                    req.getDynamicEntityDao()
                    .getPresentationAnnoInspector().getFieldMetadata(req.getPrefix(),
                        req.getRequestedPropertyName(),
                        req.getComponentProperties(),
                        SupportedFieldType.DATE, req.getType(),
                        req.getTargetClass(),
                        req.getPresentationAttribute(),
                        req.getMergedPropertyType(),
                        req.getDynamicEntityDao()
                    )
                );
            } else if (
                    req.getExplicitType() != null &&
                            req.getExplicitType() == SupportedFieldType.STRING
                            ||
                            req.getReturnedClass().equals(String.class)
                    ) {
                if (req.getRequestedPropertyName().equals(req.getIdProperty())) {
                    metadata.put(req.getRequestedPropertyName(),
                        req.getDynamicEntityDao().getPresentationAnnoInspector().getFieldMetadata(
                            req.getPrefix(),
                            req.getRequestedPropertyName(),
                            req.getComponentProperties(),
                            SupportedFieldType.ID, SupportedFieldType.STRING,
                            req.getType(),
                            req.getTargetClass(),
                            req.getPresentationAttribute(),
                            req.getMergedPropertyType(),
                            req.getDynamicEntityDao()));
                } else {
                    metadata.put(req.getRequestedPropertyName(),
                        req.getDynamicEntityDao().getPresentationAnnoInspector().getFieldMetadata(
                            req.getPrefix(),
                            req.getRequestedPropertyName(),
                            req.getComponentProperties(),
                            SupportedFieldType.STRING, req.getType(),
                            req.getTargetClass(),
                            req.getPresentationAttribute(),
                            req.getMergedPropertyType(),
                            req.getDynamicEntityDao()));
                }
            } else if (
                    req.getExplicitType() != null &&
                            req.getExplicitType() == SupportedFieldType.MONEY
                            ||
                            req.getReturnedClass().equals(Money.class)
                    ) {
                metadata.put(req.getRequestedPropertyName(),
                    req.getDynamicEntityDao().getPresentationAnnoInspector().getFieldMetadata(
                        req.getPrefix(),
                        req.getRequestedPropertyName(),
                        req.getComponentProperties(),
                        SupportedFieldType.MONEY, req.getType(),
                        req.getTargetClass(),
                        req.getPresentationAttribute(),
                        req.getMergedPropertyType(),
                        req.getDynamicEntityDao()
                    )
                );
            } else if (
                    req.getExplicitType() != null &&
                            req.getExplicitType() == SupportedFieldType.DECIMAL
                            ||
                            req.getReturnedClass().equals(Double.class) ||
                            req.getReturnedClass().equals(BigDecimal.class)
                    ) {
                metadata.put(req.getRequestedPropertyName(),
                    req.getDynamicEntityDao().getPresentationAnnoInspector().getFieldMetadata(
                        req.getPrefix(),
                        req.getRequestedPropertyName(),
                        req.getComponentProperties(),
                        SupportedFieldType.DECIMAL, req.getType(),
                        req.getTargetClass(),
                        req.getPresentationAttribute(),
                        req.getMergedPropertyType(),
                        req.getDynamicEntityDao()
                    )
                );
            } else if (
                    req.getExplicitType() != null &&
                            req.getExplicitType() == SupportedFieldType.FOREIGN_KEY
                            ||
                            req.getForeignField() != null &&
                                    req.isPropertyForeignKey()
                    ) {
                ClassMetadata foreignMetadata;
                String foreignKeyClass;
                String lookupDisplayProperty;
                if (req.getForeignField() == null) {
                    Class<?>[] entities = req.getDynamicEntityDao().
                            getAllPolymorphicEntitiesFromCeiling(req.getType().getReturnedClass());
                    foreignMetadata = req.getDynamicEntityDao().getSessionFactory().getClassMetadata(entities
                            [entities.length - 1]);
                    foreignKeyClass = entities[entities.length - 1].getName();
                    lookupDisplayProperty = ((BasicFieldMetadata) req.
                            getPresentationAttribute()).getLookupDisplayProperty();
                    if (StringUtils.isEmpty(lookupDisplayProperty) &&
                            AdminMainEntity.class.isAssignableFrom(entities[entities.length - 1])) {
                        lookupDisplayProperty = AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY;
                    }
                    if (StringUtils.isEmpty(lookupDisplayProperty)) {
                        lookupDisplayProperty = "name";
                    }
                } else {
                    try {
                        foreignMetadata = req.getDynamicEntityDao().getSessionFactory().
                                getClassMetadata(Class.forName(req.getForeignField()
                                .getForeignKeyClass()));
                        foreignKeyClass = req.getForeignField().getForeignKeyClass();
                        lookupDisplayProperty = req.getForeignField().getDisplayValueProperty();
                        if (StringUtils.isEmpty(lookupDisplayProperty) &&
                                AdminMainEntity.class.isAssignableFrom(Class.forName(foreignKeyClass))) {
                            lookupDisplayProperty = AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY;
                        }
                        if (StringUtils.isEmpty(lookupDisplayProperty)) {
                            lookupDisplayProperty = "name";
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                Class<?> foreignResponseType = foreignMetadata.getIdentifierType().getReturnedClass();
                if (foreignResponseType.equals(String.class)) {
                    metadata.put(req.getRequestedPropertyName(),
                        req.getDynamicEntityDao().getPresentationAnnoInspector().
                            getFieldMetadata(req.getPrefix(),
                                req.getRequestedPropertyName(),
                                req.getComponentProperties(),
                                SupportedFieldType.FOREIGN_KEY, SupportedFieldType.STRING,
                                req.getType(),
                                req.getTargetClass(),
                                req.getPresentationAttribute(),
                                req.getMergedPropertyType(),
                                req.getDynamicEntityDao()
                            )
                    );
                } else {
                    metadata.put(req.getRequestedPropertyName(), req
                        .getDynamicEntityDao().getPresentationAnnoInspector().getFieldMetadata(req.getPrefix(),
                                req.getRequestedPropertyName(),
                                req.getComponentProperties(),
                                SupportedFieldType.FOREIGN_KEY, SupportedFieldType.INTEGER,
                                req.getType(),
                                req.getTargetClass(),
                                req.getPresentationAttribute(),
                                req.getMergedPropertyType(),
                                req.getDynamicEntityDao()
                        )
                    );
                }
                ((BasicFieldMetadata) metadata.get(req.getRequestedPropertyName())).
                        setForeignKeyProperty(foreignMetadata.getIdentifierPropertyName());
                ((BasicFieldMetadata) metadata.get(req.getRequestedPropertyName()))
                        .setForeignKeyClass(foreignKeyClass);
                ((BasicFieldMetadata) metadata.get(req.getRequestedPropertyName())).
                        setForeignKeyDisplayValueProperty(lookupDisplayProperty);
            } else if (
                    req.getExplicitType() != null &&
                            req.getExplicitType() == SupportedFieldType.ADDITIONAL_FOREIGN_KEY
                            ||
                            req.getAdditionalForeignFields() != null &&
                                    req.getAdditionalForeignKeyIndexPosition() >= 0
                    ) {
                if (!req.getType().isEntityType()) {
                    throw new IllegalArgumentException("Only ManyToOne and OneToOne fields can be marked as a " +
                            "SupportedFieldType of ADDITIONAL_FOREIGN_KEY");
                }
                ClassMetadata foreignMetadata;
                String foreignKeyClass;
                String lookupDisplayProperty;
                if (req.getAdditionalForeignKeyIndexPosition() < 0) {
                    Class<?>[] entities = req.getDynamicEntityDao().getAllPolymorphicEntitiesFromCeiling
                            (req.getType().getReturnedClass());
                    foreignMetadata = req.getDynamicEntityDao().getSessionFactory().
                            getClassMetadata(entities[entities.length - 1]);
                    foreignKeyClass = entities[entities.length - 1].getName();
                    lookupDisplayProperty = ((BasicFieldMetadata) req.getPresentationAttribute()).
                            getLookupDisplayProperty();
                    if (StringUtils.isEmpty(lookupDisplayProperty) &&
                            AdminMainEntity.class.isAssignableFrom(entities[entities.length - 1])) {
                        lookupDisplayProperty = AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY;
                    }
                    if (StringUtils.isEmpty(lookupDisplayProperty)) {
                        lookupDisplayProperty = "name";
                    }
                } else {
                    try {
                        foreignMetadata = req.getDynamicEntityDao().getSessionFactory().
                                getClassMetadata(Class.forName(req.getAdditionalForeignFields()
                                        [req.getAdditionalForeignKeyIndexPosition()].getForeignKeyClass()));
                        foreignKeyClass = req.getAdditionalForeignFields()[
                                req.getAdditionalForeignKeyIndexPosition()].getForeignKeyClass();
                        lookupDisplayProperty = req.getAdditionalForeignFields()[
                                req.getAdditionalForeignKeyIndexPosition()].getDisplayValueProperty();
                        if (StringUtils.isEmpty(lookupDisplayProperty) && AdminMainEntity.class.isAssignableFrom(Class.forName(foreignKeyClass))) {
                            lookupDisplayProperty = AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY;
                        }
                        if (StringUtils.isEmpty(lookupDisplayProperty)) {
                            lookupDisplayProperty = "name";
                        }
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                Class<?> foreignResponseType = foreignMetadata.getIdentifierType().getReturnedClass();
                if (foreignResponseType.equals(String.class)) {
                    metadata.put(req.getRequestedPropertyName(),
                        req.getDynamicEntityDao().getPresentationAnnoInspector().getFieldMetadata(
                            req.getPrefix(),
                            req.getRequestedPropertyName(),
                            req.getComponentProperties(),
                            SupportedFieldType.ADDITIONAL_FOREIGN_KEY,
                            SupportedFieldType.STRING,
                            req.getType(),
                            req.getTargetClass(),
                            req.getPresentationAttribute(),
                            req.getMergedPropertyType(),
                            req.getDynamicEntityDao()
                        )
                    );
                } else {
                    metadata.put(req.getRequestedPropertyName(),
                        req.getDynamicEntityDao().getPresentationAnnoInspector().
                            getFieldMetadata(req.getPrefix(),
                                req.getRequestedPropertyName(),
                                req.getComponentProperties(),
                                SupportedFieldType.ADDITIONAL_FOREIGN_KEY, SupportedFieldType.INTEGER,
                                req.getType(),
                                req.getTargetClass(),
                                req.getPresentationAttribute(),
                                req.getMergedPropertyType(),
                                req.getDynamicEntityDao()
                            )
                    );
                }
                ((BasicFieldMetadata) metadata.get(req.getRequestedPropertyName())).
                        setForeignKeyProperty(foreignMetadata.getIdentifierPropertyName());
                ((BasicFieldMetadata) metadata.get(req.getRequestedPropertyName())).
                        setForeignKeyClass(foreignKeyClass);
                ((BasicFieldMetadata) metadata.get(req.getRequestedPropertyName())).
                        setForeignKeyDisplayValueProperty(lookupDisplayProperty);
            }
            //return type not supported - just skip this property
            return FieldProviderResponse.HANDLED;
        }
        return FieldProviderResponse.NOT_HANDLED;
    }

}

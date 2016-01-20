package cn.globalph.openadmin.server.dao.provider.metadata;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ManyToOne;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.presentation.AdminPresentationAdornedTargetCollection;
import cn.globalph.common.presentation.AdminPresentationOperationTypes;
import cn.globalph.common.presentation.client.OperationScope;
import cn.globalph.common.presentation.client.PersistenceAssociationItemType;
import cn.globalph.common.presentation.override.AdminPresentationAdornedTargetCollectionOverride;
import cn.globalph.common.presentation.override.AdminPresentationMergeEntry;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverride;
import cn.globalph.common.presentation.override.AdminPresentationMergeOverrides;
import cn.globalph.common.presentation.override.AdminPresentationOverrides;
import cn.globalph.common.presentation.override.PropertyType;
import cn.globalph.openadmin.dto.AdornedTargetCollectionMetadata;
import cn.globalph.openadmin.dto.AdornedTargetList;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.ForeignKey;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.override.FieldMetadataOverride;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.dao.FieldInfo;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataFromFieldTypeRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.OverrideViaAnnotationRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.OverrideViaXmlRequest;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author felix.wu
 */
@Component("blAdornedTargetCollectionFieldMetadataProvider")
@Scope("prototype")
public class AdornedTargetCollectionFieldMetadataProvider extends AdvancedCollectionFieldMetadataProvider {

    private static final Log LOG = LogFactory.getLog(AdornedTargetCollectionFieldMetadataProvider.class);

    protected boolean canHandleFieldForConfiguredMetadata(AddMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata) {
        AdminPresentationAdornedTargetCollection annot = addMetadataRequest.getRequestedField().getAnnotation(AdminPresentationAdornedTargetCollection.class);
        return annot != null;
    }

    protected boolean canHandleFieldForTypeMetadata(AddMetadataFromFieldTypeRequest addMetadataFromFieldTypeRequest, Map<String, FieldMetadata> metadata) {
        AdminPresentationAdornedTargetCollection annot = addMetadataFromFieldTypeRequest.getRequestedField().getAnnotation(AdminPresentationAdornedTargetCollection.class);
        return annot != null;
    }

    protected boolean canHandleAnnotationOverride(OverrideViaAnnotationRequest overrideViaAnnotationRequest, Map<String, FieldMetadata> metadata) {
        AdminPresentationOverrides myOverrides = overrideViaAnnotationRequest.getRequestedEntity().getAnnotation(AdminPresentationOverrides.class);
        AdminPresentationMergeOverrides myMergeOverrides = overrideViaAnnotationRequest.getRequestedEntity().getAnnotation(AdminPresentationMergeOverrides.class);
        return (myOverrides != null && !ArrayUtils.isEmpty(myOverrides.adornedTargetCollections())) ||
                myMergeOverrides != null;
    }

    @Override
    public FieldProviderResponse addMetadata(AddMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata) {
        if (!canHandleFieldForConfiguredMetadata(addMetadataRequest, metadata)) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        AdminPresentationAdornedTargetCollection annot = addMetadataRequest.getRequestedField().getAnnotation(AdminPresentationAdornedTargetCollection.class);
        FieldInfo info = buildFieldInfo(addMetadataRequest.getRequestedField());
        FieldMetadataOverride override = constructAdornedTargetCollectionMetadataOverride(annot);
        buildAdornedTargetCollectionMetadata(addMetadataRequest.getParentClass(), addMetadataRequest.getTargetClass(), metadata, info, override, addMetadataRequest.getDynamicEntityDao());
        setClassOwnership(addMetadataRequest.getParentClass(), addMetadataRequest.getTargetClass(), metadata, info);
        return FieldProviderResponse.HANDLED;
    }

    @Override
    public FieldProviderResponse overrideViaAnnotation(OverrideViaAnnotationRequest overrideViaAnnotationRequest, Map<String, FieldMetadata> metadata) {
        if (!canHandleAnnotationOverride(overrideViaAnnotationRequest, metadata)) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        Map<String, AdminPresentationAdornedTargetCollectionOverride> presentationAdornedTargetCollectionOverrides = new HashMap<String, AdminPresentationAdornedTargetCollectionOverride>();

        AdminPresentationOverrides myOverrides = overrideViaAnnotationRequest.getRequestedEntity().getAnnotation(AdminPresentationOverrides.class);
        if (myOverrides != null) {
            for (AdminPresentationAdornedTargetCollectionOverride myOverride : myOverrides.adornedTargetCollections()) {
                presentationAdornedTargetCollectionOverrides.put(myOverride.name(), myOverride);
            }
        }

        for (String propertyName : presentationAdornedTargetCollectionOverrides.keySet()) {
            for (String key : metadata.keySet()) {
                if (key.startsWith(propertyName)) {
                    buildAdminPresentationAdornedTargetCollectionOverride(overrideViaAnnotationRequest.getPrefix(), overrideViaAnnotationRequest.getParentExcluded(), metadata, presentationAdornedTargetCollectionOverrides, propertyName, key, overrideViaAnnotationRequest.getDynamicEntityDao());
                }
            }
        }

        AdminPresentationMergeOverrides myMergeOverrides = overrideViaAnnotationRequest.getRequestedEntity().getAnnotation(AdminPresentationMergeOverrides.class);
        if (myMergeOverrides != null) {
            for (AdminPresentationMergeOverride override : myMergeOverrides.value()) {
                String propertyName = override.name();
                Map<String, FieldMetadata> loopMap = new HashMap<String, FieldMetadata>();
                loopMap.putAll(metadata);
                for (Map.Entry<String, FieldMetadata> entry : loopMap.entrySet()) {
                    if (entry.getKey().startsWith(propertyName) || StringUtils.isEmpty(propertyName)) {
                        FieldMetadata targetMetadata = entry.getValue();
                        if (targetMetadata instanceof AdornedTargetCollectionMetadata) {
                            AdornedTargetCollectionMetadata serverMetadata = (AdornedTargetCollectionMetadata) targetMetadata;
                            if (serverMetadata.getTargetClass() != null) {
                                try {
                                    Class<?> targetClass = Class.forName(serverMetadata.getTargetClass());
                                    Class<?> parentClass = null;
                                    if (serverMetadata.getOwningClass() != null) {
                                        parentClass = Class.forName(serverMetadata.getOwningClass());
                                    }
                                    String fieldName = serverMetadata.getFieldName();
                                    Field field = overrideViaAnnotationRequest.getDynamicEntityDao().getFieldManager()
                                                .getField(targetClass, fieldName);
                                    Map<String, FieldMetadata> temp = new HashMap<String, FieldMetadata>(1);
                                    temp.put(field.getName(), serverMetadata);
                                    FieldInfo info = buildFieldInfo(field);
                                    FieldMetadataOverride fieldMetadataOverride = overrideAdornedTargetMergeMetadata(override);
                                    if (serverMetadata.getExcluded() != null && serverMetadata.getExcluded() &&
                                            (fieldMetadataOverride.getExcluded() == null || fieldMetadataOverride.getExcluded())) {
                                        continue;
                                    }
                                    buildAdornedTargetCollectionMetadata(parentClass, targetClass, temp, info,
                                            fieldMetadataOverride,
                                            overrideViaAnnotationRequest.getDynamicEntityDao());
                                    serverMetadata = (AdornedTargetCollectionMetadata) temp.get(field.getName());
                                    metadata.put(entry.getKey(), serverMetadata);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            }
        }

        return FieldProviderResponse.HANDLED;
    }

    @Override
    public FieldProviderResponse overrideViaXml(OverrideViaXmlRequest overrideViaXmlRequest, Map<String, FieldMetadata> metadata) {
        Map<String, FieldMetadataOverride> overrides = getTargetedOverride(overrideViaXmlRequest.getDynamicEntityDao(), overrideViaXmlRequest.getRequestedConfigKey(), overrideViaXmlRequest.getRequestedCeilingEntity());
        if (overrides != null) {
            for (String propertyName : overrides.keySet()) {
                final FieldMetadataOverride localMetadata = overrides.get(propertyName);
                for (String key : metadata.keySet()) {
                    if (key.equals(propertyName)) {
                        try {
                            if (metadata.get(key) instanceof AdornedTargetCollectionMetadata) {
                                AdornedTargetCollectionMetadata serverMetadata = (AdornedTargetCollectionMetadata) metadata.get(key);
                                if (serverMetadata.getTargetClass() != null) {
                                    Class<?> targetClass = Class.forName(serverMetadata.getTargetClass());
                                    Class<?> parentClass = null;
                                    if (serverMetadata.getOwningClass() != null) {
                                        parentClass = Class.forName(serverMetadata.getOwningClass());
                                    }
                                    String fieldName = serverMetadata.getFieldName();
                                    Field field = overrideViaXmlRequest.getDynamicEntityDao().getFieldManager().getField(targetClass, fieldName);
                                    Map<String, FieldMetadata> temp = new HashMap<String, FieldMetadata>(1);
                                    temp.put(field.getName(), serverMetadata);
                                    FieldInfo info = buildFieldInfo(field);
                                    buildAdornedTargetCollectionMetadata(parentClass, targetClass, temp, info, localMetadata, overrideViaXmlRequest.getDynamicEntityDao());
                                    serverMetadata = (AdornedTargetCollectionMetadata) temp.get(field.getName());
                                    metadata.put(key, serverMetadata);
                                    if (overrideViaXmlRequest.getParentExcluded()) {
                                        if (LOG.isDebugEnabled()) {
                                            LOG.debug("applyAdornedTargetCollectionMetadataOverrides:Excluding " + key + "because parent is marked as excluded.");
                                        }
                                        serverMetadata.setExcluded(true);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return FieldProviderResponse.HANDLED;
    }

    @Override
    public FieldProviderResponse addMetadataFromFieldType(AddMetadataFromFieldTypeRequest req, Map<String, FieldMetadata> metadata) {
        if (!canHandleFieldForTypeMetadata(req, metadata)) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        super.addMetadataFromFieldType(req, metadata);
        //add additional adorned target support
        AdornedTargetCollectionMetadata fieldMetadata = (AdornedTargetCollectionMetadata) req.getPresentationAttribute();
        if ( StringUtils.isEmpty(fieldMetadata.getCollectionRootEntity()) ) {
            fieldMetadata.setCollectionRootEntity( req.getType().getReturnedClass().getName() );
            AdornedTargetList targetList = ((AdornedTargetList) fieldMetadata.getPersistenceAssociation().
                                            getPersistenceAssociationItems().get(PersistenceAssociationItemType.ADORNEDTARGETLIST));
            targetList.setAdornedTargetEntityClassname( fieldMetadata.getCollectionRootEntity() );
        }
        return FieldProviderResponse.HANDLED;
    }

    protected FieldMetadataOverride overrideAdornedTargetMergeMetadata(AdminPresentationMergeOverride merge) {
        FieldMetadataOverride fieldMetadataOverride = new FieldMetadataOverride();
        Map<String, AdminPresentationMergeEntry> overrideValues = getAdminPresentationEntries(merge.mergeEntries());
        for (Map.Entry<String, AdminPresentationMergeEntry> entry : overrideValues.entrySet()) {
            String stringValue = entry.getValue().overrideValue();
            if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.CURRENCYCODEFIELD)) {
                fieldMetadataOverride.setCurrencyCodeField(stringValue);
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.CUSTOMCRITERIA)) {
                fieldMetadataOverride.setCustomCriteria(entry.getValue().stringArrayOverrideValue());
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.EXCLUDED)) {
                fieldMetadataOverride.setExcluded(StringUtils.isEmpty(stringValue)?entry.getValue().booleanOverrideValue():
                                    Boolean.parseBoolean(stringValue));
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.FRIENDLYNAME)) {
                fieldMetadataOverride.setFriendlyName(stringValue);
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.GRIDVISIBLEFIELDS)) {
                fieldMetadataOverride.setGridVisibleFields(entry.getValue().stringArrayOverrideValue());
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.IGNOREADORNEDPROPERTIES)) {
                fieldMetadataOverride.setIgnoreAdornedProperties(StringUtils.isEmpty(stringValue)?entry.getValue().booleanOverrideValue():
                                    Boolean.parseBoolean(stringValue));
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.JOINENTITYCLASS)) {
                fieldMetadataOverride.setJoinEntityClass(stringValue);
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.MAINTAINEDADORNEDTARGETFIELDS)) {
                fieldMetadataOverride.setMaintainedAdornedTargetFields(entry.getValue().stringArrayOverrideValue());
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.OPERATIONTYPES)) {
                AdminPresentationOperationTypes operationType = entry.getValue().operationTypes();
                fieldMetadataOverride.setAddType(operationType.addType());
                fieldMetadataOverride.setRemoveType(operationType.removeType());
                fieldMetadataOverride.setUpdateType(operationType.updateType());
                fieldMetadataOverride.setFetchType(operationType.fetchType());
                fieldMetadataOverride.setInspectType(operationType.inspectType());
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.ORDER)) {
                fieldMetadataOverride.setOrder(StringUtils.isEmpty(stringValue) ? entry.getValue().intOverrideValue() :
                        Integer.parseInt(stringValue));
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.PARENTOBJECTIDPROPERTY)) {
                fieldMetadataOverride.setParentObjectIdProperty(stringValue);
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.PARENTOBJECTPROPERTY)) {
                fieldMetadataOverride.setParentObjectProperty(stringValue);
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.READONLY)) {
                fieldMetadataOverride.setReadOnly(StringUtils.isEmpty(stringValue) ? entry.getValue()
                        .booleanOverrideValue() :
                        Boolean.parseBoolean(stringValue));
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.SECURITYLEVEL)) {
                fieldMetadataOverride.setSecurityLevel(stringValue);
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.SHOWIFPROPERTY)) {
                fieldMetadataOverride.setShowIfProperty(stringValue);
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.SORTASCENDING)) {
                fieldMetadataOverride.setSortAscending(StringUtils.isEmpty(stringValue) ? entry.getValue()
                        .booleanOverrideValue() :
                        Boolean.parseBoolean(stringValue));
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.SORTPROPERTY)) {
                fieldMetadataOverride.setSortProperty(stringValue);
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.TAB)) {
                fieldMetadataOverride.setTab(stringValue);
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.TABORDER)) {
                fieldMetadataOverride.setTabOrder(StringUtils.isEmpty(stringValue) ? entry.getValue()
                        .intOverrideValue() :
                        Integer.parseInt(stringValue));
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.TARGETOBJECTIDPROPERTY)) {
                fieldMetadataOverride.setTargetObjectIdProperty(stringValue);
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.TARGETOBJECTPROPERTY)) {
                fieldMetadataOverride.setTargetObjectProperty(stringValue);
            } else if (entry.getKey().equals(PropertyType.AdminPresentationAdornedTargetCollection.USESERVERSIDEINSPECTIONCACHE)) {
                fieldMetadataOverride.setUseServerSideInspectionCache(StringUtils.isEmpty(stringValue) ? entry
                        .getValue().booleanOverrideValue() :
                        Boolean.parseBoolean(stringValue));
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Unrecognized type: " + entry.getKey() + ". Not setting on adorned target field.");
                }
            }
        }

        return fieldMetadataOverride;
    }

    protected void buildAdminPresentationAdornedTargetCollectionOverride(String prefix, Boolean isParentExcluded, Map<String, FieldMetadata> mergedProperties, Map<String, AdminPresentationAdornedTargetCollectionOverride> presentationAdornedTargetCollectionOverrides, String propertyName, String key, DynamicEntityDao dynamicEntityDao) {
        AdminPresentationAdornedTargetCollectionOverride override = presentationAdornedTargetCollectionOverrides.get(propertyName);
        if (override != null) {
            AdminPresentationAdornedTargetCollection annot = override.value();
            if (annot != null) {
                String testKey = prefix + key;
                if ((testKey.startsWith(propertyName + ".") || testKey.equals(propertyName)) && annot.excluded()) {
                    FieldMetadata metadata = mergedProperties.get(key);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("buildAdminPresentationAdornedTargetCollectionOverride:Excluding " + key + "because an override annotation declared " + testKey + "to be excluded");
                    }
                    metadata.setExcluded(true);
                    return;
                }
                if ((testKey.startsWith(propertyName + ".") || testKey.equals(propertyName)) && !annot.excluded()) {
                    FieldMetadata metadata = mergedProperties.get(key);
                    if (!isParentExcluded) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("buildAdminPresentationAdornedTargetCollectionOverride:Showing " + key + "because an override annotation declared " + testKey + " to not be excluded");
                        }
                        metadata.setExcluded(false);
                    }
                }
                if (!(mergedProperties.get(key) instanceof AdornedTargetCollectionMetadata)) {
                    return;
                }
                AdornedTargetCollectionMetadata serverMetadata = (AdornedTargetCollectionMetadata) mergedProperties.get(key);
                if (serverMetadata.getTargetClass() != null) {
                    try {
                        Class<?> targetClass = Class.forName(serverMetadata.getTargetClass());
                        Class<?> parentClass = null;
                        if (serverMetadata.getOwningClass() != null) {
                            parentClass = Class.forName(serverMetadata.getOwningClass());
                        }
                        String fieldName = serverMetadata.getFieldName();
                        Field field = dynamicEntityDao.getFieldManager().getField(targetClass, fieldName);
                        FieldMetadataOverride localMetadata = constructAdornedTargetCollectionMetadataOverride(annot);
                        //do not include the previous metadata - we want to construct a fresh metadata from the override annotation
                        Map<String, FieldMetadata> temp = new HashMap<String, FieldMetadata>(1);
                        FieldInfo info = buildFieldInfo(field);
                        buildAdornedTargetCollectionMetadata(parentClass, targetClass, temp, info, localMetadata, dynamicEntityDao);
                        AdornedTargetCollectionMetadata result = (AdornedTargetCollectionMetadata) temp.get(field.getName());
                        result.setInheritedFromType(serverMetadata.getInheritedFromType());
                        result.setAvailableToTypes(serverMetadata.getAvailableToTypes());
                        mergedProperties.put(key, result);
                        if (isParentExcluded) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("buildAdminPresentationAdornedTargetCollectionOverride:Excluding " + key + "because the parent was excluded");
                            }
                            serverMetadata.setExcluded(true);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    protected FieldMetadataOverride constructAdornedTargetCollectionMetadataOverride(AdminPresentationAdornedTargetCollection adornedTargetCollection) {
        if (adornedTargetCollection != null) {
            FieldMetadataOverride override = new FieldMetadataOverride();
            override.setGridVisibleFields(adornedTargetCollection.gridVisibleFields());
            override.setIgnoreAdornedProperties(adornedTargetCollection.ignoreAdornedProperties());
            override.setMaintainedAdornedTargetFields(adornedTargetCollection.maintainedAdornedTargetFields());
            override.setParentObjectIdProperty(adornedTargetCollection.parentObjectIdProperty());
            override.setParentObjectProperty(adornedTargetCollection.parentObjectProperty());
            override.setSortAscending(adornedTargetCollection.sortAscending());
            override.setSortProperty(adornedTargetCollection.sortProperty());
            override.setTargetObjectIdProperty(adornedTargetCollection.targetObjectIdProperty());
            override.setTargetObjectProperty(adornedTargetCollection.targetObjectProperty());
            override.setJoinEntityClass(adornedTargetCollection.joinEntityClass());
            override.setCustomCriteria(adornedTargetCollection.customCriteria());
            override.setUseServerSideInspectionCache(adornedTargetCollection.useServerSideInspectionCache());
            override.setExcluded(adornedTargetCollection.excluded());
            override.setFriendlyName(adornedTargetCollection.friendlyName());
            override.setReadOnly(adornedTargetCollection.readOnly());
            override.setOrder(adornedTargetCollection.order());
            override.setTab(adornedTargetCollection.tab());
            override.setTabOrder(adornedTargetCollection.tabOrder());
            override.setSecurityLevel(adornedTargetCollection.securityLevel());
            override.setAddType(adornedTargetCollection.operationTypes().addType());
            override.setFetchType(adornedTargetCollection.operationTypes().fetchType());
            override.setRemoveType(adornedTargetCollection.operationTypes().removeType());
            override.setUpdateType(adornedTargetCollection.operationTypes().updateType());
            override.setInspectType(adornedTargetCollection.operationTypes().inspectType());
            override.setShowIfProperty(adornedTargetCollection.showIfProperty());
            override.setCurrencyCodeField(adornedTargetCollection.currencyCodeField());
            return override;
        }
        throw new IllegalArgumentException("AdminPresentationAdornedTargetCollection annotation not found on field.");
    }

    protected void buildAdornedTargetCollectionMetadata(Class<?> parentClass, Class<?> targetClass, Map<String, FieldMetadata> attributes, FieldInfo field, FieldMetadataOverride adornedTargetCollectionMetadata, DynamicEntityDao dynamicEntityDao) {
        AdornedTargetCollectionMetadata serverMetadata = (AdornedTargetCollectionMetadata) attributes.get(field.getName());

        Class<?> resolvedClass = parentClass==null?targetClass:parentClass;
        AdornedTargetCollectionMetadata metadata;
        if (serverMetadata != null) {
            metadata = serverMetadata;
        } else {
            metadata = new AdornedTargetCollectionMetadata();
        }
        metadata.setTargetClass(targetClass.getName());
        metadata.setFieldName(field.getName());

        if (adornedTargetCollectionMetadata.getReadOnly() != null) {
            metadata.setMutable(!adornedTargetCollectionMetadata.getReadOnly());
        }
        if (adornedTargetCollectionMetadata.getShowIfProperty()!=null) {
            metadata.setShowIfProperty(adornedTargetCollectionMetadata.getShowIfProperty());
        }

        cn.globalph.openadmin.dto.PersistenceOperationType dtoOperationTypes = new cn.globalph.openadmin.dto.PersistenceOperationType(OperationScope.ADORNEDTARGETLIST, OperationScope.ADORNEDTARGETLIST, OperationScope.ADORNEDTARGETLIST, OperationScope.ADORNEDTARGETLIST, OperationScope.BASIC);
        if (adornedTargetCollectionMetadata.getAddType() != null) {
            dtoOperationTypes.setAddType(adornedTargetCollectionMetadata.getAddType());
        }
        if (adornedTargetCollectionMetadata.getRemoveType() != null) {
            dtoOperationTypes.setRemoveType(adornedTargetCollectionMetadata.getRemoveType());
        }
        if (adornedTargetCollectionMetadata.getFetchType() != null) {
            dtoOperationTypes.setFetchType(adornedTargetCollectionMetadata.getFetchType());
        }
        if (adornedTargetCollectionMetadata.getInspectType() != null) {
            dtoOperationTypes.setInspectType(adornedTargetCollectionMetadata.getInspectType());
        }
        if (adornedTargetCollectionMetadata.getUpdateType() != null) {
            dtoOperationTypes.setUpdateType(adornedTargetCollectionMetadata.getUpdateType());
        }

        //don't allow additional non-persistent properties or additional foreign keys for an advanced collection datasource - they don't make sense in this context
        PersistenceAssociation persistencePerspective;
        if (serverMetadata != null) {
            persistencePerspective = metadata.getPersistenceAssociation();
            persistencePerspective.setPersistenceOperationType(dtoOperationTypes);
        } else {
            persistencePerspective = new PersistenceAssociation(dtoOperationTypes, new String[]{}, new ForeignKey[]{});
            metadata.setPersistenceAssociation(persistencePerspective);
        }

        String parentObjectProperty = null;
        if (serverMetadata != null) {
            parentObjectProperty = ((AdornedTargetList) serverMetadata.getPersistenceAssociation().getPersistenceAssociationItems().get(PersistenceAssociationItemType.ADORNEDTARGETLIST)).getLinkedObjectPath();
        }
        if (!StringUtils.isEmpty(adornedTargetCollectionMetadata.getParentObjectProperty())) {
            parentObjectProperty = adornedTargetCollectionMetadata.getParentObjectProperty();
        }
        if (parentObjectProperty == null && !StringUtils.isEmpty(field.getOneToManyMappedBy())) {
            parentObjectProperty = field.getOneToManyMappedBy();
        }
        if (parentObjectProperty == null && !StringUtils.isEmpty(field.getManyToManyMappedBy())) {
            parentObjectProperty = field.getManyToManyMappedBy();
        }
        if (StringUtils.isEmpty(parentObjectProperty)) {
            throw new IllegalArgumentException("Unable to infer a parentObjectProperty for the @AdminPresentationAdornedTargetCollection annotated field("+field.getName()+"). If not using the mappedBy property of @OneToMany or @ManyToMany, please make sure to explicitly define the parentObjectProperty property");
        }

        String sortProperty = null;
        if (serverMetadata != null) {
            sortProperty = ((AdornedTargetList) serverMetadata.getPersistenceAssociation().getPersistenceAssociationItems().get(PersistenceAssociationItemType.ADORNEDTARGETLIST)).getSortField();
        }
        if (!StringUtils.isEmpty(adornedTargetCollectionMetadata.getSortProperty())) {
            sortProperty = adornedTargetCollectionMetadata.getSortProperty();
        }

        metadata.setParentObjectClass(resolvedClass.getName());
        if (adornedTargetCollectionMetadata.getMaintainedAdornedTargetFields() != null) {
            metadata.setMaintainedAdornedTargetFields(adornedTargetCollectionMetadata.getMaintainedAdornedTargetFields());
        }
        if (adornedTargetCollectionMetadata.getGridVisibleFields() != null) {
            metadata.setGridVisibleFields(adornedTargetCollectionMetadata.getGridVisibleFields());
        }
        String parentObjectIdProperty = null;
        if (serverMetadata != null) {
            parentObjectIdProperty = ((AdornedTargetList) serverMetadata.getPersistenceAssociation().getPersistenceAssociationItems().get(PersistenceAssociationItemType.ADORNEDTARGETLIST)).getLinkedIdProperty();
        }
        if (adornedTargetCollectionMetadata.getParentObjectIdProperty()!=null) {
            parentObjectIdProperty = adornedTargetCollectionMetadata.getParentObjectIdProperty();
        }
        String targetObjectProperty = null;
        if (serverMetadata != null) {
            targetObjectProperty = ((AdornedTargetList) serverMetadata.getPersistenceAssociation().getPersistenceAssociationItems().get(PersistenceAssociationItemType.ADORNEDTARGETLIST)).getTargetObjectPath();
        }
        if (adornedTargetCollectionMetadata.getTargetObjectProperty()!=null) {
            targetObjectProperty = adornedTargetCollectionMetadata.getTargetObjectProperty();
        }
        if (StringUtils.isEmpty(parentObjectIdProperty)) {
            throw new IllegalArgumentException("targetObjectProperty not defined");
        }

        String joinEntityClass = null;
        if (serverMetadata != null) {
            joinEntityClass = ((AdornedTargetList) serverMetadata.getPersistenceAssociation().getPersistenceAssociationItems().get(PersistenceAssociationItemType.ADORNEDTARGETLIST)).getJoinEntityClass();
        }
        if (adornedTargetCollectionMetadata.getJoinEntityClass() != null) {
            joinEntityClass = adornedTargetCollectionMetadata.getJoinEntityClass();
        }

        Class<?> collectionTarget = null;
        try {
            checkCeiling: {
                try {
                    ParameterizedType pt = (ParameterizedType) field.getGenericType();
                    java.lang.reflect.Type collectionType = pt.getActualTypeArguments()[0];
                    String ceilingEntityName = ((Class<?>) collectionType).getName();
                    collectionTarget = entityConfiguration.lookupEntityClass(ceilingEntityName);
                    break checkCeiling;
                } catch (NoSuchBeanDefinitionException e) {
                    // We weren't successful at looking at entity configuration to find the type of this collection.
                    // We will continue and attempt to find it via the Hibernate annotations
                }
                if (!StringUtils.isEmpty(field.getOneToManyTargetEntity()) && !void.class.getName().equals(field.getOneToManyTargetEntity())) {
                    collectionTarget = Class.forName(field.getOneToManyTargetEntity());
                    break checkCeiling;
                }
                if (!StringUtils.isEmpty(field.getManyToManyTargetEntity()) && !void.class.getName().equals(field.getManyToManyTargetEntity())) {
                    collectionTarget = Class.forName(field.getManyToManyTargetEntity());
                    break checkCeiling;
                }
            }
            if (StringUtils.isNotBlank(joinEntityClass)) {
                collectionTarget = Class.forName(joinEntityClass);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (collectionTarget == null) {
            throw new IllegalArgumentException("Unable to infer the type of the collection from the targetEntity property of a OneToMany or ManyToMany collection.");
        }
        Field collectionTargetField = dynamicEntityDao.getFieldManager().getField(collectionTarget, targetObjectProperty);
        ManyToOne manyToOne = collectionTargetField.getAnnotation(ManyToOne.class);
        String ceiling = null;
        checkCeiling: {
            if (manyToOne != null && manyToOne.targetEntity() != void.class) {
                ceiling = manyToOne.targetEntity().getName();
                break checkCeiling;
            }
            ceiling = collectionTargetField.getType().getName();
        }
        if (!StringUtils.isEmpty(ceiling)) {
            metadata.setCollectionRootEntity(ceiling);
        }

        String targetObjectIdProperty = null;
        if (serverMetadata != null) {
            targetObjectIdProperty = ((AdornedTargetList) serverMetadata.getPersistenceAssociation().getPersistenceAssociationItems().get(PersistenceAssociationItemType.ADORNEDTARGETLIST)).getTargetIdProperty();
        }
        if (adornedTargetCollectionMetadata.getTargetObjectIdProperty()!=null) {
            targetObjectIdProperty = adornedTargetCollectionMetadata.getTargetObjectIdProperty();
        }
        Boolean isAscending = true;
        if (serverMetadata != null) {
            isAscending = ((AdornedTargetList) serverMetadata.getPersistenceAssociation().getPersistenceAssociationItems().get(PersistenceAssociationItemType.ADORNEDTARGETLIST)).getSortAscending();
        }
        if (adornedTargetCollectionMetadata.isSortAscending()!=null) {
            isAscending = adornedTargetCollectionMetadata.isSortAscending();
        }

        if (serverMetadata != null) {
            AdornedTargetList adornedTargetList = (AdornedTargetList) serverMetadata.getPersistenceAssociation().getPersistenceAssociationItems().get(PersistenceAssociationItemType.ADORNEDTARGETLIST);
            adornedTargetList.setCollectionFieldName(field.getName());
            adornedTargetList.setLinkedObjectPath(parentObjectProperty);
            adornedTargetList.setLinkedIdProperty(parentObjectIdProperty);
            adornedTargetList.setTargetObjectPath(targetObjectProperty);
            adornedTargetList.setTargetIdProperty(targetObjectIdProperty);
            adornedTargetList.setJoinEntityClass(joinEntityClass);
            adornedTargetList.setAdornedTargetEntityClassname(collectionTarget.getName());
            adornedTargetList.setSortField(sortProperty);
            adornedTargetList.setSortAscending(isAscending);
            adornedTargetList.setMutable(metadata.isMutable());
        } else {
            AdornedTargetList adornedTargetList = new AdornedTargetList(field.getName(), parentObjectProperty, parentObjectIdProperty, targetObjectProperty, targetObjectIdProperty, collectionTarget.getName(), sortProperty, isAscending);
            adornedTargetList.setJoinEntityClass(joinEntityClass);
            adornedTargetList.setMutable(metadata.isMutable());
            persistencePerspective.addPersistenceAssociationItem(PersistenceAssociationItemType.ADORNEDTARGETLIST, adornedTargetList);
        }

        if (adornedTargetCollectionMetadata.getExcluded() != null) {
            if (LOG.isDebugEnabled()) {
                if (adornedTargetCollectionMetadata.getExcluded()) {
                    LOG.debug("buildAdornedTargetCollectionMetadata:Excluding " + field.getName() + " because it was explicitly declared in config");
                } else {
                    LOG.debug("buildAdornedTargetCollectionMetadata:Showing " + field.getName() + " because it was explicitly declared in config");
                }
            }
            metadata.setExcluded(adornedTargetCollectionMetadata.getExcluded());
        }
        if (adornedTargetCollectionMetadata.getFriendlyName() != null) {
            metadata.setFriendlyName(adornedTargetCollectionMetadata.getFriendlyName());
        }
        if (adornedTargetCollectionMetadata.getSecurityLevel() != null) {
            metadata.setSecurityLevel(adornedTargetCollectionMetadata.getSecurityLevel());
        }
        if (adornedTargetCollectionMetadata.getOrder() != null) {
            metadata.setOrder(adornedTargetCollectionMetadata.getOrder());
        }

        if (adornedTargetCollectionMetadata.getTab() != null) {
            metadata.setTab(adornedTargetCollectionMetadata.getTab());
        }
        if (adornedTargetCollectionMetadata.getTabOrder() != null) {
            metadata.setTabOrder(adornedTargetCollectionMetadata.getTabOrder());
        }

        if (adornedTargetCollectionMetadata.getCustomCriteria() != null) {
            metadata.setCustomCriteria(adornedTargetCollectionMetadata.getCustomCriteria());
        }

        if (adornedTargetCollectionMetadata.getUseServerSideInspectionCache() != null) {
            persistencePerspective.setUseServerSideInspectionCache(adornedTargetCollectionMetadata.getUseServerSideInspectionCache());
        }

        if (adornedTargetCollectionMetadata.isIgnoreAdornedProperties() != null) {
            metadata.setIgnoreAdornedProperties(adornedTargetCollectionMetadata.isIgnoreAdornedProperties());
        }
        if (adornedTargetCollectionMetadata.getCurrencyCodeField()!=null) {
            metadata.setCurrencyCodeField(adornedTargetCollectionMetadata.getCurrencyCodeField());
        }

        attributes.put(field.getName(), metadata);
    }

    @Override
    public int getOrder() {
        return FieldMetadataProvider.ADORNED_TARGET;
    }
}

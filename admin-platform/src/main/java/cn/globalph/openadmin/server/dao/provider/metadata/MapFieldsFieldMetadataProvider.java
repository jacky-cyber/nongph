package cn.globalph.openadmin.server.dao.provider.metadata;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.presentation.AdminPresentationMapField;
import cn.globalph.common.presentation.AdminPresentationMapFields;
import cn.globalph.common.presentation.client.CustomFieldSearchableTypes;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.override.FieldMetadataOverride;
import cn.globalph.openadmin.server.dao.FieldInfo;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataFromFieldTypeRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.OverrideViaAnnotationRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.OverrideViaXmlRequest;
import cn.globalph.openadmin.server.service.persistence.module.FieldManager;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import org.hibernate.internal.TypeLocatorImpl;
import org.hibernate.type.Type;
import org.hibernate.type.TypeResolver;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * @author Jeff Fischer
 */
@Component("blMapFieldsFieldMetadataProvider")
@Scope("prototype")
public class MapFieldsFieldMetadataProvider extends DefaultFieldMetadataProvider {

    private static final Log LOG = LogFactory.getLog(MapFieldsFieldMetadataProvider.class);

    protected boolean canHandleFieldForConfiguredMetadata(AddMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata) {
        AdminPresentationMapFields annot = addMetadataRequest.getRequestedField().getAnnotation(AdminPresentationMapFields.class);
        return annot != null;
    }

    protected boolean canHandleFieldForTypeMetadata(AddMetadataFromFieldTypeRequest addMetadataFromFieldTypeRequest, Map<String, FieldMetadata> metadata) {
        AdminPresentationMapFields annot = addMetadataFromFieldTypeRequest.getRequestedField().getAnnotation(AdminPresentationMapFields.class);
        return annot != null;
    }

    @Override
    public FieldProviderResponse addMetadata(AddMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata) {
        if (!canHandleFieldForConfiguredMetadata(addMetadataRequest, metadata)) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        AdminPresentationMapFields annot = addMetadataRequest.getRequestedField().getAnnotation(AdminPresentationMapFields.class);
        for (AdminPresentationMapField mapField : annot.mapDisplayFields()) {
            if (mapField.fieldPresentation().fieldType() == SupportedFieldType.UNKNOWN) {
                throw new IllegalArgumentException("fieldType property on AdminPresentation must be set for AdminPresentationMapField");
            }
            FieldMetadataOverride override = constructBasicMetadataOverride(mapField.fieldPresentation(), null, null);
            override.setFriendlyName(mapField.fieldPresentation().friendlyName());
            FieldInfo myInfo = new FieldInfo();
            myInfo.setName(addMetadataRequest.getRequestedField().getName() + FieldManager.MAPFIELDSEPARATOR + mapField.fieldName());
            buildBasicMetadata(addMetadataRequest.getParentClass(), addMetadataRequest.getTargetClass(), metadata, myInfo, override, addMetadataRequest.getDynamicEntityDao());
            setClassOwnership(addMetadataRequest.getParentClass(), addMetadataRequest.getTargetClass(), metadata, myInfo);
            BasicFieldMetadata basicFieldMetadata = (BasicFieldMetadata) metadata.get(myInfo.getName());
            if (!mapField.targetClass().equals(Void.class)) {
                if (mapField.targetClass().isInterface()) {
                    throw new IllegalArgumentException("targetClass on @AdminPresentationMapField must be a concrete class");
                }
                basicFieldMetadata.setMapFieldValueClass(mapField.targetClass().getName());
            }
            if (mapField.searchable() != CustomFieldSearchableTypes.NOT_SPECIFIED) {
                basicFieldMetadata.setSearchable(mapField.searchable() == CustomFieldSearchableTypes.YES);
            }
            if (!StringUtils.isEmpty(mapField.manyToField())) {
                basicFieldMetadata.setManyToField(mapField.manyToField());
            }
        }
        return FieldProviderResponse.HANDLED;
    }

    @Override
    public FieldProviderResponse addMetadataFromFieldType(AddMetadataFromFieldTypeRequest req, Map<String, FieldMetadata> metadata) {
        if (!canHandleFieldForTypeMetadata(req, metadata)) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        //look for any map field metadata that was previously added for the requested field
        for (Map.Entry<String, FieldMetadata> entry : req.getPresentationAttributes().entrySet()) {
            if (entry.getKey().startsWith( req.getRequestedPropertyName() + FieldManager.MAPFIELDSEPARATOR)) {
                TypeLocatorImpl typeLocator = new TypeLocatorImpl( new TypeResolver() );

                Type myType = null;
                //first, check if an explicit type was declared
                String valueClass = ((BasicFieldMetadata) entry.getValue()).getMapFieldValueClass();
                if (valueClass != null) {
                    myType = typeLocator.entity(valueClass);
                }
                if (myType == null) {
                    SupportedFieldType fieldType = ((BasicFieldMetadata) entry.getValue()).getExplicitFieldType();
                    Class<?> basicJavaType = getBasicJavaType(fieldType);
                    if (basicJavaType != null) {
                        myType = typeLocator.basic(basicJavaType);
                    }
                }
                if (myType == null) {
                    java.lang.reflect.Type genericType = req.getRequestedField().getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        ParameterizedType pType = (ParameterizedType) genericType;
                        Class<?> clazz = (Class<?>) pType.getActualTypeArguments()[1];
                        Class<?>[] entities = req.getDynamicEntityDao().getAllPolymorphicEntitiesFromCeiling(clazz);
                        if (!ArrayUtils.isEmpty(entities)) {
                            myType = typeLocator.entity(entities[entities.length-1]);
                        }
                    }
                }
                if (myType == null) {
                       throw new IllegalArgumentException("Unable to establish the type for the property (" + entry
                               .getKey() + ")");
                }
                //add property for this map field as if it was a normal field
                super.addMetadataFromFieldType(new AddMetadataFromFieldTypeRequest(req.getRequestedField(),
                        req.getTargetClass(),
                        req.getForeignField(), 
                        req.getAdditionalForeignFields(),
                        req.getMergedPropertyType(), 
                        req.getComponentProperties(),
                        req.getIdProperty(),
                        req.getPrefix(),
                        entry.getKey(), 
                        myType, 
                        req.isPropertyForeignKey(),
                        req.getAdditionalForeignKeyIndexPosition(),
                        req.getPresentationAttributes(), entry.getValue(),
                        ((BasicFieldMetadata) entry.getValue()).getExplicitFieldType(),
                        myType.getReturnedClass(), 
                        req.getDynamicEntityDao()), 
                        metadata);
            }
        }
        return FieldProviderResponse.HANDLED;
    }

    @Override
    public FieldProviderResponse overrideViaAnnotation(OverrideViaAnnotationRequest overrideViaAnnotationRequest, Map<String, FieldMetadata> metadata) {
        //TODO support annotation override
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public FieldProviderResponse overrideViaXml(OverrideViaXmlRequest overrideViaXmlRequest, Map<String, FieldMetadata> metadata) {
        //TODO support xml override
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public int getOrder() {
        return FieldMetadataProvider.MAP_FIELD;
    }
}

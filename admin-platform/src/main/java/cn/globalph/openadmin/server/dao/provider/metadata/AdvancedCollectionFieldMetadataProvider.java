package cn.globalph.openadmin.server.dao.provider.metadata;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.AdminPresentationMap;
import cn.globalph.openadmin.dto.CollectionMetadata;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataFromFieldTypeRequest;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * @author felix.wu
 */
public class AdvancedCollectionFieldMetadataProvider extends FieldMetadataProviderAdapter {

    protected boolean canHandleFieldForTypeMetadata(AddMetadataFromFieldTypeRequest req, Map<String, FieldMetadata> metadata) {
        AdminPresentationMap map = req.getRequestedField().getAnnotation(AdminPresentationMap.class);
        AdminPresentationCollection collection = req.getRequestedField().getAnnotation(AdminPresentationCollection.class);
        return map != null || collection != null;
    }

    @Override
    public FieldProviderResponse addMetadataFromFieldType(AddMetadataFromFieldTypeRequest req, Map<String, FieldMetadata> metadata) {
        if (!canHandleFieldForTypeMetadata(req, metadata)) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        
        CollectionMetadata fieldMetadata = (CollectionMetadata) req.getPresentationAttribute();
        if ( StringUtils.isEmpty(fieldMetadata.getCollectionRootEntity()) ) {
            ParameterizedType listType = (ParameterizedType) req.getRequestedField().getGenericType();
            Class<?> listClass = (Class<?>) listType.getActualTypeArguments()[0];
            fieldMetadata.setCollectionRootEntity(listClass.getName());
        }
        if (req.getTargetClass() != null) {
            if ( StringUtils.isEmpty(fieldMetadata.getInheritedFromType() ) ) {
                fieldMetadata.setInheritedFromType( req.getTargetClass().getName() );
            }
            if (ArrayUtils.isEmpty(fieldMetadata.getAvailableToTypes())) {
                fieldMetadata.setAvailableToTypes(new String[]{req.getTargetClass().getName()});
            }
        }
        metadata.put(req.getRequestedPropertyName(), fieldMetadata);
        return FieldProviderResponse.HANDLED;
    }
}

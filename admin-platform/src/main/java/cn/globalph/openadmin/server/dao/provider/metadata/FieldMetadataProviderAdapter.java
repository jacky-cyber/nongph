package cn.globalph.openadmin.server.dao.provider.metadata;

import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataFromFieldTypeRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataFromMappingDataRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.LateStageAddMetadataRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.OverrideViaAnnotationRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.OverrideViaXmlRequest;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import org.springframework.core.Ordered;

import java.util.Map;

/**
 * @author Jeff Fischer
 */
public class FieldMetadataProviderAdapter extends AbstractFieldMetadataProvider {

    @Override
    public FieldProviderResponse addMetadata(AddMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata) {
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public FieldProviderResponse lateStageAddMetadata(LateStageAddMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata) {
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public FieldProviderResponse overrideViaAnnotation(OverrideViaAnnotationRequest overrideViaAnnotationRequest, Map<String, FieldMetadata> metadata) {
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public FieldProviderResponse overrideViaXml(OverrideViaXmlRequest overrideViaXmlRequest, Map<String, FieldMetadata> metadata) {
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public FieldProviderResponse addMetadataFromMappingData(AddMetadataFromMappingDataRequest addMetadataFromMappingDataRequest, FieldMetadata metadata) {
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public FieldProviderResponse addMetadataFromFieldType(AddMetadataFromFieldTypeRequest addMetadataFromFieldTypeRequest, Map<String, FieldMetadata> metadata) {
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

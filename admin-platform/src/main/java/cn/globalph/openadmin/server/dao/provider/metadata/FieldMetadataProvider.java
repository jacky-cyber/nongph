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
 * Classes implementing this interface are capable of manipulating metadata for a field resulting from the inspection
 * phase for the admin. Providers are typically added in response to new admin presentation annotation support.
 * Implementers should generally extend {@link FieldMetadataProviderAdapter}.
 * 展现声明解析接口
 * @author felix.wu
 */
public interface FieldMetadataProvider extends Ordered {

    //standard ordering constants for BLC providers
    public static final int BASIC = Integer.MAX_VALUE;
    public static final int COLLECTION = 20000;
    public static final int ADORNED_TARGET = 30000;
    public static final int MAP = 40000;
    public static final int MAP_FIELD = 50000;

    /**
     * Contribute to metadata inspection for the {@link java.lang.reflect.Field} instance in the request. Implementations should
     * add values to the metadata parameter.
     *
     * @param addMetadataRequest contains the requested field and support classes.
     * @param metadata implementations should add metadata for the requested field here
     * @return whether or not this implementation adjusted metadata
     */
    FieldProviderResponse addMetadata(AddMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata);
    
    /**
     * Contribute to metadata inspection for the {@link java.lang.reflect.Field} instance in the request. Implementations should
     * add values to the metadata parameter.
     * 
     * This method differs from {@link #addMetadata(AddMetadataRequest, Map)} in that it will be invoked after the cacheable
     * properties are assembled. It is therefore useful in scenarios where you may want to contribute properties to 
     * metadata that are dynamic and should not be cached normally.
     *
     * @param lateStageAddMetadataRequest contains the requested field name and support classes.
     * @param metadata implementations should add metadata for the requested field here
     * @return whether or not this implementation adjusted metadata
     */
    FieldProviderResponse lateStageAddMetadata(LateStageAddMetadataRequest addMetadataRequest, Map<String, FieldMetadata> metadata);

    /**
     * Contribute to metadata inspection for the entity in the request. Implementations should override values
     * in the metadata parameter.
     *
     * @param overrideViaAnnotationRequest contains the requested entity and support classes.
     * @param metadata implementations should override metadata here
     * @return whether or not this implementation adjusted metadata
     */
    FieldProviderResponse overrideViaAnnotation(OverrideViaAnnotationRequest overrideViaAnnotationRequest, Map<String, FieldMetadata> metadata);

    /**
     * Contribute to metadata inspection for the ceiling entity and config key. Implementations should override
     * values in the metadata parameter.
     *
     * @param overrideViaXmlRequest contains the requested config key, ceiling entity and support classes.
     * @param metadata implementations should override metadata here
     * @return whether or not this implementation adjusted metadata
     */
    FieldProviderResponse overrideViaXml(OverrideViaXmlRequest overrideViaXmlRequest, Map<String, FieldMetadata> metadata);

    /**
     * Contribute to metadata inspection using Hibernate column information. Implementations should impact values
     * in the metadata parameter.
     *
     * @param addMetadataFromMappingDataRequest contains the requested Hibernate type and support classes.
     * @param metadata implementations should impact values for the metadata for the field here
     * @return whether or not this implementation adjusted metadata
     */
    FieldProviderResponse addMetadataFromMappingData(AddMetadataFromMappingDataRequest addMetadataFromMappingDataRequest, FieldMetadata metadata);

    /**
     * Contribute to metadata inspection for the {@link java.lang.reflect.Field} instance in the request. Implementations should
     * add values to the metadata parameter. This is metadata based on the field type.
     *
     * @param addMetadataFromFieldTypeRequest contains the requested field, property name and support classes.
     * @param metadata implementations should add values for the field here
     * @return whether or not this implementation adjusted metadata
     */
    FieldProviderResponse addMetadataFromFieldType(AddMetadataFromFieldTypeRequest addMetadataFromFieldTypeRequest, Map<String, FieldMetadata> metadata);

}

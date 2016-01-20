package cn.globalph.openadmin.server.dao.provider.metadata.request;

import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;

import org.hibernate.mapping.Property;
import org.hibernate.type.Type;

import java.util.List;

/**
 * Contains the requested Hibernate type, metadata and support classes.
 *
 * @author Jeff Fischer
 */
public class AddMetadataFromMappingDataRequest {

    private final List<Property> componentProperties;
    private final SupportedFieldType type;
    private final SupportedFieldType secondaryType;
    private final Type requestedEntityType;
    private final String propertyName;
    private final MergedPropertyType mergedPropertyType;
    private final DynamicEntityDao dynamicEntityDao;

    public AddMetadataFromMappingDataRequest(List<Property> componentProperties, SupportedFieldType type, SupportedFieldType secondaryType, Type requestedEntityType, String propertyName, MergedPropertyType mergedPropertyType, DynamicEntityDao dynamicEntityDao) {
        this.componentProperties = componentProperties;
        this.type = type;
        this.secondaryType = secondaryType;
        this.requestedEntityType = requestedEntityType;
        this.propertyName = propertyName;
        this.mergedPropertyType = mergedPropertyType;
        this.dynamicEntityDao = dynamicEntityDao;
    }

    public List<Property> getComponentProperties() {
        return componentProperties;
    }

    public SupportedFieldType getType() {
        return type;
    }

    public SupportedFieldType getSecondaryType() {
        return secondaryType;
    }

    public Type getRequestedEntityType() {
        return requestedEntityType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public MergedPropertyType getMergedPropertyType() {
        return mergedPropertyType;
    }

    public DynamicEntityDao getDynamicEntityDao() {
        return dynamicEntityDao;
    }
}

package cn.globalph.openadmin.server.dao.provider.metadata.request;

import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.ForeignKey;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;

import org.hibernate.mapping.Property;
import org.hibernate.type.Type;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Contains the requested field, property name and support classes.
 * AddMetadataFromFieldType输入参数
 * @author felix.wu
 */
public class AddMetadataFromFieldTypeRequest {

    private final Field requestedField;
    private final Class<?> targetClass;
    private final ForeignKey foreignField;
    private final ForeignKey[] additionalForeignFields;
    private final MergedPropertyType mergedPropertyType;
    private final List<Property> componentProperties;
    private final String idProperty;
    private final String prefix;
    private final String requestedPropertyName;
    private final Type type;
    private final boolean propertyForeignKey;
    private final int additionalForeignKeyIndexPosition;
    private final Map<String, FieldMetadata> presentationAttributes;
    private final FieldMetadata presentationAttribute;
    private final SupportedFieldType explicitType;
    private final Class<?> returnedClass;
    private final DynamicEntityDao dynamicEntityDao;

    public AddMetadataFromFieldTypeRequest(Field requestedField, 
    		                               Class<?> targetClass, 
    		                               ForeignKey foreignField,
                                           ForeignKey[] additionalForeignFields,
                                           MergedPropertyType mergedPropertyType, 
                                           List<Property> componentProperties,
                                           String idProperty,
                                           String prefix, 
                                           String requestedPropertyName, 
                                           Type type,
                                           boolean propertyForeignKey, 
                                           int additionalForeignKeyIndexPosition,
                                           Map<String, FieldMetadata> presentationAttributes,
                                           FieldMetadata presentationAttribute, 
                                           SupportedFieldType explicitType, 
                                           Class<?> returnedClass, 
                                           DynamicEntityDao dynamicEntityDao) {
        this.requestedField = requestedField;
        this.targetClass = targetClass;
        this.foreignField = foreignField;
        this.additionalForeignFields = additionalForeignFields;
        this.mergedPropertyType = mergedPropertyType;
        this.componentProperties = componentProperties;
        this.idProperty = idProperty;
        this.prefix = prefix;
        this.requestedPropertyName = requestedPropertyName;
        this.type = type;
        this.propertyForeignKey = propertyForeignKey;
        this.additionalForeignKeyIndexPosition = additionalForeignKeyIndexPosition;
        this.presentationAttributes = presentationAttributes;
        this.presentationAttribute = presentationAttribute;
        this.explicitType = explicitType;
        this.returnedClass = returnedClass;
        this.dynamicEntityDao = dynamicEntityDao;
    }

    public Field getRequestedField() {
        return requestedField;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public ForeignKey getForeignField() {
        return foreignField;
    }

    public ForeignKey[] getAdditionalForeignFields() {
        return additionalForeignFields;
    }

    public MergedPropertyType getMergedPropertyType() {
        return mergedPropertyType;
    }

    public List<Property> getComponentProperties() {
        return componentProperties;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getRequestedPropertyName() {
        return requestedPropertyName;
    }

    public Type getType() {
        return type;
    }

    public boolean isPropertyForeignKey() {
        return propertyForeignKey;
    }

    public int getAdditionalForeignKeyIndexPosition() {
        return additionalForeignKeyIndexPosition;
    }

    public Map<String, FieldMetadata> getPresentationAttributes() {
        return presentationAttributes;
    }

    public FieldMetadata getPresentationAttribute() {
        return presentationAttribute;
    }

    public SupportedFieldType getExplicitType() {
        return explicitType;
    }

    public Class<?> getReturnedClass() {
        return returnedClass;
    }

    public DynamicEntityDao getDynamicEntityDao() {
        return dynamicEntityDao;
    }
}

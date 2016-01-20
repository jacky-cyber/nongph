package cn.globalph.cms.admin.server.handler;

import org.apache.commons.collections.CollectionUtils;

import cn.globalph.cms.field.domain.FieldDefinition;
import cn.globalph.cms.field.domain.FieldGroup;
import cn.globalph.cms.structure.domain.StructuredContentType;
import cn.globalph.common.enumeration.domain.DataDrivenEnumerationValue;
import cn.globalph.common.presentation.ConfigurationItem;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.client.VisibilityEnum;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.dto.Property;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Commonalities between {@link PageTemplateCustomPersistenceHandler} and {@link StructuredContentTypeCustomPersistenceHandler}
 * since they share similar issues in regards to dynamic fields
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Component("blDynamicFieldPersistenceHandlerHelper")
public class DynamicFieldPersistenceHandlerHelper {

    /**
     * Builds all of the metadata for all of the dynamic properties within a {@link StructuredContentType}, gleaned from
     * the {@link FieldGroup}s and {@link FieldDefinition}s.
     * 
     * @param fieldGroups groups that the {@link Property}s are built from
     * @param inheritedType the value that each built {@link FieldMetadata} for each property will use to notate where the
     * dynamic field actually came from (meaning {@link FieldMetadata#setAvailableToTypes(String[])} and {@link FieldMetadata#setInheritedFromType(String)}
     * @return
     */
    public Property[] buildDynamicPropertyList(List<FieldGroup> fieldGroups, Class<?> inheritedType) {
        List<Property> propertiesList = new ArrayList<Property>();
        int groupCount = 1;
        int fieldCount = 0;
        for (FieldGroup group : fieldGroups) {
            List<FieldDefinition> definitions = group.getFieldDefinitions();
            for (FieldDefinition definition : definitions) {
                Property property = new Property();
                property.setName(definition.getName());
                BasicFieldMetadata fieldMetadata = new BasicFieldMetadata();
                property.setFieldMetadata(fieldMetadata);
                fieldMetadata.setFieldType(definition.getFieldType());
                
                fieldMetadata.setMutable(true);
                fieldMetadata.setInheritedFromType(inheritedType.getName());
                fieldMetadata.setAvailableToTypes(new String[] {inheritedType.getName()});
                fieldMetadata.setForeignKeyCollection(false);
                fieldMetadata.setMergedPropertyType(MergedPropertyType.PRIMARY);
                fieldMetadata.setLength(definition.getMaxLength());
                if (definition.getDataDrivenEnumeration() != null && !CollectionUtils.isEmpty(definition.getDataDrivenEnumeration().getEnumValues())) {
                    int count = definition.getDataDrivenEnumeration().getEnumValues().size();
                    String[][] enumItems = new String[count][2];
                    for (int j = 0; j < count; j++) {
                        DataDrivenEnumerationValue item = definition.getDataDrivenEnumeration().getEnumValues().get(j);
                        enumItems[j][0] = item.getKey();
                        enumItems[j][1] = item.getDisplay();
                    }
                    fieldMetadata.setEnumerationValues(enumItems);
                }
                fieldMetadata.setName(definition.getName());
                fieldMetadata.setFriendlyName(definition.getFriendlyName());
                fieldMetadata.setSecurityLevel(definition.getSecurityLevel()==null?"":definition.getSecurityLevel());
                fieldMetadata.setOrder(fieldCount++);
                fieldMetadata.setVisibility(definition.getHiddenFlag()?VisibilityEnum.HIDDEN_ALL:VisibilityEnum.VISIBLE_ALL);
                fieldMetadata.setGroup(group.getName());
                fieldMetadata.setGroupOrder(groupCount);
                fieldMetadata.setTab("General");
                fieldMetadata.setTabOrder(100);
                fieldMetadata.setGroupCollapsed(group.getInitCollapsedFlag());
                fieldMetadata.setExplicitFieldType(SupportedFieldType.UNKNOWN);
                fieldMetadata.setLargeEntry(definition.getTextAreaFlag());
                fieldMetadata.setProminent(false);
                fieldMetadata.setColumnWidth(String.valueOf(definition.getColumnWidth()));
                fieldMetadata.setBroadleafEnumeration("");
                fieldMetadata.setReadOnly(false);
                fieldMetadata.setRequiredOverride(definition.getRequiredFlag());
                if (definition.getValidationRegEx() != null) {
                    Map<String, String> itemMap = new HashMap<String, String>();
                    itemMap.put("regularExpression", definition.getValidationRegEx());
                    itemMap.put(ConfigurationItem.ERROR_MESSAGE, definition.getValidationErrorMesageKey());
                    fieldMetadata.getValidationConfigurations().put("cn.globalph.openadmin.server.service.persistence.validation.RegexPropertyValidator", itemMap);
                }
                
                
                if (definition.getFieldType().equals(SupportedFieldType.ADDITIONAL_FOREIGN_KEY)) {
                    fieldMetadata.setForeignKeyClass(definition.getAdditionalForeignKeyClass());
                    fieldMetadata.setOwningClass(definition.getAdditionalForeignKeyClass());
                    fieldMetadata.setForeignKeyDisplayValueProperty("__adminMainEntity");
                }
                
                propertiesList.add(property);
            }
            groupCount++;
            fieldCount = 0;
        }
        Property property = new Property();
        property.setName("id");
        BasicFieldMetadata fieldMetadata = new BasicFieldMetadata();
        property.setFieldMetadata(fieldMetadata);
        fieldMetadata.setFieldType(SupportedFieldType.ID);
        fieldMetadata.setSecondaryType(SupportedFieldType.INTEGER);
        fieldMetadata.setMutable(true);
        fieldMetadata.setInheritedFromType(inheritedType.getName());
        fieldMetadata.setAvailableToTypes(new String[] {inheritedType.getName()});
        fieldMetadata.setForeignKeyCollection(false);
        fieldMetadata.setMergedPropertyType(MergedPropertyType.PRIMARY);
        fieldMetadata.setName("id");
        fieldMetadata.setFriendlyName("ID");
        fieldMetadata.setSecurityLevel("");
        fieldMetadata.setVisibility(VisibilityEnum.HIDDEN_ALL);
        fieldMetadata.setExplicitFieldType(SupportedFieldType.UNKNOWN);
        fieldMetadata.setLargeEntry(false);
        fieldMetadata.setProminent(false);
        fieldMetadata.setColumnWidth("*");
        fieldMetadata.setBroadleafEnumeration("");
        fieldMetadata.setReadOnly(true);
        propertiesList.add(property);

        Property[] properties = new Property[propertiesList.size()];
        properties = propertiesList.toArray(properties);
        Arrays.sort(properties, new Comparator<Property>() {
            @Override
            public int compare(Property o1, Property o2) {
                /*
                 * First, compare properties based on order fields
                 */
                if (o1.getFieldMetadata().getOrder() != null && o2.getFieldMetadata().getOrder() != null) {
                    return o1.getFieldMetadata().getOrder().compareTo(o2.getFieldMetadata().getOrder());
                } else if (o1.getFieldMetadata().getOrder() != null && o2.getFieldMetadata().getOrder() == null) {
                    /*
                     * Always favor fields that have an order identified
                     */
                    return -1;
                } else if (o1.getFieldMetadata().getOrder() == null && o2.getFieldMetadata().getOrder() != null) {
                    /*
                     * Always favor fields that have an order identified
                     */
                    return 1;
                } else if (o1.getFieldMetadata().getFriendlyName() != null && o2.getFieldMetadata().getFriendlyName() != null) {
                    return o1.getFieldMetadata().getFriendlyName().compareTo(o2.getFieldMetadata().getFriendlyName());
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            }
        });
        return properties;
    }

}

package cn.globalph.openadmin.server.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.server.dao.provider.metadata.DefaultFieldMetadataProvider;
import cn.globalph.openadmin.server.dao.provider.metadata.FieldMetadataProvider;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataFromMappingDataRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.AddMetadataRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.OverrideViaAnnotationRequest;
import cn.globalph.openadmin.server.dao.provider.metadata.request.OverrideViaXmlRequest;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import org.hibernate.mapping.Property;
import org.hibernate.type.Type;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin声明元数据帮助类
 * @author felix.wu
 */
@Component("blMetadata")
@Scope("prototype")
public class PresentationAnnoInspector {

    private static final Log LOG = LogFactory.getLog(PresentationAnnoInspector.class);

    @Resource(name="blMetadataProviders")
    protected List<FieldMetadataProvider> fieldMetadataProviders = new ArrayList<FieldMetadataProvider>();

    @Resource(name= "blDefaultFieldMetadataProvider")
    protected FieldMetadataProvider defaultFieldMetadataProvider;
    
    /**
     * 查找目标类的Presentation声明
     * @param parentClass
     * @param targetClass
     * @param dynamicEntityDao
     * @param prefix
     * @return Map<属性名，属性admin元数据>
     */
    public Map<String, FieldMetadata> getFieldPresentationAttributes(Class<?> parentClass, Class<?> targetClass, DynamicEntityDao dynamicEntityDao, String prefix) {
        Map<String, FieldMetadata> attributes = new HashMap<String, FieldMetadata>();
        Field[] fields = dynamicEntityDao.getAllFields(targetClass);
        for (Field field : fields) {
            boolean foundOneOrMoreHandlers = false;
            for (FieldMetadataProvider fieldMetadataProvider : fieldMetadataProviders) {
                FieldProviderResponse response = fieldMetadataProvider.addMetadata(new AddMetadataRequest(field, parentClass, targetClass,
                        dynamicEntityDao, prefix), attributes);
                if (FieldProviderResponse.NOT_HANDLED != response) {
                    foundOneOrMoreHandlers = true;
                }
                if (FieldProviderResponse.HANDLED_BREAK == response) {
                    break;
                }
            }
            if (!foundOneOrMoreHandlers) {
                defaultFieldMetadataProvider.addMetadata(new AddMetadataRequest(field, parentClass, targetClass,
                        dynamicEntityDao, prefix), attributes);
            }
        }
        return attributes;
    }

    public Map<String, FieldMetadata> overrideMetadata(Class<?>[] entities, 
    		                                           PropertyBuilder propertyBuilder,
    		                                           String prefix, 
    		                                           Boolean isParentExcluded, 
    		                                           String ceilingEntityClassname, 
    		                                           String configurationKey, 
    		                                           DynamicEntityDao dynamicEntityDao) {
        Boolean classAnnotatedPopulateManyToOneFields = null;
        //go in reverse order since I want the lowest subclass override to come last to guarantee that it takes effect
        //判断是否声明populateToOneFields
        for (int i = entities.length-1;i >= 0; i--) {
            AdminPresentationClass adminPresentationClass = entities[i].getAnnotation(AdminPresentationClass.class);
            if (adminPresentationClass != null && adminPresentationClass.populateToOneFields() != PopulateToOneFieldsEnum.NOT_SPECIFIED) {
                classAnnotatedPopulateManyToOneFields = adminPresentationClass.populateToOneFields()==PopulateToOneFieldsEnum.TRUE;
                break;
            }
        }

        Map<String, FieldMetadata> mergedProperties = propertyBuilder.execute(classAnnotatedPopulateManyToOneFields);
        for (int i = entities.length-1;i >= 0; i--) {
            boolean handled = false;
            for (FieldMetadataProvider fieldMetadataProvider : fieldMetadataProviders) {
            	OverrideViaAnnotationRequest ovr = new OverrideViaAnnotationRequest(entities[i], isParentExcluded, dynamicEntityDao, prefix);
                FieldProviderResponse response = fieldMetadataProvider.overrideViaAnnotation(ovr, mergedProperties);
                if (FieldProviderResponse.NOT_HANDLED != response) {
                    handled = true;
                }
                if (FieldProviderResponse.HANDLED_BREAK == response) {
                    break;
                }
            }
            if (!handled) {
            	OverrideViaAnnotationRequest ovr = new OverrideViaAnnotationRequest(entities[i], isParentExcluded, dynamicEntityDao, prefix);
                defaultFieldMetadataProvider.overrideViaAnnotation(ovr, mergedProperties);
            }
        }
        OverrideViaXmlRequest ovxr = new OverrideViaXmlRequest(configurationKey, ceilingEntityClassname, prefix, isParentExcluded, dynamicEntityDao);
        ((DefaultFieldMetadataProvider) defaultFieldMetadataProvider).overrideExclusionsFromXml(ovxr, mergedProperties);

        boolean handled = false;
        for (FieldMetadataProvider fieldMetadataProvider : fieldMetadataProviders) {
        	OverrideViaXmlRequest xr = new OverrideViaXmlRequest(configurationKey, ceilingEntityClassname, prefix,
                    isParentExcluded, dynamicEntityDao); 
            FieldProviderResponse response = fieldMetadataProvider.overrideViaXml(xr, mergedProperties);
            if (FieldProviderResponse.NOT_HANDLED != response) {
                handled = true;
            }
            if (FieldProviderResponse.HANDLED_BREAK == response) {
                break;
            }
        }
        if (!handled) {
            defaultFieldMetadataProvider.overrideViaXml(
                                new OverrideViaXmlRequest(configurationKey, ceilingEntityClassname, prefix,
                                        isParentExcluded, dynamicEntityDao), mergedProperties);
        }

        return mergedProperties;
    }

    public FieldMetadata getFieldMetadata(
        String prefix,
        String propertyName,
        List<Property> componentProperties,
        SupportedFieldType type,
        Type entityType,
        Class<?> targetClass,
        FieldMetadata presentationAttribute,
        MergedPropertyType mergedPropertyType,
        DynamicEntityDao dynamicEntityDao
    ) {
        return getFieldMetadata(prefix, propertyName, componentProperties, type, null, entityType, targetClass, presentationAttribute, mergedPropertyType, dynamicEntityDao);
    }

    public FieldMetadata getFieldMetadata(
        String prefix,
        final String propertyName,
        final List<Property> componentProperties,
        final SupportedFieldType type,
        final SupportedFieldType secondaryType,
        final Type entityType,
        Class<?> targetClass,
        final FieldMetadata presentationAttribute,
        final MergedPropertyType mergedPropertyType,
        final DynamicEntityDao dynamicEntityDao
    ) {
        if (presentationAttribute.getTargetClass() == null) {
            presentationAttribute.setTargetClass(targetClass.getName());
            presentationAttribute.setFieldName(propertyName);
        }
        presentationAttribute.setInheritedFromType(targetClass.getName());
        presentationAttribute.setAvailableToTypes(new String[]{targetClass.getName()});
        boolean handled = false;
        for (FieldMetadataProvider fieldMetadataProvider : fieldMetadataProviders) {
            FieldProviderResponse response = fieldMetadataProvider.addMetadataFromMappingData(new AddMetadataFromMappingDataRequest(
                componentProperties, type, secondaryType, entityType, propertyName, mergedPropertyType, dynamicEntityDao), presentationAttribute);
            if (FieldProviderResponse.NOT_HANDLED != response) {
                handled = true;
            }
            if (FieldProviderResponse.HANDLED_BREAK == response) {
                break;
            }
        }
        if (!handled) {
            defaultFieldMetadataProvider.addMetadataFromMappingData(new AddMetadataFromMappingDataRequest(
                    componentProperties, type, secondaryType, entityType, propertyName, mergedPropertyType, dynamicEntityDao), presentationAttribute);
        }

        return presentationAttribute;
    }

    public FieldMetadataProvider getDefaultFieldMetadataProvider() {
        return defaultFieldMetadataProvider;
    }

    public void setDefaultFieldMetadataProvider(FieldMetadataProvider defaultFieldMetadataProvider) {
        this.defaultFieldMetadataProvider = defaultFieldMetadataProvider;
    }

    public List<FieldMetadataProvider> getFieldMetadataProviders() {
        return fieldMetadataProviders;
    }

    public void setFieldMetadataProviders(List<FieldMetadataProvider> fieldMetadataProviders) {
        this.fieldMetadataProviders = fieldMetadataProviders;
    }
}

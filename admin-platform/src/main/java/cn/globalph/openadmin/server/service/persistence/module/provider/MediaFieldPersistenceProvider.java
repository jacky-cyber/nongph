package cn.globalph.openadmin.server.service.persistence.module.provider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;

import cn.globalph.common.media.domain.Media;
import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.sandbox.SandBoxHelper;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationException;
import cn.globalph.openadmin.server.service.persistence.module.FieldManager;
import cn.globalph.openadmin.server.service.persistence.module.FieldNotAvailableException;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.AddFilterPropertiesRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.PopulateValueRequest;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Brian Polster
 */
@Component("blMediaFieldPersistenceProvider")
@Scope("prototype")
public class MediaFieldPersistenceProvider extends FieldPersistenceProviderAdapter {
    
    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Resource(name="blSandBoxHelper")
    protected SandBoxHelper sandBoxHelper;

    protected boolean canHandlePersistence(PopulateValueRequest populateValueRequest, Serializable instance) {
        return populateValueRequest.getMetadata().getFieldType() == SupportedFieldType.MEDIA;
    }

    protected boolean canHandleExtraction(ExtractValueRequest extractValueRequest, Property property) {
        return extractValueRequest.getMetadata().getFieldType() == SupportedFieldType.MEDIA;
    }

    @Override
    public FieldProviderResponse populateValue(PopulateValueRequest populateValueRequest, Serializable instance) throws PersistenceOperationException {
        if (!canHandlePersistence(populateValueRequest, instance)) {
            return FieldProviderResponse.NOT_HANDLED;
        }
        FieldProviderResponse response = FieldProviderResponse.HANDLED;
        boolean dirty = false;
        try {
            setNonDisplayableValues(populateValueRequest);
            Class<?> valueType = null;
            if (!populateValueRequest.getProperty().getName().contains(FieldManager.MAPFIELDSEPARATOR)) {
                valueType = populateValueRequest.getReturnType();
            } else {
                String valueClassName = populateValueRequest.getMetadata().getMapFieldValueClass();
                if (valueClassName != null) {
                    valueType = Class.forName(valueClassName);
                }
                if (valueType == null) {
                    valueType = populateValueRequest.getReturnType();
                }
            }

            if (valueType == null) {
                throw new IllegalAccessException("Unable to determine the valueType for the rule field (" + populateValueRequest.getProperty().getName() + ")");
            }
        
            if (Media.class.isAssignableFrom(valueType)) {
                Media newMedia = convertJsonToMedia(populateValueRequest.getProperty().getUnHtmlEncodedValue());
                Media media;
                try {
                    media = (Media) populateValueRequest.getFieldManager().getFieldValue(instance,
                            populateValueRequest.getProperty().getName());
                } catch (FieldNotAvailableException e) {
                    throw new IllegalArgumentException(e);
                }
                populateValueRequest.getProperty().setOriginalValue(convertMediaToJson(media));

                boolean persist = false;
                if (media == null) {
                    media = (Media) valueType.newInstance();
                    persist = true;
                }

                Map description = BeanUtils.describe(media);
                for (Object temp : description.keySet()) {
                    String property = (String) temp;
                    //ignore id and SandBoxDiscriminator fields
                    String[] ignoredProperties = sandBoxHelper.getSandBoxDiscriminatorFieldList();
                    ignoredProperties = (String[]) ArrayUtils.add(ignoredProperties, "id");
                    Arrays.sort(ignoredProperties);
                    if (Arrays.binarySearch(ignoredProperties, property) < 0) {
                        String prop1 = String.valueOf(description.get(property));
                        String prop2 = String.valueOf(BeanUtils.getProperty(newMedia, property));
                        if (!prop1.equals(prop2)) {
                            dirty = true;
                            break;
                        }
                    }
                }

                if (dirty) {
                    updateMediaFields(media, newMedia);
                    if (persist) {
                        populateValueRequest.getPersistenceManager().getDynamicEntityDao().persist(media);
                    }
                    populateValueRequest.getFieldManager().setFieldValue(instance,
                            populateValueRequest.getProperty().getName(), media);
		            response = FieldProviderResponse.HANDLED_BREAK;
                }
            } else {
                throw new UnsupportedOperationException("MediaFields only work with Media types.");
            }
        } catch (Exception e) {
            throw new PersistenceOperationException(e);
        }
        populateValueRequest.getProperty().setIsDirty(dirty);

        return response;
    }

    @Override
    public FieldProviderResponse extractValue(ExtractValueRequest extractValueRequest, Property property) throws PersistenceOperationException {
        if (!canHandleExtraction(extractValueRequest, property)) {
            return FieldProviderResponse.NOT_HANDLED;
        }

        if (extractValueRequest.getRequestedValue() != null) {
            if (extractValueRequest.getRequestedValue() instanceof Media) {
                Media media = (Media) extractValueRequest.getRequestedValue();
                String jsonString = convertMediaToJson(media);
                property.setValue(jsonString);
                property.setUnHtmlEncodedValue(jsonString);
                property.setDisplayValue(extractValueRequest.getDisplayVal());
                return FieldProviderResponse.HANDLED_BREAK;
            } else {
                throw new UnsupportedOperationException("MEDIA type is currently only supported on fields of type Media");
            }
        }
        return FieldProviderResponse.HANDLED;
    }

    @Override
    public FieldProviderResponse filterProperties(AddFilterPropertiesRequest addFilterPropertiesRequest, Map<String, FieldMetadata> properties) {
        // BP:  Basically copied this from RuleFieldPersistenceProvider
        List<Property> propertyList = new ArrayList<Property>();
        propertyList.addAll(Arrays.asList(addFilterPropertiesRequest.getEntity().getProperties()));
        Iterator<Property> itr = propertyList.iterator();
        List<Property> additionalProperties = new ArrayList<Property>();
        while(itr.hasNext()) {
            Property prop = itr.next();
            if (prop.getName().endsWith("Json")) {
                for (Map.Entry<String, FieldMetadata> entry : properties.entrySet()) {
                    if (prop.getName().startsWith(entry.getKey())) {
                        BasicFieldMetadata originalFM = (BasicFieldMetadata) entry.getValue();
                        if (originalFM.getFieldType() == SupportedFieldType.MEDIA) {
                            Property originalProp = addFilterPropertiesRequest.getEntity().findProperty(originalFM
                                    .getName());
                            if (originalProp == null) {
                                originalProp = new Property();
                                originalProp.setName(originalFM.getName());
                                additionalProperties.add(originalProp);
                            }
                            originalProp.setValue(prop.getValue());
                            originalProp.setRawValue(prop.getRawValue());
                            originalProp.setUnHtmlEncodedValue(prop.getUnHtmlEncodedValue());
                            itr.remove();
                            break;
                        }
                    }
                }
            }
        }
        propertyList.addAll(additionalProperties);
        addFilterPropertiesRequest.getEntity().setProperties(propertyList.toArray(new Property[propertyList.size()]));
        return FieldProviderResponse.HANDLED;
    }

    @Override
    public int getOrder() {
        return FieldPersistenceProvider.MEDIA;
    }

    protected String convertMediaToJson(Media media) {
        String json;
        try {
            ObjectMapper om = new ObjectMapper();
            return om.writeValueAsString(media);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    protected Media convertJsonToMedia(String jsonProp) {
        try {
            ObjectMapper om = new ObjectMapper();
            return om.readValue(jsonProp, entityConfiguration.lookupEntityClass(Media.class.getName(), Media.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void updateMediaFields(Media oldMedia, Media newMedia) {
        oldMedia.setAltText(newMedia.getAltText());
        oldMedia.setTags(newMedia.getTags());
        oldMedia.setTitle(newMedia.getTitle());
        oldMedia.setUrl(newMedia.getUrl());
    }
}

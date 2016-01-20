package cn.globalph.openadmin.server.service.persistence.module.provider;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import cn.globalph.common.value.Searchable;
import cn.globalph.common.value.ValueAssignable;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.dao.FieldInfo;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationException;
import cn.globalph.openadmin.server.service.persistence.module.FieldManager;
import cn.globalph.openadmin.server.service.persistence.module.FieldNotAvailableException;
import cn.globalph.openadmin.server.service.persistence.module.criteria.FilterMapping;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.AddSearchMappingRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.ExtractValueRequest;
import cn.globalph.openadmin.server.service.persistence.module.provider.request.PopulateValueRequest;
import cn.globalph.openadmin.server.service.type.FieldProviderResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Jeff Fischer
 */
@Component("blMapFieldPersistenceProvider")
@Scope("prototype")
public class MapFieldPersistenceProvider extends BasicFieldPersistenceProvider {

    @Override
    protected boolean canHandlePersistence(PopulateValueRequest populateValueRequest, Serializable instance) {
        return populateValueRequest.getProperty().getName().contains(FieldManager.MAPFIELDSEPARATOR);
    }

    @Override
    protected boolean canHandleExtraction(ExtractValueRequest extractValueRequest, Property property) {
        return property.getName().contains(FieldManager.MAPFIELDSEPARATOR);
    }

    @Override
    public FieldProviderResponse populateValue(PopulateValueRequest populateValueRequest, Serializable instance) {
        boolean dirty = false;
        try {
            //handle some additional field settings (if applicable)
            Class<?> valueType = null;
            String valueClassName = populateValueRequest.getMetadata().getMapFieldValueClass();
            if (valueClassName != null) {
                valueType = Class.forName(valueClassName);
            }
            if (valueType == null) {
                valueType = populateValueRequest.getReturnType();
            }
            if (valueType == null) {
                throw new IllegalAccessException("Unable to determine the valueType for the rule field (" + populateValueRequest.getProperty().getName() + ")");
            }
            if (ValueAssignable.class.isAssignableFrom(valueType)) {
                ValueAssignable assignableValue;
                try {
                    assignableValue = (ValueAssignable) populateValueRequest.getFieldManager().getFieldValue(instance, populateValueRequest.getProperty().getName());
                } catch (FieldNotAvailableException e) {
                    throw new IllegalArgumentException(e);
                }
                String key = populateValueRequest.getProperty().getName().substring(populateValueRequest.getProperty().getName().indexOf(FieldManager.MAPFIELDSEPARATOR) + FieldManager.MAPFIELDSEPARATOR.length(), populateValueRequest.getProperty().getName().length());
                boolean persistValue = false;
                if (assignableValue == null) {
                    assignableValue = (ValueAssignable) valueType.newInstance();
                    persistValue = true;
                    dirty = true;
                } else {
                    dirty = assignableValue.getValue().equals(populateValueRequest.getProperty().getValue());
                    populateValueRequest.getProperty().setOriginalValue(String.valueOf(assignableValue));
                    populateValueRequest.getProperty().setOriginalDisplayValue(String.valueOf(assignableValue));
                }
                assignableValue.setName(key);
                assignableValue.setValue(populateValueRequest.getProperty().getValue());
                String fieldName = populateValueRequest.getProperty().getName().substring(0, populateValueRequest.getProperty().getName().indexOf(FieldManager.MAPFIELDSEPARATOR));
                Field field = populateValueRequest.getFieldManager().getField(instance.getClass(), fieldName);
                FieldInfo fieldInfo = buildFieldInfo(field);
                String manyToField = null;
                if (populateValueRequest.getMetadata().getManyToField() != null) {
                    manyToField = populateValueRequest.getMetadata().getManyToField();
                }
                if (manyToField == null) {
                    manyToField = fieldInfo.getManyToManyMappedBy();
                }
                if (manyToField == null) {
                    manyToField = fieldInfo.getOneToManyMappedBy();
                }
                if (manyToField != null) {
                    String propertyName = populateValueRequest.getProperty().getName();
                    Object middleInstance = instance;
                    if (propertyName.contains(".")) {
                        propertyName = propertyName.substring(0, propertyName.lastIndexOf("."));
                        middleInstance = populateValueRequest.getFieldManager().getFieldValue(instance, propertyName);
                    }
                    populateValueRequest.getFieldManager().setFieldValue(assignableValue, manyToField, middleInstance);
                }
                if (Searchable.class.isAssignableFrom(valueType)) {
                    ((Searchable) assignableValue).setSearchable(populateValueRequest.getMetadata().getSearchable());
                }
                if (persistValue) {
                    populateValueRequest.getPersistenceManager().getDynamicEntityDao().persist(assignableValue);
                    populateValueRequest.getFieldManager().setFieldValue(instance, populateValueRequest.getProperty().getName(), assignableValue);
                }
            } else {
                //handle the map value set itself
                if (FieldProviderResponse.NOT_HANDLED==super.populateValue(populateValueRequest, instance)) {
                    return FieldProviderResponse.NOT_HANDLED;
                }
            }
        } catch (Exception e) {
            throw new PersistenceOperationException(e);
        }
        populateValueRequest.getProperty().setIsDirty(dirty);
        return FieldProviderResponse.HANDLED_BREAK;
    }

    @Override
    public FieldProviderResponse extractValue(ExtractValueRequest extractValueRequest, Property property) throws PersistenceOperationException {
        if (extractValueRequest.getRequestedValue() != null && extractValueRequest.getRequestedValue() instanceof ValueAssignable) {
            ValueAssignable assignableValue = (ValueAssignable) extractValueRequest.getRequestedValue();
            String val = (String) assignableValue.getValue();
            property.setValue(val);
            property.setDisplayValue(extractValueRequest.getDisplayVal());
        } else {
            if (FieldProviderResponse.NOT_HANDLED==super.extractValue(extractValueRequest, property)) {
                return FieldProviderResponse.NOT_HANDLED;
            }
        }
        return FieldProviderResponse.HANDLED;
    }

    @Override
    public FieldProviderResponse addSearchMapping(AddSearchMappingRequest addSearchMappingRequest, List<FilterMapping> filterMappings) {
        return FieldProviderResponse.NOT_HANDLED;
    }

    @Override
    public int getOrder() {
        return FieldPersistenceProvider.MAP_FIELD;
    }
}

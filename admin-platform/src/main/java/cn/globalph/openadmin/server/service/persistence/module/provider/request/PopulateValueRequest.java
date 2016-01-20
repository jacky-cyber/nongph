package cn.globalph.openadmin.server.service.persistence.module.provider.request;

import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationExecutor;
import cn.globalph.openadmin.server.service.persistence.module.DataFormatProvider;
import cn.globalph.openadmin.server.service.persistence.module.FieldManager;

/**
 * Contains the requested value, instance and support classes.
 *
 * @author Jeff Fischer
 */
public class PopulateValueRequest {

    private final Boolean setId;
    private final FieldManager fieldManager;
    private final Property property;
    private final BasicFieldMetadata metadata;
    private final Class<?> returnType;
    private final String requestedValue;
    private final PersistenceOperationExecutor persistenceManager;
    private final DataFormatProvider dataFormatProvider;

    public PopulateValueRequest(Boolean setId, FieldManager fieldManager, Property property, BasicFieldMetadata metadata, Class<?> returnType, String requestedValue, PersistenceOperationExecutor persistenceManager, DataFormatProvider dataFormatProvider) {
        this.setId = setId;
        this.fieldManager = fieldManager;
        this.property = property;
        this.metadata = metadata;
        this.returnType = returnType;
        this.requestedValue = requestedValue;
        this.persistenceManager = persistenceManager;
        this.dataFormatProvider = dataFormatProvider;
    }

    public Boolean getSetId() {
        return setId;
    }

    public FieldManager getFieldManager() {
        return fieldManager;
    }

    public Property getProperty() {
        return property;
    }

    public BasicFieldMetadata getMetadata() {
        return metadata;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public String getRequestedValue() {
        return requestedValue;
    }

    public PersistenceOperationExecutor getPersistenceManager() {
        return persistenceManager;
    }

    public DataFormatProvider getDataFormatProvider() {
        return dataFormatProvider;
    }
}

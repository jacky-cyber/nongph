package cn.globalph.openadmin.server.service.persistence.module.provider.request;

import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationExecutor;
import cn.globalph.openadmin.server.service.persistence.module.DataFormatProvider;
import cn.globalph.openadmin.server.service.persistence.module.FieldManager;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;

import java.io.Serializable;
import java.util.List;

/**
 * Contains the requested value, property and support classes.
 *
 * @author Jeff Fischer
 */
public class ExtractValueRequest {

    protected final List<Property> props;
    protected final FieldManager fieldManager;
    protected final BasicFieldMetadata metadata;
    protected final Object requestedValue;
    protected String displayVal;
    protected final PersistenceOperationExecutor persistenceManager;
    protected final RecordHelper recordHelper;
    protected final Serializable entity;

    public ExtractValueRequest(List<Property> props, FieldManager fieldManager, BasicFieldMetadata metadata, 
            Object requestedValue, String displayVal, PersistenceOperationExecutor persistenceManager, 
            RecordHelper recordHelper, Serializable entity) {
        this.props = props;
        this.fieldManager = fieldManager;
        this.metadata = metadata;
        this.requestedValue = requestedValue;
        this.displayVal = displayVal;
        this.persistenceManager = persistenceManager;
        this.recordHelper = recordHelper;
        this.entity = entity;
    }

    public List<Property> getProps() {
        return props;
    }

    public FieldManager getFieldManager() {
        return fieldManager;
    }

    public BasicFieldMetadata getMetadata() {
        return metadata;
    }

    public Object getRequestedValue() {
        return requestedValue;
    }

    public String getDisplayVal() {
        return displayVal;
    }

    public PersistenceOperationExecutor getPersistenceManager() {
        return persistenceManager;
    }

    public DataFormatProvider getDataFormatProvider() {
        return recordHelper;
    }
    
    public RecordHelper getRecordHelper() {
        return recordHelper;
    }

    public void setDisplayVal(String displayVal) {
        this.displayVal = displayVal;
    }
    
    public Serializable getEntity() {
        return entity;
    }
    
}

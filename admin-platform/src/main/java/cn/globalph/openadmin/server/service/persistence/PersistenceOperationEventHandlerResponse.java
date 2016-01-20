package cn.globalph.openadmin.server.service.persistence;

import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author felix.wu
 */
public class PersistenceOperationEventHandlerResponse {
	public static enum ResponseStatus {
        HANDLED,NOT_HANDLED,HANDLED_BREAK
    }

    protected Entity entity;
    protected DynamicResultSet dynamicResultSet;
    protected ResponseStatus status;
    protected Map<String, Object> additionalData = new HashMap<String, Object>();
    
    public PersistenceOperationEventHandlerResponse withEntity(Entity entity) {
        setEntity(entity);
        return this;
    }

    public PersistenceOperationEventHandlerResponse withStatus(ResponseStatus status) {
        setStatus(status);
        return this;
    }

    public PersistenceOperationEventHandlerResponse withDynamicResultSet(DynamicResultSet dynamicResultSet) {
        setDynamicResultSet(dynamicResultSet);
        return this;
    }

    public PersistenceOperationEventHandlerResponse withAdditionalData(Map<String, Object> additionalData) {
        setAdditionalData(additionalData);
        return this;
    }

    public DynamicResultSet getDynamicResultSet() {
        return dynamicResultSet;
    }

    public void setDynamicResultSet(DynamicResultSet dynamicResultSet) {
        this.dynamicResultSet = dynamicResultSet;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }
}

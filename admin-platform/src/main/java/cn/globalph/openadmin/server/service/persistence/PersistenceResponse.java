package cn.globalph.openadmin.server.service.persistence;

import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 持久操作响应
 * @author fwu
 *
 */
public class PersistenceResponse {

    protected DynamicResultSet dynamicResultSet;
    protected Entity entity;
    protected Map<String, Object> additionalData = new HashMap<String, Object>();

    public PersistenceResponse withDynamicResultSet(DynamicResultSet dynamicResultSet) {
        setDynamicResultSet(dynamicResultSet);
        return this;
    }

    public PersistenceResponse withEntity(Entity entity) {
        setEntity(entity);
        return this;
    }

    public PersistenceResponse withAdditionalData(Map<String, Object> additionalData) {
        setAdditionalData(additionalData);
        return this;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
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

    public static class AdditionalData {
        public static final String CLONEID = "cloneId";
    }
}

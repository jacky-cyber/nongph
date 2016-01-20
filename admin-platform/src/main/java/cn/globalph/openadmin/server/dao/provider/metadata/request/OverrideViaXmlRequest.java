package cn.globalph.openadmin.server.dao.provider.metadata.request;

import cn.globalph.openadmin.server.dao.DynamicEntityDao;

/**
 * Contains the requested config key, ceiling entity, metadata and support classes.
 *
 * @author Jeff Fischer
 */
public class OverrideViaXmlRequest {

    private final String requestedConfigKey;
    private final String requestedCeilingEntity;
    private final String prefix;
    private final Boolean parentExcluded;
    private final DynamicEntityDao dynamicEntityDao;

    public OverrideViaXmlRequest(String requestedConfigKey, String requestedCeilingEntity, String prefix, Boolean parentExcluded, DynamicEntityDao dynamicEntityDao) {
        this.requestedConfigKey = requestedConfigKey;
        this.requestedCeilingEntity = requestedCeilingEntity;
        this.prefix = prefix;
        this.parentExcluded = parentExcluded;
        this.dynamicEntityDao = dynamicEntityDao;
    }

    public String getRequestedConfigKey() {
        return requestedConfigKey;
    }

    public String getRequestedCeilingEntity() {
        return requestedCeilingEntity;
    }

    public String getPrefix() {
        return prefix;
    }

    public Boolean getParentExcluded() {
        return parentExcluded;
    }

    public DynamicEntityDao getDynamicEntityDao() {
        return dynamicEntityDao;
    }
}

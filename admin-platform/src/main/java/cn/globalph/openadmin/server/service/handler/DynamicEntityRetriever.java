package cn.globalph.openadmin.server.service.handler;

import java.io.Serializable;
import java.util.List;

import cn.globalph.openadmin.dto.Entity;

/**
 * @author Jeff Fischer
 */
public interface DynamicEntityRetriever {

    Entity fetchDynamicEntity(Serializable root, List<String> dirtyFields, boolean includeId) throws Exception;

    Entity fetchEntityBasedOnId(String themeConfigurationId, List<String> dirtyFields) throws Exception;

    String getFieldContainerClassName();

}

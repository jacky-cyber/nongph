package cn.globalph.common.cache.engine;

import java.io.Serializable;

/**
 * 
 * @felix.wu
 *
 */
public interface HydratedCacheManager {

    public Object getHydratedCacheElementItem(String cacheRegion, String cacheName, Serializable elementKey, String elementItemName);

    public void addHydratedCacheElementItem(String cacheRegion, String cacheName, Serializable elementKey, String elementItemName, Object elementValue);
    
}

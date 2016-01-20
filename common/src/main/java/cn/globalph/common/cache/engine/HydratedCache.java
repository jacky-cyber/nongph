package cn.globalph.common.cache.engine;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * 
 * @felix.wu
 *
 */
@Deprecated
public class HydratedCache extends Hashtable<String, Object> {

    private static final long serialVersionUID = 1L;

    private String cacheName;
    private String cacheRegion;

    public HydratedCache(String cacheRegion, String cacheName) {
        this.cacheRegion = cacheRegion;
        this.cacheName = cacheName;
    }

    public String getCacheName() {
        return cacheName;
    }

    public String getCacheRegion() {
        return cacheRegion;
    }

    public void setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
    }

    public HydratedCacheElement getCacheElement(String cacheRegion, String cacheName, Serializable key) {
        return (HydratedCacheElement) get(cacheRegion + "_" + cacheName + "_" + key);
    }
    
    public HydratedCacheElement removeCacheElement(String cacheRegion, String cacheName, Serializable key) {
        String myKey = cacheRegion + "_" + cacheName + "_" + key;
        return (HydratedCacheElement) remove(myKey);
    }
    
    public void addCacheElement(String cacheRegion, String cacheName, Serializable key, Object value) {
        String myKey = cacheRegion + "_" + cacheName + "_" + key;
        put(myKey, value);
    }
}

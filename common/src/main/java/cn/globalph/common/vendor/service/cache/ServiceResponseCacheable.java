package cn.globalph.common.vendor.service.cache;

import net.sf.ehcache.Cache;


/**
 * @felix.wu
 *
 */
public interface ServiceResponseCacheable {
    
    public void clearCache();
    
    public Cache getCache();

}

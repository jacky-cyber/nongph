package cn.globalph.b2c.web.service;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;


/**
 * @author bpolster
 */
public interface CacheKeyResolverExtensionHandler extends ExtensionHandler {

    /**
     * The passed in StringBuilder represents the current state of the cache key prior
     * to running any extension handlers.
     * 
     * Any implementations of this processor can read modify the passed in stringBuilder as
     * needed.    
     * 
     * @param stringBuilder
     * @param hasProducts
     * @return
     */
    public ExtensionResultStatusType updateCacheKey(StringBuilder stringBuilder, boolean hasProducts);
}

package cn.globalph.b2c.web.service;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * @author bpolster
 */
@Service("blCacheKeyResolverExtensionManager")
public class CacheKeyResolverExtensionManager extends ExtensionManager<CacheKeyResolverExtensionHandler> {
    
    public CacheKeyResolverExtensionManager() {
        super(CacheKeyResolverExtensionHandler.class);
    }
}

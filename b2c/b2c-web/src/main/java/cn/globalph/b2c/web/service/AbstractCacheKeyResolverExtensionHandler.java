package cn.globalph.b2c.web.service;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;


public class AbstractCacheKeyResolverExtensionHandler extends AbstractExtensionHandler implements
        CacheKeyResolverExtensionHandler {


    @Override
    public ExtensionResultStatusType updateCacheKey(StringBuilder stringBuilder, boolean hasProducts) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}

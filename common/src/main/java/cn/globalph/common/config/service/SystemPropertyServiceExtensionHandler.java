package cn.globalph.common.config.service;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;


/**
 * @author bpolster
 */
public interface SystemPropertyServiceExtensionHandler extends ExtensionHandler {
    
    /**
     * Provides an opportunity for modules to resolve a system property.
     * 
     * @param propertyName
     * @param resultHolder
     * @return
     */
    public ExtensionResultStatusType resolveProperty(String propertyName, ExtensionResultHolder resultHolder);
    
}

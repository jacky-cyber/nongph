package cn.globalph.common.config.service;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;


/**
 * @author bpolster
 */
public abstract class AbstractSystemPropertyServiceExtensionHandler extends AbstractExtensionHandler implements
        SystemPropertyServiceExtensionHandler {

    @Override
    public ExtensionResultStatusType resolveProperty(String propertyName, ExtensionResultHolder resultHolder) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
}

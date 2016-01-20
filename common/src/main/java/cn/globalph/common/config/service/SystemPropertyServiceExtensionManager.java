package cn.globalph.common.config.service;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * @author bpolster
 */
@Service("blSystemPropertyServiceExtensionManager")
public class SystemPropertyServiceExtensionManager extends ExtensionManager<SystemPropertyServiceExtensionHandler> {

    public SystemPropertyServiceExtensionManager() {
        super(SystemPropertyServiceExtensionHandler.class);
    }

    /**
     * The first "handler" to "handle" the request wins.
     */
    public boolean continueOnHandled() {
        return false;
    }
}

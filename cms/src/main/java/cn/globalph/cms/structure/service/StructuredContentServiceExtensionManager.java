package cn.globalph.cms.structure.service;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * 
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
@Service("blStructuredContentServiceExtensionManager")
public class StructuredContentServiceExtensionManager extends ExtensionManager<StructuredContentServiceExtensionHandler> {

    public StructuredContentServiceExtensionManager() {
        super(StructuredContentServiceExtensionHandler.class);
    }

    @Override
    public boolean continueOnHandled() {
        return true;
    }
    
}

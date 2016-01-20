package cn.globalph.common.web.resource;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

@Service("blResourceRequestExtensionManager")
public class ResourceRequestExtensionManager extends ExtensionManager<ResourceRequestExtensionHandler>{

    public ResourceRequestExtensionManager() {
        super(ResourceRequestExtensionHandler.class);
    }

    /**
     * The first handler to return a handled status will win
     */
    @Override
    public boolean continueOnHandled() {
        return false;
    }

}

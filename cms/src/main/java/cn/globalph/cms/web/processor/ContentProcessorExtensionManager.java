package cn.globalph.cms.web.processor;

import cn.globalph.common.extension.ExtensionManager;

/**
 * @author Elbert Bautista (elbertbautista)
 */
public class ContentProcessorExtensionManager extends ExtensionManager<ContentProcessorExtensionHandler> {

    public ContentProcessorExtensionManager() {
        super(ContentProcessorExtensionHandler.class);
    }

    @Override
    public boolean continueOnHandled() {
        return true;
    }

}

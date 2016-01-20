package cn.globalph.common.web;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * @author Andre Azzolini (apazzolini)
 */
@Service("blBroadleafThymeleafViewResolverExtensionManager")
public class BroadleafThymeleafViewResolverExtensionManager extends ExtensionManager<ThymeleafViewResolverExtensionHandler> {

    public BroadleafThymeleafViewResolverExtensionManager() {
        super(ThymeleafViewResolverExtensionHandler.class);
    }

    /**
     * By default, this manager will allow other handlers to process the method when a handler returns
     * HANDLED.
     */
    @Override
    public boolean continueOnHandled() {
        return true;
    }
}

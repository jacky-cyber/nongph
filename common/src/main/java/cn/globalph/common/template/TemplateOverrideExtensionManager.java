package cn.globalph.common.template;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

/**
 * @author Andre Azzolini (apazzolini)
 */
@Service("blTemplateOverrideExtensionManager")
public class TemplateOverrideExtensionManager extends ExtensionManager<TemplateOverrideExtensionHandler> {
    
    public TemplateOverrideExtensionManager() {
        super(TemplateOverrideExtensionHandler.class);
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

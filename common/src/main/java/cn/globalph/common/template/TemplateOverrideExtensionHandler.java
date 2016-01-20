package cn.globalph.common.template;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

/**
 * Certain objects may have templates that resolve differently based on Broadleaf modules. This extension handler
 * provides the abilities for modules to provide that functionality.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public interface TemplateOverrideExtensionHandler extends ExtensionHandler {
    
    public ExtensionResultStatusType getOverrideTemplate(ExtensionResultHolder<String> erh, Object object);


}

package cn.globalph.common.template;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;


/**
 * @author Andre Azzolini (apazzolini)
 */
public abstract class AbstractTemplateOverrideExtensionHandler extends AbstractExtensionHandler implements TemplateOverrideExtensionHandler {
    
    @Override
    public ExtensionResultStatusType getOverrideTemplate(ExtensionResultHolder<String> erh, Object object) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}

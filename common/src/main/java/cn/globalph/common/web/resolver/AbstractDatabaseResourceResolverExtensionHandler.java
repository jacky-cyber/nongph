package cn.globalph.common.web.resolver;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

import org.thymeleaf.TemplateProcessingParameters;


/**
 * @author Andre Azzolini (apazzolini)
 */
public abstract class AbstractDatabaseResourceResolverExtensionHandler extends AbstractExtensionHandler 
        implements DatabaseResourceResolverExtensionHandler {
    
    public ExtensionResultStatusType resolveResource(ExtensionResultHolder erh, 
            TemplateProcessingParameters params, String resourceName) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}

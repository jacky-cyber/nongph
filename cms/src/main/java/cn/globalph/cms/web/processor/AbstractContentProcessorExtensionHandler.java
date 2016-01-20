package cn.globalph.cms.web.processor;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.common.web.deeplink.DeepLink;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

import java.util.List;

/**
 * Abstract implementation of {@link ContentProcessorExtensionHandler}
 * 
 * @author Andre Azzolini (apazzolini)
 */
public abstract class AbstractContentProcessorExtensionHandler extends AbstractExtensionHandler 
        implements ContentProcessorExtensionHandler {

    @Override
    public ExtensionResultStatusType addAdditionalFieldsToModel(Arguments arguments, Element element) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType addExtensionFieldDeepLink(List<DeepLink> links, Arguments arguments, Element element) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    
    @Override
    public ExtensionResultStatusType postProcessDeepLinks(List<DeepLink> links) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}

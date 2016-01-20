package cn.globalph.cms.web.processor;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.common.web.deeplink.DeepLink;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

import java.util.List;

/**
 * Extension handler for the {@link ContentProcessor}
 *
 * @author Elbert Bautista (elbertbautista)
 */
public interface ContentProcessorExtensionHandler extends ExtensionHandler {

    /**
     * This method will add any additional attributes to the model that the extension needs
     *
     * @param arguments - the Thymeleaf Processor arguments
     * @param element - the Thymeleaf Processor element
     * @return - ExtensionResultStatusType
     */
    public ExtensionResultStatusType addAdditionalFieldsToModel(Arguments arguments, Element element);

    /**
     * Provides a hook point for an extension of content processor to optionally add in deep links
     * for a content item based on its extension fields
     * @param links
     * @param arguments
     * @param element
     * @return ExtensionResultStatusType
     */
    public ExtensionResultStatusType addExtensionFieldDeepLink(List<DeepLink> links, Arguments arguments, Element element);
    
    /**
     * Provides a hook point to allow extension handlers to modify the generated deep links.
     * 
     * @param links
     * @return ExtensionResultStatusType
     */
    public ExtensionResultStatusType postProcessDeepLinks(List<DeepLink> links);

}

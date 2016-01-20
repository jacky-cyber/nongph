package cn.globalph.common.extensibility.context;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Provide the proper parser for the mergeImport element of the custom embedded namespace. See
 * {@code EmbeddedBeanDefinitionParser} for more information.
 *
 * @author Jeff Fischer
 */
public class EmbeddedNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("mergeImport", new EmbeddedBeanDefinitionParser());
    }

}

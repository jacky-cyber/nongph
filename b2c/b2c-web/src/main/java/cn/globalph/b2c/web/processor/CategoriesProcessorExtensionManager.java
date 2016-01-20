package cn.globalph.b2c.web.processor;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

/**
 * @author Jeff Fischer
 */
@Service("blCategoriesProcessorExtensionManager")
public class CategoriesProcessorExtensionManager extends ExtensionManager<CategoriesProcessorExtensionHandler> {

    public CategoriesProcessorExtensionManager() {
        super(CategoriesProcessorExtensionHandler.class);
    }

}

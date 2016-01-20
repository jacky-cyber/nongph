package cn.globalph.b2c.web.processor;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

/**
 * @author Jeff Fischer
 */
public interface CategoriesProcessorExtensionHandler extends ExtensionHandler {

    public ExtensionResultStatusType findAllPossibleChildCategories(String parentCategory, String maxResults, ExtensionResultHolder resultHolder);

}

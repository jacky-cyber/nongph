package cn.globalph.b2c.catalog.service;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

/**
 * @author Jeff Fischer
 */
public interface CatalogServiceExtensionHandler extends ExtensionHandler {

    public ExtensionResultStatusType findCategoryByURI(String uri, ExtensionResultHolder resultHolder);

    public ExtensionResultStatusType findProductByURI(String uri, ExtensionResultHolder resultHolder);

}

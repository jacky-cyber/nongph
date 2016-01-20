package cn.globalph.b2c.catalog.dao;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

/**
 * @author Jeff Fischer
 */
public interface CategoryDaoExtensionHandler extends ExtensionHandler {

    public ExtensionResultStatusType findCategoryByURI(String uri, ExtensionResultHolder resultHolder);

    Long getCurrentDateResolution();

    void setCurrentDateResolution(Long currentDateResolution);

}

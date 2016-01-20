package cn.globalph.b2c.product.dao;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

/**
 * @author Jeff Fischer
 */
public interface ProductDaoExtensionHandler extends ExtensionHandler {

    public ExtensionResultStatusType findProductByURI(String uri, ExtensionResultHolder resultHolder);

    Long getCurrentDateResolution();

    void setCurrentDateResolution(Long currentDateResolution);

}

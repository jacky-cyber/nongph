package cn.globalph.b2c.product.extension;

import cn.globalph.b2c.product.domain.ProductOption;
import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

/**
 * For internal usage. Allows extending API calls without subclassing the entity.
 *
 */
public interface ProductOptionEntityExtensionHandler extends ExtensionHandler {

    ExtensionResultStatusType getProductXrefs(ProductOption delegate, ExtensionResultHolder resultHolder);

}

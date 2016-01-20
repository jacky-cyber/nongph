package cn.globalph.b2c.product.extension;

import cn.globalph.b2c.product.domain.Product;
import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

/**
 * For internal usage. Allows extending API calls without subclassing the entity.
 *
 */
public interface ProductEntityExtensionHandler extends ExtensionHandler {

    ExtensionResultStatusType getAllParentCategoryXrefs(Product delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getAllParentCategories(Product delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getProductOptionXrefs(Product delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getProductOptions(Product delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getCrossSaleProducts(Product delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getUpSaleProducts(Product delegate, ExtensionResultHolder resultHolder);

}

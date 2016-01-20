package cn.globalph.b2c.catalog.extension;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

/**
 * For internal usage. Allows extending API calls without subclassing the entity.
 *
 * @author Jeff Fischer
 */
public interface CategoryEntityExtensionHandler extends ExtensionHandler {

    ExtensionResultStatusType getChildCategoryXrefs(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getAllChildCategoryXrefs(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getAllChildCategories(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType hasAllChildCategories(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getChildCategories(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType hasChildCategories(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getChildCategoryIds(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getAllParentCategoryXrefs(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getAllParentCategories(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getActiveProductXrefs(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getAllProductXrefs(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getActiveProducts(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getAllProducts(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getFeaturedProducts(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getCrossSaleProducts(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getUpSaleProducts(Category delegate, ExtensionResultHolder resultHolder);

    ExtensionResultStatusType getChildCategoryURLMap(Category delegate, ExtensionResultHolder resultHolder);
}

package cn.globalph.b2c.catalog.extension;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

/**
 * For internal usage. Allows extension of entity API calls without subclassing the entity.
 *
 * @author Jeff Fischer
 */
@Service("blCategoryEntityExtensionManager")
public class CategoryEntityExtensionManager extends ExtensionManager<CategoryEntityExtensionHandler> {

    public CategoryEntityExtensionManager() {
        super(CategoryEntityExtensionHandler.class);
    }

}

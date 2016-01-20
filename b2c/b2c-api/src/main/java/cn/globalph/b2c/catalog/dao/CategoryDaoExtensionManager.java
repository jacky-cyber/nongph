package cn.globalph.b2c.catalog.dao;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

/**
 * @author Jeff Fischer
 */
@Service("blCategoryDaoExtensionManager")
public class CategoryDaoExtensionManager extends ExtensionManager<CategoryDaoExtensionHandler> {

    public CategoryDaoExtensionManager() {
        super(CategoryDaoExtensionHandler.class);
    }

}

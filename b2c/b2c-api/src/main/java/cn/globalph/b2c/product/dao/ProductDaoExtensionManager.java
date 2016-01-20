package cn.globalph.b2c.product.dao;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

/**
 * @author Jeff Fischer
 */
@Service("blProductDaoExtensionManager")
public class ProductDaoExtensionManager extends ExtensionManager<ProductDaoExtensionHandler> {

    public ProductDaoExtensionManager() {
        super(ProductDaoExtensionHandler.class);
    }

}

package cn.globalph.b2c.product.extension;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

/**
 * For internal usage. Allows extending API calls without subclassing the entity.
 *
 */
@Service("blProductEntityExtensionManager")
public class ProductEntityExtensionManager extends ExtensionManager<ProductEntityExtensionHandler> {

    public ProductEntityExtensionManager() {
        super(ProductEntityExtensionHandler.class);
    }

}

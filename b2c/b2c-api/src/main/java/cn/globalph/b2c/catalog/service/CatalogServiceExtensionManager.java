package cn.globalph.b2c.catalog.service;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

/**
 * @author Jeff Fischer
 */
@Service("blCatalogServiceExtensionManager")
public class CatalogServiceExtensionManager extends ExtensionManager<CatalogServiceExtensionHandler> {

    public CatalogServiceExtensionManager() {
        super(CatalogServiceExtensionHandler.class);
    }

}

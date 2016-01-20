package cn.globalph.b2c.offer.extension;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;

/**
 * For internal usage. Allows extending API calls without subclassing the entity.
 *
 * @author Jeff Fischer
 */
@Service("blOfferEntityExtensionManager")
public class OfferEntityExtensionManager extends ExtensionManager<OfferEntityExtensionHandler> {

    public OfferEntityExtensionManager() {
        super(OfferEntityExtensionHandler.class);
    }

}

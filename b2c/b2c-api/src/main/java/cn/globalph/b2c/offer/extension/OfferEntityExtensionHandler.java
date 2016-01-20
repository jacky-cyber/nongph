package cn.globalph.b2c.offer.extension;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;

/**
 * For internal usage. Allows extending API calls without subclassing the entity.
 *
 * @author Jeff Fischer
 */
public interface OfferEntityExtensionHandler extends ExtensionHandler {

    ExtensionResultStatusType getOfferCodes(Offer delegate, ExtensionResultHolder resultHolder);

}

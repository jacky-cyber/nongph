package cn.globalph.b2c.offer.service;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * @author felix.wu
 */
@Service("blOfferServiceExtensionManager")
public class OfferServiceExtensionManager extends ExtensionManager<OfferServiceExtensionHandler> {

    public static final String STOP_PROCESSING = "stopProcessing";

    public OfferServiceExtensionManager() {
        super(OfferServiceExtensionHandler.class);
    }

}

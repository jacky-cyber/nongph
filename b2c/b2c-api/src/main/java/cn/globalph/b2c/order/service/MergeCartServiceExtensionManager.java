package cn.globalph.b2c.order.service;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * Extension manager for merge cart.
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Service("blMergeCartServiceExtensionManager")
public class MergeCartServiceExtensionManager extends ExtensionManager<MergeCartServiceExtensionHandler> {

    public MergeCartServiceExtensionManager() {
        super(MergeCartServiceExtensionHandler.class);
    }

    /**
     * By default,this extension manager will continue on handled allowing multiple handlers to interact with the order.
     */
    public boolean continueOnHandled() {
        return true;
    }

}

package cn.globalph.b2c.web.service;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */
@Service("blUpdateCartServiceExtensionManager")
public class UpdateCartServiceExtensionManager extends ExtensionManager<UpdateCartServiceExtensionHandler> {
    
    public UpdateCartServiceExtensionManager() {
        super(UpdateCartServiceExtensionHandler.class);
    }
}

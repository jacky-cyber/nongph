package cn.globalph.b2c.inventory.service;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Component;


@Component("blInventoryServiceExtensionManager")
public class InventoryServiceExtensionManager extends ExtensionManager<InventoryServiceExtensionHandler> {

    public InventoryServiceExtensionManager() {
        super(InventoryServiceExtensionHandler.class);
    }

}

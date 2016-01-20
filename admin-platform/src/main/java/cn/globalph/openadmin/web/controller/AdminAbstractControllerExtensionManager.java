package cn.globalph.openadmin.web.controller;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Component;

/**
 * Manager for the {@link AdminControllerExtensionHandler}
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("blAdminAbstractControllerExtensionManager")
public class AdminAbstractControllerExtensionManager extends ExtensionManager<AdminControllerExtensionHandler> {

    public AdminAbstractControllerExtensionManager() {
        super(AdminControllerExtensionHandler.class);
     }

}

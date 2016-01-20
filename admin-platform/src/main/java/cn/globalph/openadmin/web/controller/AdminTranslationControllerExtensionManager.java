package cn.globalph.openadmin.web.controller;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Component;

/**
 * @author Andre Azzolini (apazzolini)
 */
@Component("blAdminTranslationControllerExtensionManager")
public class AdminTranslationControllerExtensionManager extends ExtensionManager<AdminTranslationControllerExtensionHandler> {

    public AdminTranslationControllerExtensionManager() {
        super(AdminTranslationControllerExtensionHandler.class);
    }

}

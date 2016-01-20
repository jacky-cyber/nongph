package cn.globalph.openadmin.web.controller;

import cn.globalph.common.extension.ExtensionManager;
import cn.globalph.openadmin.web.form.entity.DefaultMainActions;

import org.springframework.stereotype.Component;


/**
 * Extension manager to modify the actions that are added by default when viewing a ceiling entity for a particular
 * section (for instance, a list of Products in the 'Product' section). Assuming that the user has proper permissions,
 * the mainActions list would have {@link DefaultMainActions#ADD}
 *
 * @author Phillip Verheyden (phillipuniverse)
 * @see 
 */
@Component("blMainEntityActionsExtensionManager")
public class MainEntityActionsExtensionManager extends ExtensionManager<MainEntityActionsExtensionHandler> {

    /**
     * @param _clazz
     */
    public MainEntityActionsExtensionManager() {
        super(MainEntityActionsExtensionHandler.class);
    }

    @Override
    public boolean continueOnHandled() {
        return true;
    }

}

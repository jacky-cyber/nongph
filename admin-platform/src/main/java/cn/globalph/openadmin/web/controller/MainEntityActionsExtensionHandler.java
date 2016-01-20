package cn.globalph.openadmin.web.controller;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.web.form.entity.DefaultMainActions;
import cn.globalph.openadmin.web.form.entity.EntityFormAction;

import java.util.List;


/**
 * 
 *
 * @author Phillip Verheyden (phillipuniverse)
 */
public interface MainEntityActionsExtensionHandler extends ExtensionHandler {

    /**
     * Extension point to override the actions that are added by default when viewing a ceiling entity for a particular
     * section (for instance, a list of Products in the 'Product' section). Assuming that the user has proper permissions,
     * the mainActions list would have {@link DefaultMainActions#ADD}
     * 
     * @param cmd the metadata for the ceiling entity that is being displayed
     * @param mainActions the actions that are added to the main form by default. Use this list to add more actions
     */
    public void modifyMainActions(ClassMetadata cmd, List<EntityFormAction> mainActions);
    
}

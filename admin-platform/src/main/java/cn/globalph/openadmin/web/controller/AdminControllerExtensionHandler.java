package cn.globalph.openadmin.web.controller;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.openadmin.server.security.domain.AdminSection;
import cn.globalph.openadmin.web.controller.entity.EntityAdminController;
import cn.globalph.openadmin.web.form.entity.EntityFormAction;

import org.springframework.ui.Model;

import java.util.List;


/**
 * Extension handler for methods present in {@link BasicAdminController}.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public interface AdminControllerExtensionHandler extends ExtensionHandler {
    
    public static final String NEW_CLASS_NAME = "newClassName";

    /**
     * Invoked every time {@link EntityAdminController#viewEntityList()} is invoked to allow additional
     * main form actions to be contributed.
     * 
     * @param model
     * @param sectionKey
     * @return
     */
    public ExtensionResultStatusType addAdditionalMainActions(String sectionClassName, List<EntityFormAction> actions);

    /**
     * Invoked every time {@link BasicAdminController#setModelAttributes(Model, String)} is invoked.
     * 
     * @param model
     * @param sectionKey
     * @return the extension result status
     */
    public ExtensionResultStatusType setAdditionalModelAttributes(Model model, String sectionKey);

    /**
     * Invoked whenever {@link BasicAdminController#getClassNameForSection(String)} is invoked. If an extension
     * handler sets the {@link #NEW_CLASS_NAME} variable in the ExtensionResultHolder, the overriden value will be used.
     * 
     * @param erh
     * @param sectionKey
     * @param section
     * @return
     */
    public ExtensionResultStatusType overrideClassNameForSection(ExtensionResultHolder<String> erh, String sectionKey, 
            AdminSection section);
}

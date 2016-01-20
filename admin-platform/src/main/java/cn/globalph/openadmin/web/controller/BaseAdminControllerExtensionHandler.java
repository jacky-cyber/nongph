package cn.globalph.openadmin.web.controller;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.openadmin.server.security.domain.AdminSection;
import cn.globalph.openadmin.web.form.entity.EntityFormAction;

import org.springframework.ui.Model;

import java.util.List;


/**
 * Abstract implementatino of {@link AdminControllerExtensionHandler}.
 * 
 * Individual implementations of this extension handler should subclass this class as it will allow them to 
 * only override the methods that they need for their particular scenarios.
 * 
  */
public class BaseAdminControllerExtensionHandler extends AbstractExtensionHandler implements AdminControllerExtensionHandler {

    @Override
    public ExtensionResultStatusType addAdditionalMainActions(String sectionClassName, List<EntityFormAction> actions) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType setAdditionalModelAttributes(Model model, String sectionKey) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    
    @Override
    public ExtensionResultStatusType overrideClassNameForSection(ExtensionResultHolder<String> erh, String sectionKey, 
            AdminSection section) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}

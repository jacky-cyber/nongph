package cn.globalph.openadmin.web.service;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Component;

@Component("blFormBuilderExtensionManager")
public class FormBuilderExtensionManager extends ExtensionManager<FormBuilderExtensionHandler> {

    public FormBuilderExtensionManager() {
        super(FormBuilderExtensionHandler.class);
    }

    @Override
    public boolean continueOnHandled() {
        return true;
    }

}

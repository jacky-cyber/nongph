package cn.globalph.openadmin.web.controller;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.openadmin.web.form.TranslationForm;

/**
 * 
 * @author Andre Azzolini (apazzolini)
 */
public abstract class AbstractAdminTranslationControllerExtensionHandler extends AbstractExtensionHandler
        implements AdminTranslationControllerExtensionHandler {

    @Override
    public ExtensionResultStatusType applyTransformation(TranslationForm form) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}

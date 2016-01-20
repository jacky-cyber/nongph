package cn.globalph.openadmin.web.controller;

import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.openadmin.web.form.TranslationForm;

/**
 * @author felix.wu
 */
public interface AdminTranslationControllerExtensionHandler extends ExtensionHandler {

    /**
     * Applies any necessary transformations to the given form. For example, some entity fields might need to be
     * mapped in a different way.
     *
     * @param form
     */
    public ExtensionResultStatusType applyTransformation(TranslationForm form);

}

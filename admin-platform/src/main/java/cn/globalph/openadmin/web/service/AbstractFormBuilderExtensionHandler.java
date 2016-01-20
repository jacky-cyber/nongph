package cn.globalph.openadmin.web.service;

import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.web.form.component.ListGridRecord;
import cn.globalph.openadmin.web.form.entity.EntityForm;

/**
 * Abstract class to provide convenience for determining how to handle form 
 * extensions in the admin
 * 
 * @author Kelly Tisdell
 * @author Phillip Verheyden (phillipuniverse)
 */
public abstract class AbstractFormBuilderExtensionHandler extends AbstractExtensionHandler implements FormBuilderExtensionHandler {

    @Override
    public ExtensionResultStatusType modifyUnpopulatedEntityForm(EntityForm ef) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType modifyPopulatedEntityForm(EntityForm ef, Entity entity) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType modifyDetailEntityForm(EntityForm ef) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    
    @Override
    public ExtensionResultStatusType modifyListGridRecord(String className, ListGridRecord record, Entity entity) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    
    @Override
    public ExtensionResultStatusType addAdditionalFormActions(EntityForm entityForm) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }
    
}

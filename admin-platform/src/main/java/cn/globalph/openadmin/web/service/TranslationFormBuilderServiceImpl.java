package cn.globalph.openadmin.web.service;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.common.i18n.domain.Translation;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.openadmin.web.form.TranslationForm;
import cn.globalph.openadmin.web.form.component.DefaultListGridActions;
import cn.globalph.openadmin.web.form.component.ListGrid;
import cn.globalph.openadmin.web.form.component.ListGridAction;
import cn.globalph.openadmin.web.form.component.ListGridRecord;
import cn.globalph.openadmin.web.form.entity.ComboField;
import cn.globalph.openadmin.web.form.entity.DefaultEntityFormActions;
import cn.globalph.openadmin.web.form.entity.EntityForm;
import cn.globalph.openadmin.web.form.entity.EntityFormAction;
import cn.globalph.openadmin.web.form.entity.Field;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

@Service("blTranslationFormBuilderService")
public class TranslationFormBuilderServiceImpl implements TranslationFormBuilderService {
    
    @Resource(name = "blFormBuilderService")
    protected DynamicFormBuilder formBuilderService;
    
    @Override
    public ListGrid buildListGrid(List<Translation> translations, boolean isRte) {
        // Set up the two header fields we're interested in for the translations list grid
        List<Field> headerFields = new ArrayList<Field>();
        headerFields.add(new Field()
            .withName("localeCode")
            .withFriendlyName("Translation_localeCode")
            .withOrder(0));
        
        headerFields.add(new Field()
            .withName("translatedValue")
            .withFriendlyName("Translation_translatedValue")
            .withOrder(10));
        
        // Create the list grid and set its basic properties
        ListGrid listGrid = new ListGrid();
        listGrid.getHeaderFields().addAll(headerFields);
        listGrid.setListGridType(ListGrid.Type.TRANSLATION);
        listGrid.setCanFilterAndSort(false);
        
        // Allow add/update/remove actions, but provisioned especially for translation. Because of this, we will clone
        // the default actions so that we may change the class
        ListGridAction addAction = DefaultListGridActions.ADD.clone();
        ListGridAction removeAction = DefaultListGridActions.REMOVE.clone();
        ListGridAction updateAction = DefaultListGridActions.UPDATE.clone();
        addAction.setButtonClass("translation-grid-add");
        removeAction.setButtonClass("translation-grid-remove");
        updateAction.setButtonClass("translation-grid-update");
        listGrid.addToolbarAction(addAction);
        listGrid.addRowAction(removeAction);
        listGrid.addRowAction(updateAction);
        
        //TODO rework code elsewhere so these don't have to be added
        listGrid.setSubCollectionFieldName("translation");
        listGrid.setSectionKey("translation");
        
        // Create records for each of the entries in the translations list
        for (Translation t : translations) {
            ListGridRecord record = new ListGridRecord();
            record.setListGrid(listGrid);
            record.setId(String.valueOf(t.getId()));
            
            record.getFields().add(new Field()
                .withName("translatedValue")
                .withFriendlyName("Translation_translatedValue")
                .withOrder(10)
                .withValue(t.getTranslatedValue())
                .withDisplayValue(isRte ? getLocalizedEditToViewMessage() : t.getTranslatedValue()));
            
            listGrid.getRecords().add(record);
        }
        
        return listGrid;
    }
    
    @Override
    public EntityForm buildTranslationForm(TranslationForm formProperties) {
        EntityForm ef = new EntityForm();
        
        EntityFormAction saveAction = DefaultEntityFormActions.SAVE.clone();
        saveAction.setButtonClass("translation-submit-button");
        ef.addAction(saveAction);
        
        ef.addField(getLocaleField(formProperties.getLocaleCode()));
        
        ef.addField(new Field()
            .withName("translatedValue")
            .withFieldType(formProperties.getIsRte() ? "html" : "string")
            .withFriendlyName("Translation_translatedValue")
            .withValue(formProperties.getTranslatedValue())
            .withOrder(10));
        
        ef.addHiddenField(new Field()
            .withName("ceilingEntity")
            .withValue(formProperties.getCeilingEntity()));
        
        ef.addHiddenField(new Field()
            .withName("entityId")
            .withValue(formProperties.getEntityId()));
        
        ef.addHiddenField(new Field()
            .withName("propertyName")
            .withValue(formProperties.getPropertyName()));
        
        ef.addHiddenField(new Field()
            .withName("isRte")
            .withValue(String.valueOf(formProperties.getIsRte())));
        
        return ef;
    }
    
    protected ComboField getLocaleField(String value) {
        ComboField f = new ComboField();
        f.setName("localeCode");
        f.setFriendlyName("Translation_localeCode");
        f.setFieldType(SupportedFieldType.EXPLICIT_ENUMERATION.toString());
        f.setOrder(0);
        
        if (StringUtils.isNotBlank(value)) {
            f.setValue(value);
           }
        
        return f;
    }
    
    protected String getLocalizedEditToViewMessage() {
        WebRequestContext ctx = WebRequestContext.getWebRequestContext();
        if (ctx != null && ctx.getMessageSource() != null) {
            return ctx.getMessageSource().getMessage("i18n.editToView", null, java.util.Locale.CHINA);
        }
        return null;
    }

}

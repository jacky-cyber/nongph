package cn.globalph.admin.web.controller;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.openadmin.web.controller.AbstractAdminTranslationControllerExtensionHandler;
import cn.globalph.openadmin.web.controller.AdminTranslationControllerExtensionManager;
import cn.globalph.openadmin.web.form.TranslationForm;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author Andre Azzolini (apazzolini)
 */
@Component("blAdminProductTranslationExtensionHandler")
public class AdminProductTranslationExtensionHandler extends AbstractAdminTranslationControllerExtensionHandler {
    
    @Resource(name = "blCatalogService")
    protected CatalogService catalogService;

    @Resource(name = "blAdminTranslationControllerExtensionManager")
    protected AdminTranslationControllerExtensionManager extensionManager;

    @PostConstruct
    public void init() {
        if (isEnabled()) {
            extensionManager.registerHandler(this);
        }
    }
    
    /**
     * If we are trying to translate a field on Product that starts with "defaultSku.", we really want to associate the
     * translation with Sku, its associated id, and the property name without "defaultSku."
     */
    @Override
    public ExtensionResultStatusType applyTransformation(TranslationForm form) {
        if (form.getCeilingEntity().equals(Product.class.getName()) && form.getPropertyName().startsWith("defaultSku.")) {
            Product p = catalogService.findProductById(Long.parseLong(form.getEntityId()));
                /*
            form.setCeilingEntity(Sku.class.getName());
            form.setEntityId(String.valueOf(p.getDefaultSku().getId()));
            form.setPropertyName(form.getPropertyName().substring("defaultSku.".length()));
                */
        }
        
        return ExtensionResultStatusType.HANDLED;
    }
    
}

package cn.globalph.b2c.web.controller.catalog;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.web.catalog.ProductHandlerMapping;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.template.TemplateOverrideExtensionManager;
import cn.globalph.common.template.TemplateType;
import cn.globalph.common.web.TemplateTypeAware;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.common.web.controller.BasicController;
import cn.globalph.common.web.deeplink.DeepLinkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class works in combination with the CategoryHandlerMapping which finds a category based upon
 * the passed in URL.
 *
 */
public class BasicProductController extends BasicController implements Controller, TemplateTypeAware {
    
    protected String defaultProductView = "catalog/product";
    protected static String MODEL_ATTRIBUTE_NAME = "sku";
    
    @Autowired(required = false)
    @Qualifier("blProductDeepLinkService")
    protected DeepLinkService<Product> deepLinkService;
    
    @Resource(name = "blTemplateOverrideExtensionManager")
    protected TemplateOverrideExtensionManager templateOverrideManager;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView model = new ModelAndView();
        Sku currentSku = (Sku) request.getAttribute(ProductHandlerMapping.CURRENT_SKU_ATTRIBUTE_NAME);
        assert(currentSku != null);
        Product product =  currentSku.getProduct();
        model.addObject(MODEL_ATTRIBUTE_NAME, currentSku);

        addDeepLink(model, deepLinkService, product);
        
        ExtensionResultHolder<String> erh = new ExtensionResultHolder<String>();
        templateOverrideManager.getHandlerProxy().getOverrideTemplate(erh, product);
        
        if (StringUtils.isNotBlank(erh.getResult())) {
            model.setViewName(erh.getResult());
        } else if (StringUtils.isNotEmpty(product.getDisplayTemplate())) {
            model.setViewName(product.getDisplayTemplate());    
        } else {
            model.setViewName(getDefaultProductView());
        }
        return model;
    }

    public String getDefaultProductView() {
        return defaultProductView;
    }

    public void setDefaultProductView(String defaultProductView) {
        this.defaultProductView = defaultProductView;
    }
    
    @Override
    public String getExpectedTemplateName(HttpServletRequest request) {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null) {
            Sku sku = (Sku) context.getRequest().getAttribute(ProductHandlerMapping.CURRENT_SKU_ATTRIBUTE_NAME);
            if (sku != null && sku.getProduct().getDisplayTemplate() != null) {
                return sku.getProduct().getDisplayTemplate();
            }
        }
        return getDefaultProductView();
    }

    @Override
    public TemplateType getTemplateType(HttpServletRequest request) {
        return TemplateType.PRODUCT;
    }

}

package cn.globalph.b2c.web.catalog;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.common.web.BLCAbstractHandlerMapping;
import cn.globalph.common.web.WebRequestContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * This handler mapping works with the Category entity to determine if a category has been configured for
 * the passed in URL.   
 * 
 * If the URL matches a valid Category then this mapping returns the handler configured via the 
 * controllerName property or blCategoryController by default. 
 *
 * @see cn.globalph.b2c.catalog.domain.Category
 * @see CataService
 */
public class CategoryHandlerMapping extends BLCAbstractHandlerMapping {
    
    protected String defaultTemplateName = "catalog/category";

    @Resource(name = "blCatalogService")
    private CatalogService catalogService;
    
    public static final String CURRENT_CATEGORY_ATTRIBUTE_NAME = "category";

    @Override
    protected Object getHandlerInternal(HttpServletRequest request)
            throws Exception {      
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if (context != null && context.getRequestURIWithoutContext() != null) {
        	String url = context.getRequestURIWithoutContext();
        	boolean isAjax = false;
        	if(url.endsWith("ajaxCategory")){
        		int length = url.length();
        		url = url.substring(0,length-13);
        		isAjax = true;
        	}
            Category category = catalogService.findCategoryByURI(url);

            if (category != null) {
                context.getRequest().setAttribute(CURRENT_CATEGORY_ATTRIBUTE_NAME, category);
                if(isAjax){
                	return ajaxControllerName;
                }else{
                	return controllerName;
                }
            	}
           }
        return null;
    }

    public String getDefaultTemplateName() {
        return defaultTemplateName;
    }

    public void setDefaultTemplateName(String defaultTemplateName) {
        this.defaultTemplateName = defaultTemplateName;
    }
}

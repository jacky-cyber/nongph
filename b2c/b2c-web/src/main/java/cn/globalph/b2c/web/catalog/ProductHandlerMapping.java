package cn.globalph.b2c.web.catalog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.product.domain.Sku;
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
 * @see cn.globalph.b2c.product.domain.Product
 * @see CatalogService
 */
public class ProductHandlerMapping extends BLCAbstractHandlerMapping {
    
	public static final String CURRENT_SKU_ATTRIBUTE_NAME = "currentSKU";
	
    private String controllerName="blProductController";
    
    private Pattern skuDetailPattern = Pattern.compile( "(.+)/(\\d+)" );
    
    @Resource(name = "blCatalogService")
    private CatalogService catalogService;

    private String defaultTemplateName = "catalog/product";
    
    public String getDefaultTemplateName() {
        return defaultTemplateName;
    }

    public void setDefaultTemplateName(String defaultTemplateName) {
        this.defaultTemplateName = defaultTemplateName;
    }
    
    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        WebRequestContext context = WebRequestContext.getWebRequestContext();
        if( context != null && context.getRequestURIWithoutContext() != null ) {
        	String uri = context.getRequestURIWithoutContext();
        	Matcher matcher = skuDetailPattern.matcher( uri );
        	if( matcher.matches() ) {
        		Sku sku = catalogService.findSkuByURI( matcher.group(1), Long.valueOf(matcher.group(2)) );
                if (sku != null) {
                    context.getRequest().setAttribute(CURRENT_SKU_ATTRIBUTE_NAME, sku);
                    return controllerName;
                }
        	}
        }
        return null;
    }

   public static void main(String[] args){
	   ProductHandlerMapping m = new ProductHandlerMapping();
	   Matcher mc = m.skuDetailPattern.matcher( "aaa/123" );
	   if( mc.matches() ){
		   System.out.println( mc.group(1) );
		   System.out.println( mc.group(2) );
	   }
   }

}

package cn.globalph.b2c.web.catalog.taglib;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.catalog.domain.Category;

import javax.servlet.jsp.JspException;

public class CategoryLookupTag extends AbstractCatalogTag {

    private static final Log LOG = LogFactory.getLog(CategoryTag.class);
    private String var;

    private String categoryName;

    @Override
    public void doTag() throws JspException {
        catalogService = super.getCatalogService();
       
        List<Category> categoryList = catalogService.findCategoriesByName(categoryName);
        
        if( categoryList.isEmpty() ){
        	if( LOG.isDebugEnabled() )
        		LOG.debug("The category returned was null for categoryName: " + categoryName);
        	getJspContext().setAttribute(var, null);
        } else {
        	getJspContext().setAttribute(var, categoryList.get(0));
        }
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}


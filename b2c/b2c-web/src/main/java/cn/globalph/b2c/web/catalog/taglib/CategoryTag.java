package cn.globalph.b2c.web.catalog.taglib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.catalog.domain.Category;

import javax.servlet.jsp.JspException;

public class CategoryTag extends AbstractCatalogTag {

    private static final Log LOG = LogFactory.getLog(CategoryTag.class);
    private static final long serialVersionUID = 1L;
    private String var;

    private long categoryId;

    @Override
    public void doTag() throws JspException {
        catalogService = super.getCatalogService();

        Category category = catalogService.findCategoryById(categoryId);

        if(category == null && LOG.isDebugEnabled()){
            LOG.debug("The category returned was null for categoryId: " + categoryId);
        }

        getJspContext().setAttribute(var, category);
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

}

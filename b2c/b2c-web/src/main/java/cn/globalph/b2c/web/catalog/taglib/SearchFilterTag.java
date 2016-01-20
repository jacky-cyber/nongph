package cn.globalph.b2c.web.catalog.taglib;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.product.domain.Product;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.util.List;

/**
 * The SearchFilterTag sets up an environment for it's children {@link SearchFilterItemTag}s. If a queryString
 * is passed in, the tag will render a textbox named queryString populated with the string passed in. It will also
 * render a hidden input containing the same value to aid in deciding whether to do ajax loads while filtering
 * search results or to do a full page refresh for new searches.
 *
 */
public class SearchFilterTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    private List<Product> products;
    private List<Category> categories;
    private String queryString;
    
    @Override
    public int doStartTag() throws JspException {
        JspWriter out = this.pageContext.getOut();
//        if (products == null || products.size() == 0) { return SKIP_BODY; }
        if(products != null && products.size() > 0 ){           
            if (queryString != null && !"".equals(queryString)) {
                try {
                    out.println("<h3>Your Search</h3>");
                    out.println("<input type=\"text\"  class=\"searchQuery\" name=\"queryString\" id=\"queryString\" value='"+queryString+"' />");
                    out.println("<input type=\"hidden\"  name=\"originalQueryString\" id=\"originalQueryString\" value='"+queryString+"' />");
                } catch (IOException e) {
                }
            }
            return EVAL_BODY_INCLUDE;
        }
        if(categories != null && categories.size() > 0){
            if (queryString != null && !"".equals(queryString)) {
                try {
                    out.println("<h3>Your Search</h3>");
                    out.println("<input type=\"text\"  class=\"searchQuery\" name=\"queryString\" id=\"queryString\" value='"+queryString+"' />");
                    out.println("<input type=\"hidden\"  name=\"originalQueryString\" id=\"originalQueryString\" value='"+queryString+"' />");
                } catch (IOException e) {
                }
            }
            return EVAL_BODY_INCLUDE;           
        }
        
        return SKIP_BODY;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}

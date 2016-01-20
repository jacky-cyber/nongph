package cn.globalph.b2c.web.catalog.taglib;

import cn.globalph.b2c.catalog.domain.Category;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import java.io.IOException;

public class CategoryLinkTag extends AbstractCatalogTag {

    private Category category;

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        if (category != null) out.println(getUrl(category));

        super.doTag();
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    protected String getUrl(Category category) {
        PageContext pageContext = (PageContext)getJspContext();
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        StringBuffer sb = new StringBuffer();
        sb.append("<a href=\"");
        sb.append(request.getContextPath());
        sb.append("/");
        sb.append(category.getGeneratedUrl());
        sb.append("\">");
        sb.append(category.getName());
        sb.append("</a>");

        return sb.toString();
    }

}

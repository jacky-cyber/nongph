package cn.globalph.b2c.web.catalog.taglib;

import cn.globalph.b2c.product.domain.Product;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import java.io.IOException;

public class ProductLinkTag extends CategoryLinkTag {

    private Product product;

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        out.println(getUrl(product));
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    protected String getUrl(Product product) {
        PageContext pageContext = (PageContext)getJspContext();
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        StringBuffer sb = new StringBuffer();
        sb.append("<a class=\"noTextUnderline\"  href=\"");
        sb.append(request.getContextPath());
        sb.append("/");
        sb.append(getCategory().getGeneratedUrl());
        sb.append("?productId=" + product.getId());
        sb.append("\">");
        sb.append(product.getName());
        sb.append("</a>");

        return sb.toString();
    }

}

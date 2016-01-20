package cn.globalph.b2c.web.catalog.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class ShareTag extends SimpleTagSupport {

    private static final long serialVersionUID = 1L;

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        out.println(share());
        super.doTag();
    }

    protected String share() {
        StringBuffer sb = new StringBuffer();
        sb.append("<a id=\"fbLink\" href=\"\">");
        sb.append("<img src=\"/broadleafdemo/images/share/link-facebook.gif\" />");
        sb.append("</a>");

        sb.append("<a id=\"diggLink\" href=\"\">");
        sb.append("<img src=\"/broadleafdemo/images/share/link-digg.gif\" />");
        sb.append("</a>");

        sb.append("<a id=\"deliciousLink\" href=\"\">");
        sb.append("<img src=\"/broadleafdemo/images/share/link-delicious.gif\" />");
        sb.append("</a>");

        return sb.toString();
    }

}

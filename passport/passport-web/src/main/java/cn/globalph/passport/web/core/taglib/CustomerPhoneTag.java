package cn.globalph.passport.web.core.taglib;

import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class CustomerPhoneTag extends BodyTagSupport {
    private static final long serialVersionUID = 1L;
    private Long customerPhoneId;
    private String var;

    public int doStartTag() throws JspException {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
        CustomerState customerState = (CustomerState) applicationContext.getBean("blCustomerState");

        Customer customer = customerState.getCustomer((HttpServletRequest) pageContext.getRequest());

        return EVAL_PAGE;
    }

    public Long getCustomerPhoneId() {
        return customerPhoneId;
    }

    public String getVar() {
        return var;
    }

    public void setCustomerPhoneId(Long customerPhoneId) {
        this.customerPhoneId = customerPhoneId;
    }

    public void setVar(String var) {
        this.var = var;
    }
}

package cn.globalph.b2c.web.catalog.taglib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.common.util.BLCSystemProperty;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class GoogleAnalyticsTag extends SimpleTagSupport {
    
    private static final Log LOG = LogFactory.getLog(GoogleAnalyticsTag.class);

    protected String webPropertyId;
    
    protected String getWebPropertyIdDefault() {
        return BLCSystemProperty.resolveSystemProperty("googleAnalytics.webPropertyId");
    }

    private Order order;

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getWebPropertyId() {
        if (this.webPropertyId == null) {
            return getWebPropertyIdDefault();
        } else {
            return this.webPropertyId;
        }
    }

    public void setWebPropertyId(String webPropertyId) {
        this.webPropertyId = webPropertyId;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        String webPropertyId = getWebPropertyId();
        
        if (webPropertyId == null) {
            ServletContext sc = ((PageContext) getJspContext()).getServletContext();
            ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(sc);
            context.getAutowireCapableBeanFactory().autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
        }
        
        if (webPropertyId.equals("UA-XXXXXXX-X")) {
            LOG.warn("googleAnalytics.webPropertyId has not been overridden in a custom property file. Please set this in order to properly use the Google Analytics tag");
        }
        
        out.println(analytics(webPropertyId, order));
        super.doTag();
    }

    /**
     * Documentation for the recommended asynchronous GA tag is at:
     * http://code.google.com/apis/analytics/docs/tracking/gaTrackingEcommerce.html
     * 
     * @param webPropertyId - Google Analytics ID
     * @param order - optionally track the order submission. This should be included on the
     * page after the order has been sucessfully submitted. If null, this will just track the current page
     * @return the relevant Javascript to render on the page
     */
    protected String analytics(String webPropertyId, Order order) {
        StringBuffer sb = new StringBuffer();
        
        sb.append("<script type=\"text/javascript\">");
        sb.append("var _gaq = _gaq || [];");
        sb.append("_gaq.push(['_setAccount', '" + webPropertyId + "']);");
        sb.append("_gaq.push(['_trackPageview']);");
        
        if (order != null) {
            sb.append("_gaq.push(['_addTrans','" + order.getId() + "'");
            sb.append(",'" + order.getName() + "'");
            sb.append(",'" + order.getTotal() + "'");
            sb.append(",'" + order.getTotalFulfillmentCharges() + "'");
            sb.append("]);");

            for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
                for (FulfillmentGroupItem fulfillmentGroupItem : fulfillmentGroup.getFulfillmentGroupItems()) {
                    OrderItem orderItem = fulfillmentGroupItem.getOrderItem();
                    sb.append("_gaq.push(['_addItem','" + order.getId() + "'");
                    sb.append(",'" + orderItem.getSku().getId() + "'");
                    sb.append(",'" + orderItem.getSku().getName() + "'");
                    sb.append(",'" + orderItem.getPrice() + "'");
                    sb.append(",'" + orderItem.getQuantity() + "'");
                    sb.append("]);");
                	 }
                }
            sb.append("_gaq.push(['_trackTrans']);");
           }
        
        sb.append(" (function() {"
                + "var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;"
                + "ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';"
                + "var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);"
                + "})();");
        sb.append("</script>");

        return sb.toString();
    }
    
}

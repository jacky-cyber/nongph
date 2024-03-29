package cn.globalph.b2c.web.processor;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.common.util.BLCSystemProperty;
import cn.globalph.common.web.dialect.AbstractModelVariableModifierProcessor;

import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;

import javax.annotation.Resource;

/**
 * A Thymeleaf processor that will on order confirmation page, submit order
 * information via javascript to google analytics.
 * 
 * Example usage on order confirmation page:
 * <pre>
 *  {@code
 *      <blc:googleAnalytics th:attr="orderNumber=${order != null ? order.orderNumber : null}" />
 *      <script th:utext="${analytics}" />
 *  }
 * </pre>
 * @author tleffert
 */
public class GoogleAnalyticsProcessor extends AbstractModelVariableModifierProcessor {

    @Resource(name = "blOrderService")
    protected OrderService orderService;

    protected String affiliation;

    protected String getWebPropertyId() {
        return BLCSystemProperty.resolveSystemProperty("googleAnalytics.webPropertyId");
    }

    protected String getAffiliationDefault() {
        return BLCSystemProperty.resolveSystemProperty("googleAnalytics.affiliation");
    }
    
    @Value("${googleAnalytics.testLocal}")
    protected boolean testLocal = false;

    /**
     * Sets the name of this processor to be used in Thymeleaf template
     */
    public GoogleAnalyticsProcessor() {
        super("googleanalytics");
    }

    @Override
    public int getPrecedence() {
        return 100000;
    }

    @Override
    protected void modifyModelAttributes(Arguments arguments, Element element) {

        String orderNumber = element.getAttributeValue("orderNumber");
        Order order = null;
        if (orderNumber != null) {
            order = orderService.findOrderByOrderNumber(orderNumber);
        }
        addToModel(arguments, "analytics", analytics(getWebPropertyId(), order));
    }

    /**
     * Documentation for the recommended asynchronous GA tag is at:
     * http://code.google.com/apis/analytics/docs/tracking/gaTrackingEcommerce.html
     * 
     * @param webPropertyId
     *            - Google Analytics ID
     * @param order
     *            - optionally track the order submission. This should be
     *            included on the page after the order has been sucessfully
     *            submitted. If null, this will just track the current page
     * @return the relevant Javascript to render on the page
     */
    protected String analytics(String webPropertyId, Order order) {
        StringBuffer sb = new StringBuffer();

        sb.append("var _gaq = _gaq || [];\n");
        sb.append("_gaq.push(['_setAccount', '" + webPropertyId + "']);");

        sb.append("_gaq.push(['_trackPageview']);");
        
        if (testLocal) {
            sb.append("_gaq.push(['_setDomainName', '127.0.0.1']);");
        }
        
        if (order != null) {
            for (FulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
                for (FulfillmentGroupItem fulfillmentGroupItem : fulfillmentGroup.getFulfillmentGroupItems()) {
                    OrderItem orderItem = fulfillmentGroupItem.getOrderItem();

                    Sku sku = orderItem.getSku();
                    
                    sb.append("_gaq.push(['_addItem','" + order.getOrderNumber() + "'");
                    sb.append(",'" + sku.getId() + "'");
                    sb.append(",'" + sku.getName() + "'");
                    sb.append(",'" + getVariation(orderItem) + "'");
                    sb.append(",'" + orderItem.getAveragePrice() + "'");
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

        return sb.toString();
    }
    
    /**
     * Returns the product option values separated by a space if they are
     * relevant for the item, or the product category if no options are available
     * 
     * @return
     */
    protected String getVariation(OrderItem item) {
    	return  "";
    }

    protected void setTestLocal(boolean testLocal) {
        this.testLocal = testLocal;
    }
    
    public boolean getTestLocal() {
        return testLocal;
    }
    
    public String getAffiliation() {
        if (affiliation == null) {
            return getAffiliationDefault();
        } else {
            return affiliation;
        }
    }
    
    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

}

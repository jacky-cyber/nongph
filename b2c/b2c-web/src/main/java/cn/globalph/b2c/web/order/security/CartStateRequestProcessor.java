package cn.globalph.b2c.web.order.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.MergeCartService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.order.service.call.MergeCartResponse;
import cn.globalph.b2c.order.service.exception.RemoveFromCartException;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.pricing.service.exception.PricingException;
import cn.globalph.b2c.web.service.UpdateCartService;
import cn.globalph.common.crossapp.service.CrossAppAuthService;
import cn.globalph.common.web.AbstractRequestProcessor;
import cn.globalph.common.web.RequestProcessor;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;
import cn.globalph.passport.web.core.security.CustomerStateRequestProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Ensures that the customer's current cart is available to the request.  
 * 
 * Also invokes blMergeCartProcessor" if the user has just logged in.   
 * 
 * Genericized version of the CartStateFilter. This was made to facilitate reuse between Servlet Filters, Portlet Filters 
 * and Spring MVC interceptors. Spring has an easy way of converting HttpRequests and PortletRequests into WebRequests 
 * via <br />
 * new ServletWebRequest(httpServletRequest); new PortletWebRequest(portletRequest); <br />
 * For the interceptor pattern, you can simply implement a WebRequestInterceptor to invoke from there.
 * 
 * @see {@link CartStateFilter}
 * @see {@link RequestProcessor}
 * @see {@link ServletWebRequest}
 * @see {@link org.springframework.web.portlet.context.PortletWebRequest}
 */
@Component("blCartStateRequestProcessor")
public class CartStateRequestProcessor extends AbstractRequestProcessor {

    /** Logger for this class and subclasses */
    protected final Log LOG = LogFactory.getLog(getClass());

    public static final String BLC_RULE_MAP_PARAM = "blRuleMap";

    private final String mergeCartResponseKey = "bl_merge_cart_response";

    @Resource(name = "blOrderService")
    protected OrderService orderService;

    @Resource(name = "blUpdateCartService")
    protected UpdateCartService updateCartService;

    @Resource(name = "blMergeCartService")
    protected MergeCartService mergeCartService;
    
    @Resource(name = "blCustomerStateRequestProcessor")
    protected CustomerStateRequestProcessor customerStateRequestProcessor;

    @Autowired(required = false)
    @Qualifier("blCrossAppAuthService")
    protected CrossAppAuthService crossAppAuthService;

    protected static String cartRequestAttributeName = "cart";
    
    protected static String anonymousCartSessionAttributeName = "anonymousCart";

    public static final String OVERRIDE_CART_ATTR_NAME = "_blc_overrideCartId";
        
    @Override
    public void process(WebRequest request) {
        Customer customer = CustomerState.getCustomer();

        if (customer == null) {
            LOG.warn("No customer was found on the current request, no cart will be added to the current request. Ensure that the"
                    + " blCustomerStateFilter occurs prior to the blCartStateFilter");
            return;
        }
        Order cart = null;
        
        String orderNumber = (String)request.getAttribute("orderNumber", WebRequest.SCOPE_SESSION);
        if(orderNumber != null && !orderNumber.equals("")){
        	cart = orderService.findOrderByOrderNumber(orderNumber);
        }else{
	        cart = getOverrideCart(request);
	
	        if (cart == null && mergeCartNeeded(customer, request)) {
	            if (LOG.isDebugEnabled()) {
	                LOG.debug("Merge cart required, calling mergeCart " + customer.getId());
	            }
	            cart = mergeCart(customer, request);
	        } else if (cart == null) {
	            cart = orderService.findCartForCustomer(customer);
	        }
	
	        if (cart == null) {
	            cart = orderService.getNullOrder();
	        } else {
	            updateCartService.updateAndValidateCart(cart);
	        }
        }
        request.setAttribute(cartRequestAttributeName, cart, WebRequest.SCOPE_REQUEST);

        // Setup cart for content rule processing
        Map<String, Object> ruleMap = (Map<String, Object>) request.getAttribute(BLC_RULE_MAP_PARAM, WebRequest.SCOPE_REQUEST);
        if (ruleMap == null) {
            ruleMap = new HashMap<String, Object>();
        }
        ruleMap.put("order", cart);

        // Leaving the following line in for backwards compatibility, but all rules should use order as the 
        // variable name.
        ruleMap.put("cart", cart);
        request.setAttribute(BLC_RULE_MAP_PARAM, ruleMap, WebRequest.SCOPE_REQUEST);

    }
    
    public Order getOverrideCart(WebRequest request) {
        Long orderId = (Long) request.getAttribute(OVERRIDE_CART_ATTR_NAME, WebRequest.SCOPE_GLOBAL_SESSION);
        Order cart = null;
        if (orderId != null) {
            cart = orderService.getOrderById(orderId);
    
            if (cart == null ||
                    cart.getStatus().equals(OrderStatus.CONFIRMED) ||
                    cart.getStatus().equals(OrderStatus.CANCELLED)) {
                return null;
            }
        }

        return cart;
    }
    
    public boolean mergeCartNeeded(Customer customer, WebRequest request) {
        // When the user is a CSR, we want to disable cart merging
        if (crossAppAuthService != null && crossAppAuthService.isAuthedFromAdmin()) {
            return false;
        }

        Customer anonymousCustomer = customerStateRequestProcessor.getAnonymousCustomer(request);
        return (anonymousCustomer != null && customer.getId() != null && !customer.getId().equals(anonymousCustomer.getId()));
    }

    public Order mergeCart(Customer customer, WebRequest request) {
        Customer anonymousCustomer = customerStateRequestProcessor.getAnonymousCustomer(request);
        MergeCartResponse mergeCartResponse;
        try {
            Order cart = orderService.findCartForCustomer(anonymousCustomer);
            mergeCartResponse = mergeCartService.mergeCart(customer, cart);
        } catch (PricingException e) {
            throw new RuntimeException(e);
        } catch (RemoveFromCartException e) {
            throw new RuntimeException(e);
        }
        
        // The anonymous customer from session is no longer needed; it can be safely removed
        request.removeAttribute(CustomerStateRequestProcessor.SESSION_ATTRIBUTE_NAME_ANONYMOUS_CUSTOMER,
                WebRequest.SCOPE_GLOBAL_SESSION);
        request.removeAttribute(CustomerStateRequestProcessor.SESSION_ATTRIBUTE_NAME_ANONYMOUS_CUSTOMER_ID,
                WebRequest.SCOPE_GLOBAL_SESSION);

        request.setAttribute(mergeCartResponseKey, mergeCartResponse, WebRequest.SCOPE_GLOBAL_SESSION);
        return mergeCartResponse.getOrder();
    }

    public static String getCartRequestAttributeName() {
        return cartRequestAttributeName;
    }

    public static void setCartRequestAttributeName(String cartRequestAttributeName) {
        CartStateRequestProcessor.cartRequestAttributeName = cartRequestAttributeName;
    }
}

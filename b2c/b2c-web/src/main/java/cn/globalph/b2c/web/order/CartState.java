package cn.globalph.b2c.web.order;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.web.order.security.CartStateRequestProcessor;
import cn.globalph.common.web.WebRequestContext;

import org.springframework.web.context.request.WebRequest;

public class CartState {

    /**
     * Gets the current cart based on the current request
     * 
     * @return the current customer's cart
     */
    public static Order getCart() {
        WebRequest request = WebRequestContext.getWebRequestContext().getWebRequest();
        return (Order) request.getAttribute(CartStateRequestProcessor.getCartRequestAttributeName(), WebRequest.SCOPE_REQUEST);
    }
    
    /**
     * Sets the current cart on the current request
     * 
     * @param cart the new cart to set
     */
    public static void setCart(Order cart) {
        WebRequest request = WebRequestContext.getWebRequestContext().getWebRequest();
        request.setAttribute(CartStateRequestProcessor.getCartRequestAttributeName(), cart, WebRequest.SCOPE_REQUEST);
    }

}

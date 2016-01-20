package cn.globalph.b2c.web.order.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <p>
 * This filter should be configured after the CustomerStateFilter listener from Spring Security.
 * Retrieves the cart for the current Customer based using the authenticated user OR creates an empty non-modifiable cart and
 * stores it in the request.
 * </p>
 */
@Component("blCartStateFilter")
public class CartStateFilter extends GenericFilterBean implements  Ordered {

    /** Logger for this class and subclasses */
    protected final Log LOG = LogFactory.getLog(getClass());

    @Resource(name = "blCartStateRequestProcessor")
    protected CartStateRequestProcessor cartStateProcessor;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {  
    	WebRequest webRequest = new ServletWebRequest((HttpServletRequest) request, (HttpServletResponse)response);
    	String path = ((HttpServletRequest)request).getServletPath();
    	if(path.startsWith("/cart") || path.startsWith("/account") || path.equals("/")){
    		webRequest.removeAttribute("orderNumber", WebRequest.SCOPE_SESSION);
    	}
        cartStateProcessor.process(webRequest);
        chain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        //FilterChainOrder has been dropped from Spring Security 3
        //return FilterChainOrder.REMEMBER_ME_FILTER+1;
        return 1502;
    }

}

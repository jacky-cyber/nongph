package cn.globalph.passport.web.core.security;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
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
 * This filter should be configured after the RememberMe listener from Spring Security.
 * Retrieves the Customer based using the authenticated user OR creates an Anonymous customer and stores them
 * in the session.  
 * Calls Customer.setCookied(true) if the authentication token is an instance of {@link org.springframework.security.providers.rememberme.RememberMeAuthenticationToken).   
 * Calls Customer.setLoggedIn(true) if the authentication token is an instance of {@link org.springframework.security.providers.UsernamePasswordAuthenticationToken}
 * </p>
 */
@Component("blCustomerStateFilter")
public class CustomerStateFilter extends GenericFilterBean implements Ordered {
    
    @Resource(name="blCustomerStateRequestProcessor")
    protected CustomerStateRequestProcessor customerStateProcessor;

    @Override
    public void doFilter(ServletRequest baseRequest, ServletResponse baseResponse, FilterChain chain) throws IOException, ServletException {
        customerStateProcessor.process(new ServletWebRequest((HttpServletRequest) baseRequest, (HttpServletResponse)baseResponse));
        chain.doFilter(baseRequest, baseResponse);
    }

    @Override
    public int getOrder() {
        //FilterChainOrder has been dropped from Spring Security 3
        //return FilterChainOrder.REMEMBER_ME_FILTER+1;
        return 1501;
    }

}

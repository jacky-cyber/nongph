package cn.globalph.b2c.web.catalog;

import cn.globalph.b2c.catalog.service.dynamic.SkuPricingConsiderationContext;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import java.io.IOException;

/**
 * 
 * @felix.wu
 * @see {@link DefaultDynamicSkuPricingFilter}
 */
public abstract class AbstractDynamicSkuPricingFilter implements DynamicSkuPricingFilter {

    public void destroy() {
        //do nothing
     }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        SkuPricingConsiderationContext.setSkuPricingConsiderationContext(getPricingConsiderations(request));
        SkuPricingConsiderationContext.setSkuPricingService(getDynamicSkuPricingService(request));
        filterChain.doFilter(request, response);
     }

    public void init(FilterConfig config) throws ServletException {
        //do nothing
    }

}

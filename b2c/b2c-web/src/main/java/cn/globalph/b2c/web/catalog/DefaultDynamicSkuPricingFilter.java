package cn.globalph.b2c.web.catalog;

import cn.globalph.b2c.catalog.service.dynamic.DynamicSkuPricingService;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.web.core.CustomerState;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;

/**
 * Register this filter via Spring DelegatingFilterProxy, or register your own implementation
 * that provides additional, desirable members to the pricingConsiderations Map
 * that is generated from the getPricingConsiderations method.
 * 
 * @felix.wu
 *
 */
public class DefaultDynamicSkuPricingFilter extends AbstractDynamicSkuPricingFilter {
    
    @Resource(name="blDynamicSkuPricingService")
    protected DynamicSkuPricingService skuPricingService;
    
    @Resource(name="blCustomerState")
    protected CustomerState customerState;

    public DynamicSkuPricingService getDynamicSkuPricingService(ServletRequest request) {
        return skuPricingService;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public HashMap getPricingConsiderations(ServletRequest request) {
        HashMap pricingConsiderations = new HashMap();
        Customer customer = customerState.getCustomer((HttpServletRequest)  request);
        pricingConsiderations.put("customer", customer);
        
        return pricingConsiderations;
    }

}

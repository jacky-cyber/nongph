package cn.globalph.b2c.web.catalog;

import cn.globalph.b2c.catalog.service.dynamic.DynamicSkuPricingService;
import cn.globalph.b2c.catalog.service.dynamic.SkuPricingConsiderationContext;
import cn.globalph.b2c.product.domain.Sku;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;

import java.util.HashMap;

/**
 * 复制建立 {@link SkuPricingConsiderationContext}.
 * 不要直接实现该即可，考虑继承{@link DefaultDynamicSkuPricingFilter}并覆写相应方法
 * 
 * @author felix.wu
 * @see {@link DefaultDynamicSkuPricingFilter}
 * @see {@link AbstractDynamicSkuPricingFilter}
 * @see {@link DynamicSkuPricingService}
 * @see {@link SkuPricingConsiderationContext}
 */
public interface DynamicSkuPricingFilter extends Filter {

    /**
     * The result of this invocation should be set on
     * {@link SkuPricingConsiderationContext#setSkuPricingConsiderationContext(HashMap)} and ultimately passed to
     * {@link DynamicSkuPricingService} to determine prices.
     * 
     * @param request
     * @return a map of considerations to be used by the service in {@link #getDynamicSkuPricingService(ServletRequest)}.
     * @see {@link SkuPricingConsiderationContext#getSkuPricingConsiderationContext()}
     * @see {@link DynamicSkuPricingService}
     */
    @SuppressWarnings("rawtypes")
    public HashMap getPricingConsiderations(ServletRequest request);

    /**
     * The result of this invocation should be set on
     * {@link SkuPricingConsiderationContext#setSkuPricingService(DynamicSkuPricingService)}. This is the service that will
     * be used in calculating dynamic prices for a Sku or product option value
     * 
     * @param request
     * @return
     * @see {@link Sku#getRetailPrice()}
     * @see {@link Sku#getSalePrice()}
     */
    public DynamicSkuPricingService getDynamicSkuPricingService(ServletRequest request);
    
}

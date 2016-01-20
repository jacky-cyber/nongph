package cn.globalph.b2c.catalog.service.dynamic;

import cn.globalph.b2c.product.domain.SkuImpl;
import cn.globalph.common.classloader.release.ThreadLocalManager;

import java.util.HashMap;

/**
 * Convenient place to store the pricing considerations context and the pricing service on thread local. This class is
 * usually filled out by a {@link cn.globalph.b2c.web.catalog.DynamicSkuPricingFilter}. The default
 * implementation of this is {@link cn.globalph.b2c.web.catalog.DefaultDynamicSkuPricingFilter}.
 * 
 * @author felix.wu
 * @see {@link SkuImpl#getRetailPrice}
 * @see {@link SkuImpl#getSalePrice}
 * @see {@link cn.globalph.b2c.web.catalog.DynamicSkuPricingFilter}
 */
public class SkuPricingConsiderationContext {

    private static final ThreadLocal<SkuPricingConsiderationContext> skuPricingConsiderationContext = ThreadLocalManager.createThreadLocal(SkuPricingConsiderationContext.class);
     
     /**
      *上下文，其中包含影响架构的因素 
     * @return
      */
    public static HashMap<String, Object> getSkuPricingConsiderationContext() {
        return skuPricingConsiderationContext.get().considerations;
     }
    
    public static void setSkuPricingConsiderationContext(HashMap<String, Object> skuPricingConsiderations) {
        skuPricingConsiderationContext.get().considerations = skuPricingConsiderations;
     }

    public static DynamicSkuPricingService getSkuPricingService() {
        return skuPricingConsiderationContext.get().pricingService;
     }
    
    public static void setSkuPricingService(DynamicSkuPricingService skuPricingService) {
        skuPricingConsiderationContext.get().pricingService = skuPricingService;
     }
    
    public static boolean hasDynamicPricing() {
        return (
            getSkuPricingConsiderationContext() != null &&
            getSkuPricingConsiderationContext().size() > 0 &&
            getSkuPricingService() != null
           );
     }
    
     /**
      * 计算价格的服务
      */
    protected DynamicSkuPricingService pricingService;
    
    /**
     *上下文，其中包含影响架构的因素 
     */
    protected HashMap<String, Object> considerations;
}

package cn.globalph.b2c.catalog.service.dynamic;

import cn.globalph.b2c.product.domain.SkuImpl;
import cn.globalph.common.classloader.release.ThreadLocalManager;

import java.util.HashMap;

/**
 * Convenient place to store the active date context and the related service on thread local. 
 * 
 * @felix.wu
 * @see {@link SkuImpl#getActiveStartDate()}
 * @see {@link SkuImpl#getActiveEndDate()}
 */
public class SkuActiveDateConsiderationContext {

    private static final ThreadLocal<SkuActiveDateConsiderationContext> skuActiveDatesConsiderationContext =
            ThreadLocalManager.createThreadLocal(SkuActiveDateConsiderationContext.class);

    public static HashMap<String, Object> getSkuActiveDateConsiderationContext() {
        return SkuActiveDateConsiderationContext.skuActiveDatesConsiderationContext.get().considerations;
    }

    public static void setSkuActiveDateConsiderationContext(HashMap<String, Object> skuPricingConsiderations) {
        SkuActiveDateConsiderationContext.skuActiveDatesConsiderationContext.get().considerations = skuPricingConsiderations;
    }

    public static DynamicSkuActiveDatesService getSkuActiveDatesService() {
        return SkuActiveDateConsiderationContext.skuActiveDatesConsiderationContext.get().service;
    }

    public static void setSkuActiveDatesService(DynamicSkuActiveDatesService skuPricingService) {
        SkuActiveDateConsiderationContext.skuActiveDatesConsiderationContext.get().service = skuPricingService;
    }

    public static boolean hasDynamicActiveDates() {
        return (getSkuActiveDatesService() != null);
    }

    protected DynamicSkuActiveDatesService service;
    protected HashMap<String, Object> considerations;

}

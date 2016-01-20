package cn.globalph.b2c.catalog.service.dynamic;

import cn.globalph.b2c.product.domain.ProductOptionValueImpl;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.SkuBundleItem;
import cn.globalph.common.money.Money;

import javax.annotation.Nonnull;

import java.util.HashMap;

/**
 * <p>为{@link Sku}动态计算价格接口. This should be hooked up via a custom subclass of 
 * {@link cn.globalph.b2c.web.catalog.DefaultDynamicSkuPricingFilter} where an implementation of this class
 * should be injected and returned in the getPricing() method.</p>
 * 
 * <p>Rather than implementing this interface directly, consider subclassing the {@link DefaultDynamicSkuPricingServiceImpl}
 * and providing overrides to methods there.</p>
 * 
 * @author felix.wu
 * @see {@link DefaultDynamicSkuPricingServiceImpl}
 * @see {@link cn.globalph.b2c.web.catalog.DefaultDynamicSkuPricingFilter}
 * @see {@link SkuPricingConsiderationContext}
 */
public interface DynamicSkuPricingService {

    /**
     * While this method should return a {@link DynamicSkuPrices} (and not just null) the members of the result can all
     * be null; they do not have to be set
     * 
     * @param sku
     * @param skuPricingConsiderations
     * @return
     */
    @Nonnull
    @SuppressWarnings("rawtypes")
    public DynamicSkuPrices getSkuPrices(Sku sku, HashMap skuPricingConsiderations);

    /**
     * Used for t
     * 
     * @param sku
     * @param skuPricingConsiderations
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DynamicSkuPrices getSkuBundleItemPrice(SkuBundleItem sku, HashMap skuPricingConsiderations);

    /**
     * Execute dynamic pricing on the price of a product option value. 
     * @param productOptionValueImpl
     * @param priceAdjustment
     * @param skuPricingConsiderationContext
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DynamicSkuPrices getPriceAdjustment(ProductOptionValueImpl productOptionValueImpl, Money priceAdjustment,
            HashMap skuPricingConsiderationContext);

}

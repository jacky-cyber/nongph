package cn.globalph.b2c.catalog.service.dynamic;

import cn.globalph.b2c.product.domain.ProductOptionValueImpl;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.SkuBundleItem;
import cn.globalph.common.money.Money;

import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Default implementation of the {@link DynamicSkuPricingService} which simply ignores the considerations hashmap in all
 * method implementations
 * 
 * @felix.wu
 * 
 */
@Service("blDynamicSkuPricingService")
public class DefaultDynamicSkuPricingServiceImpl implements DynamicSkuPricingService {

    @Override
    @SuppressWarnings("rawtypes")
    public DynamicSkuPrices getSkuPrices(Sku sku, HashMap skuPricingConsiderations) {
        DynamicSkuPrices prices = new DynamicSkuPrices();
        prices.setRetailPrice(sku.getRetailPrice());
        prices.setSalePrice(sku.getSalePrice());
        prices.setPriceAdjustment(sku.getProductOptionValueAdjustments());
        return prices;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public DynamicSkuPrices getSkuBundleItemPrice(SkuBundleItem skuBundleItem,
            HashMap skuPricingConsiderations) {
        DynamicSkuPrices prices = new DynamicSkuPrices();
        prices.setSalePrice(skuBundleItem.getSalePrice());
        return prices;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public DynamicSkuPrices getPriceAdjustment(ProductOptionValueImpl productOptionValueImpl, Money priceAdjustment,
            HashMap skuPricingConsiderationContext) {
        DynamicSkuPrices prices = new DynamicSkuPrices();

        prices.setPriceAdjustment(priceAdjustment);
        return prices;
    }

}

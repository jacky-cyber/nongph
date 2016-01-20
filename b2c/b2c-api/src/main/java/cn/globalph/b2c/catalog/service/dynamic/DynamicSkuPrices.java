package cn.globalph.b2c.catalog.service.dynamic;

import cn.globalph.common.money.Money;

import java.io.Serializable;

/**
 * DTO to represent pricing overrides returned from invocations to {@link DynamicSkuPricingService}
 * @felix.wu
 * @see {@link DynamicSkuPricingService}
 */
public class DynamicSkuPrices implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Money retailPrice;
    protected Money salePrice;
    protected Money priceAdjustment;

    public Money getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(Money retailPrice) {
        this.retailPrice = retailPrice;
    }

    public Money getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Money salePrice) {
        this.salePrice = salePrice;
    }

    public Money getPriceAdjustment() {
        return priceAdjustment;
    }

    public void setPriceAdjustment(Money priceAdjustment) {
        this.priceAdjustment = priceAdjustment;
    }

}

package cn.globalph.b2c.product.domain;

import cn.globalph.common.money.Money;

import java.io.Serializable;

/**
 * Represents the {@link cn.globalph.b2c.product.domain.Sku} being sold in a bundle along with metadata
 * about the relationship itself like how many items should be included in the
 * bundle
 *
 * @author felix.wu
 * @see SkuBundle, Sku
 */
public interface SkuBundleItem extends Serializable {

    public Long getId();

    public void setId(Long id);

    public Integer getQuantity();

    public void setQuantity(Integer quantity);

    /**
    * Allows for overriding the related Product's sale price. This is only used
    * if the pricing model for the bundle is a composition of its parts
    * getProduct().getDefaultSku().getSalePrice()
    *
    * @param itemSalePrice The sale price for this bundle item
    */
    public void setSalePrice(Money salePrice);

    /**
    * @return this itemSalePrice if it is set,
    *         getProduct().getDefaultSku().getSalePrice() if this item's itemSalePrice is
    *         null
    */
    public Money getSalePrice();

    public SkuBundle getBundle();

    public void setBundle(SkuBundle bundle);

    public Money getRetailPrice();

    public Sku getSku();

    public void setSku(Sku sku);

    /**
     * Removes any currently stored dynamic pricing
     */
    public void clearDynamicPrices();
}

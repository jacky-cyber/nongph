package cn.globalph.b2c.product.domain;

import cn.globalph.common.value.Searchable;


/**
 * Implementations of this interface are used to hold data about a SKU's Attributes.
 * A SKU Attribute is a designator on a SKU that differentiates it from other similar SKUs
 * (for example: Blue attribute for hat).
 * <br>
 * <br>
 * You should implement this class if you want to make significant changes to how the
 * class is persisted.  If you just want to add additional fields then you should
 * extend {@link SkuAttributeImpl}.
 *
 * @see {@link SkuAttributeImpl}, {@link Sku}
 * @author btaylor
 *
 */
public interface SkuAttribute extends Searchable<String> {

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId();

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Long id);

    /**
     * Gets the sku.
     *
     * @return the sku
     */
    public Sku getSku();

    /**
     * Sets the sku.
     *
     * @param sku the new sku
     */
    public void setSku(Sku sku);

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName();

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name);

}

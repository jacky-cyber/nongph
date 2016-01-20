package cn.globalph.b2c.product.domain;

import cn.globalph.common.money.Money;

import java.io.Serializable;

/**
 * 
 * <p>Stores the values for a given product option.</p>
 * 
 * <p>For example, a ProductOption of type "color" might have values of ("red","blue").</p>
 * 
 * <p>ProductOptionValues can also have a price adjustment associated with it which
 * will be automatically added to the Sku retail price and sale price</p>
 *
 * @author bpolster.
 */
public interface ProductOptionValue extends Serializable {
    
    /**
     * Returns unique identifier of the product option value.
     * @return
     */
    public Long getId();

    /**
     * Sets the unique identifier of the product option value.
     * @param id
     */
    public void setId(Long id);

    /**
     * Gets the option value.  (e.g. "red")
     * @param
     */
    public String getAttributeValue();

    /**
     * Sets the option value.  (e.g. "red")
     * @param attributeValue
     */
    public void setAttributeValue(String attributeValue);

    /**
     * Returns the order that the option value should be displayed in.
     * @return
     */
    public Long getDisplayOrder();

    /**
     * Sets the display order.
     * @param order
     */
    public void setDisplayOrder(Long order);

    /**
     * Gets the price adjustment associated with this value. For instance,
     * if this ProductOptionValue represented an extra-large shirt, that
     * might be a $1 upcharge. This adjustments will be automatically
     * added to the Sku retail price and sale price
     * <br />
     * <br />
     * Note: This could also be a negative value if you wanted to offer
     * a particular ProductOptionValue at a discount
     * 
     * @return the price adjustment for this 
     */
    public Money getPriceAdjustment();

    /**
     * Gets the price adjustment associated with this value. For instance,
     * if this ProductOptionValue represented an extra-large shirt, that
     * might be a $1 upcharge. These adjustments will be automatically
     * added to the Sku retail price and sale price. To offer this
     * particular ProductOptionValue at a discount, you could also provide
     * a negative value here
     * 
     * @param priceAdjustment
     */
    public void setPriceAdjustment(Money priceAdjustment);
    
    /**
     * Returns the associated ProductOption
     *
     * @return
     */
    public ProductOption getProductOption();

    /**
     * Sets the associated product option.
     * @param productOption
     */
    public void setProductOption(ProductOption productOption);
    
}

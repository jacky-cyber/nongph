package cn.globalph.b2c.catalog.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.globalph.b2c.product.domain.Product;

/**
 * Implementations of this interface are used to hold data about the many-to-many relationship between
 * the Category table and the Product table.  This entity is only used for executing a named
 * query.
 * <br>
 * <br>
 * You should implement this class if you want to make significant changes to the
 * relationship between Category and Product.  If you just want to add additional fields
 * then you should extend {@link CategoryProductXrefImpl}.
 * 
 *  @see {@link CategoryProductXrefImpl},{@link Category}, {@link Product}
 * 
 */
public interface CategoryProductXref extends Serializable {

    /**
     * Gets the category.
     * 
     * @return the category
     */
    Category getCategory();

    /**
     * Sets the category.
     * 
     * @param category the new category
     */
    void setCategory(Category category);

    /**
     * Gets the product.
     * 
     * @return the product
     */
    Product getProduct();

    /**
     * Sets the product.
     * 
     * @param product the new product
     */
    void setProduct(Product product);

    /**
     * Gets the display order.
     * 
     * @return the display order
     */
    BigDecimal getDisplayOrder();

    /**
     * Sets the display order.
     * 
     * @param displayOrder the new display order
     */
    void setDisplayOrder(BigDecimal displayOrder);

    public void setId(Long id);

    public Long getId();
}

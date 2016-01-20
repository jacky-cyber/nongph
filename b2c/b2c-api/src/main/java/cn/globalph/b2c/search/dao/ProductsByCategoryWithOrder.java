package cn.globalph.b2c.search.dao;

import java.math.BigDecimal;

/**
 * Container object for the results of a lightweight query that retrieves the product ids
 * for a category along with their display order.
 *
 */
public class ProductsByCategoryWithOrder {

    protected Long productId;
    protected BigDecimal displayOrder;

    public ProductsByCategoryWithOrder(Long productId, BigDecimal displayOrder) {
        this.productId = productId;
        this.displayOrder = displayOrder;
    }
    
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public BigDecimal getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(BigDecimal displayOrder) {
        this.displayOrder = displayOrder;
    }
    
}

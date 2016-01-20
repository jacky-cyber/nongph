package cn.globalph.b2c.search.dao;


/**
 * Container object for the results of a lightweight query that retrieves the parent category
 * for a child product
 *
 */
public class ParentCategoryByProduct {

    protected Long parent;
    protected Long product;

    public ParentCategoryByProduct(Long parent, Long product) {
        this.parent = parent;
        this.product = product;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }
    
}

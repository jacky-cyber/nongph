package cn.globalph.b2c.catalog.domain;


import java.math.BigDecimal;

import cn.globalph.b2c.product.domain.Product;

public interface FeaturedProduct extends PromotableProduct {

    Long getId();

    void setId(Long id);

    Category getCategory();

    void setCategory(Category category);

    Product getProduct();

    void setProduct(Product product);

    void setSequence(BigDecimal sequence);
    
    BigDecimal getSequence();

    String getPromotionMessage();

    void setPromotionMessage(String promotionMessage);

    /**
     * Pass through to getProdcut() to meet the contract for promotable product.
     * @return
     */
    Product getRelatedProduct();

}

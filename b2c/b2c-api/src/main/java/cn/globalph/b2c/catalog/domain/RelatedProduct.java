package cn.globalph.b2c.catalog.domain;

import java.math.BigDecimal;

import cn.globalph.b2c.product.domain.Product;

public interface RelatedProduct extends PromotableProduct {
    
    public Long getId();

    public Product getProduct();
    
    public Category getCategory();

    public Product getRelatedProduct();

    public String getPromotionMessage();

    public BigDecimal getSequence();

    public void setId(Long id);

    public void setProduct(Product product);
    
    public void setCategory(Category category);

    public void setRelatedProduct(Product relatedProduct);

    public void setPromotionMessage(String promotionMessage);

    public void setSequence(BigDecimal sequence);
}

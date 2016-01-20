package cn.globalph.b2c.catalog.domain;

import java.io.Serializable;

import cn.globalph.b2c.product.domain.Product;

public interface PromotableProduct extends Serializable {
    Product getRelatedProduct();
    
    String getPromotionMessage();
}

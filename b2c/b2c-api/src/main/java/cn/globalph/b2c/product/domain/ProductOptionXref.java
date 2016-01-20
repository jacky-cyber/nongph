package cn.globalph.b2c.product.domain;

import java.io.Serializable;

/**
 * @author felix.wu
 */
public interface ProductOptionXref extends Serializable {

    Long getId();

    void setId(Long id);

    Product getProduct();

    void setProduct(Product product);

    ProductOption getProductOption();

    void setProductOption(ProductOption productOption);

}

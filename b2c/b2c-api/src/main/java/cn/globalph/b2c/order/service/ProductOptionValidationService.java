
package cn.globalph.b2c.order.service;

import cn.globalph.b2c.product.domain.ProductOption;

public interface ProductOptionValidationService {

    public abstract Boolean validate(ProductOption productOption, String value);

}

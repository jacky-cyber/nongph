package cn.globalph.b2c.order.domain;

import cn.globalph.b2c.product.domain.Sku;

/**
 * Interface indicating that an item contains a getSku method.
 *
 * Intended for use by subclasses of OrderItem that contain a sku.
 *
 * Created by bpolster.
 */
public interface SkuAccessor {
    Sku getSku();
}

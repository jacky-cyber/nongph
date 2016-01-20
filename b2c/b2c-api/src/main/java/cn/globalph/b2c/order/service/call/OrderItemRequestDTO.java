package cn.globalph.b2c.order.service.call;

import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.common.money.Money;

import java.util.HashMap;
import java.util.Map;

/**
 * Only the Sku and quantity are required to add an item to an order.
 *
 * The category can be inferred from the product's default category.
 *
 * The sku can be inferred from either the passed in attributes as they are compared to the product's options or
 * the sku can be determined from the product's default sku.
 * 
 * When adding a bundle using this DTO, you MUST have the {@link ProductBundle} included in the productId for it to
 * properly instantiate the {@link BundleOrderItem}
 * 
 * Important Note:  To protect against misuse, the {@link OrderService}'s addItemToCart method will blank out
 * any values passed in on this DTO for the overrideSalePrice or overrideRetailPrice.
 * 
 * Instead, implementors should call the more explicit addItemWithPriceOverrides.
 *
 */
public class OrderItemRequestDTO {

    private Long skuId;
    private Long orderItemId;
    private Integer quantity;
    private Money overrideSalePrice;
    private Money overrideRetailPrice;
    
    public OrderItemRequestDTO() {}
    
    public OrderItemRequestDTO(Long skuId, Integer quantity) {
        setSkuId(skuId);
        setQuantity(quantity);
    }
    
    public Long getSkuId() {
        return skuId;
    }

    public OrderItemRequestDTO setSkuId(Long skuId) {
        this.skuId = skuId;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public OrderItemRequestDTO setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }
    
    public Long getOrderItemId() {
        return orderItemId;
    }

    public OrderItemRequestDTO setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
        return this;
    }

    public Money getOverrideSalePrice() {
        return overrideSalePrice;
    }

    public void setOverrideSalePrice(Money overrideSalePrice) {
        this.overrideSalePrice = overrideSalePrice;
    }

    public Money getOverrideRetailPrice() {
        return overrideRetailPrice;
    }

    public void setOverrideRetailPrice(Money overrideRetailPrice) {
        this.overrideRetailPrice = overrideRetailPrice;
    }    
}

package cn.globalph.b2c.order.service.call;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.product.domain.SkuBundle;
import cn.globalph.common.money.Money;

import java.util.HashMap;
import java.util.Map;

public class ProductBundleOrderItemRequest {

    protected String name;
    protected Category category;
    protected Order order;
    protected int quantity;
    protected SkuBundle skuBundle;
    private Map<String,String> itemAttributes = new HashMap<String,String>();
    protected Money salePriceOverride;
    protected Money retailPriceOverride;

    public ProductBundleOrderItemRequest() {}
    
    public String getName() {
        return name;
    }

    public ProductBundleOrderItemRequest setName(String name) {
        this.name = name;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public ProductBundleOrderItemRequest setCategory(Category category) {
        this.category = category;
        return this;
    }
        
    public ProductBundleOrderItemRequest setOrder(Order order) {
        this.order = order;
        return this;
    }
    
    public Order getOrder() {
        return order;
    }

    public int getQuantity() {
        return quantity;
     }

    public ProductBundleOrderItemRequest setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
     }

    public SkuBundle getSkuBundle() {
        return skuBundle;
     }

    public ProductBundleOrderItemRequest setProductBundle(SkuBundle skuBundle) {
        this.skuBundle = skuBundle;
        return this;
    }

    public Map<String, String> getItemAttributes() {
        return itemAttributes;
    }

    public ProductBundleOrderItemRequest setItemAttributes(Map<String, String> itemAttributes) {
        this.itemAttributes = itemAttributes;
        return this;
    }

    public Money getSalePriceOverride() {
        return salePriceOverride;
    }

    public void setSalePriceOverride(Money salePriceOverride) {
        this.salePriceOverride = salePriceOverride;
    }

    public Money getRetailPriceOverride() {
        return retailPriceOverride;
    }

    public void setRetailPriceOverride(Money retailPriceOverride) {
        this.retailPriceOverride = retailPriceOverride;
    }

}

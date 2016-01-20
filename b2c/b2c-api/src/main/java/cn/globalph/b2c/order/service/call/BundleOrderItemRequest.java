package cn.globalph.b2c.order.service.call;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.common.money.Money;

import java.util.ArrayList;
import java.util.List;

public class BundleOrderItemRequest {

    protected String name;
    protected Category category;
    protected int quantity;
    protected Order order;
    protected List<DiscreteOrderItemRequest> discreteOrderItems = new ArrayList<DiscreteOrderItemRequest>();
    protected Money salePriceOverride;
    protected Money retailPriceOverride;

    
    public Order getOrder() {
        return order;
    }

    
    public void setOrder(Order order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<DiscreteOrderItemRequest> getDiscreteOrderItems() {
        return discreteOrderItems;
    }

    public void setDiscreteOrderItems(List<DiscreteOrderItemRequest> discreteOrderItems) {
        this.discreteOrderItems = discreteOrderItems;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BundleOrderItemRequest other = (BundleOrderItemRequest) obj;
        if (category == null) {
            if (other.category != null)
                return false;
        } else if (!category.equals(other.category))
            return false;
        if (discreteOrderItems == null) {
            if (other.discreteOrderItems != null)
                return false;
        } else if (!discreteOrderItems.equals(other.discreteOrderItems))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (quantity != other.quantity)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((discreteOrderItems == null) ? 0 : discreteOrderItems.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + quantity;
        return result;
    }
}

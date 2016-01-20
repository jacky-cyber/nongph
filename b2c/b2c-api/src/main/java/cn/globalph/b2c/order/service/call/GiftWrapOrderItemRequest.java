package cn.globalph.b2c.order.service.call;

import cn.globalph.b2c.order.domain.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class GiftWrapOrderItemRequest extends DiscreteOrderItemRequest {

    private List<OrderItem> wrappedItems = new ArrayList<OrderItem>();

    public List<OrderItem> getWrappedItems() {
        return wrappedItems;
    }

    public void setWrappedItems(List<OrderItem> wrappedItems) {
        this.wrappedItems = wrappedItems;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((wrappedItems == null) ? 0 : wrappedItems.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GiftWrapOrderItemRequest other = (GiftWrapOrderItemRequest) obj;
        if (wrappedItems == null) {
            if (other.wrappedItems != null)
                return false;
        } else if (!wrappedItems.equals(other.wrappedItems))
            return false;
        return true;
    }

}

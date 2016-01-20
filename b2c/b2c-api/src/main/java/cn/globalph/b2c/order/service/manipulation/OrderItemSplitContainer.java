package cn.globalph.b2c.order.service.manipulation;

import cn.globalph.b2c.offer.service.discount.domain.PromotableOrderItem;
import cn.globalph.b2c.order.domain.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItemSplitContainer {
    
    protected OrderItem key;
    protected List<PromotableOrderItem> splitItems = new ArrayList<PromotableOrderItem>();
    
    public OrderItem getKey() {
        return key;
    }
    
    public void setKey(OrderItem key) {
        this.key = key;
    }
    
    public List<PromotableOrderItem> getSplitItems() {
        return splitItems;
    }
    
    public void setSplitItems(List<PromotableOrderItem> splitItems) {
        this.splitItems = splitItems;
    }

}

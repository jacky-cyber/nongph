package cn.globalph.b2c.order.dao;

import java.util.List;

import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.OrderItemPriceDetail;
import cn.globalph.b2c.order.domain.OrderItemQualifier;
import cn.globalph.b2c.order.service.type.OrderItemType;

public interface OrderItemDao {

    OrderItem readOrderItemById(Long orderItemId);

    OrderItem save(OrderItem orderItem);

    void delete(OrderItem orderItem);

    OrderItem create(OrderItemType orderItemType);

    OrderItem saveOrderItem(OrderItem orderItem);

    OrderItemPriceDetail createOrderItemPriceDetail();

    OrderItemQualifier createOrderItemQualifier();

    /**
     * Sets the initial orderItemPriceDetail for the item.
     */
    OrderItemPriceDetail initializeOrderItemPriceDetails(OrderItem item);
    
    List<OrderItem> readOrderItemsByOrderId(Long orderId);

}

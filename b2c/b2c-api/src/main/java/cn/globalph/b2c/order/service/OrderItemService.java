package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.call.BundleOrderItemRequest;
import cn.globalph.b2c.order.service.call.DiscreteOrderItemRequest;
import cn.globalph.b2c.order.service.call.OrderItemRequest;
import cn.globalph.b2c.order.service.call.OrderItemRequestDTO;
import cn.globalph.b2c.order.service.call.ProductBundleOrderItemRequest;

import java.util.HashMap;
import java.util.List;

public interface OrderItemService {
    
    public OrderItem readOrderItemById(Long orderItemId);

    public OrderItem saveOrderItem(OrderItem orderItem);
    
    public void delete(OrderItem item);
        
    public OrderItem createDynamicPriceDiscreteOrderItem(final DiscreteOrderItemRequest itemRequest, @SuppressWarnings("rawtypes") HashMap skuPricingConsiderations);

    /**
     * Used to create "manual" product bundles.   Manual product bundles are primarily designed
     * for grouping items in the cart display.    Typically ProductBundle will be used to
     * achieve non programmer related bundles.
     *
     *
     * @param itemRequest
     * @return
     */
    public OrderItem createBundleOrderItem(BundleOrderItemRequest itemRequest);

    public OrderItem createBundleOrderItem(ProductBundleOrderItemRequest itemRequest);

    public OrderItem createBundleOrderItem(ProductBundleOrderItemRequest itemRequest, boolean saveItem);

    /**
     * Creates an OrderItemRequestDTO object that most closely resembles the given OrderItem.
     * That is, it will copy the SKU and quantity and attempt to copy the product and category
     * if they exist.
     * 
     * @param item the item to copy
     * @return the OrderItemRequestDTO that mirrors the item
     */
    public OrderItemRequestDTO buildOrderItemRequestDTOFromOrderItem(OrderItem item);

    public OrderItem updateDiscreteOrderItem(OrderItem orderItem, DiscreteOrderItemRequest itemRequest);

    public OrderItem createOrderItem(OrderItemRequest itemRequest);

    List<OrderItem> findOrderItemsByOrderId(Long orderId);
}

package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.domain.OrderAddress;

public interface OrderAddressService {

    OrderAddress save(OrderAddress orderAddress);

    OrderAddress readOrderAddressById(Long id);

    OrderAddress create();

    void delete(OrderAddress orderAddress);

    void deleteOrderAddressById(Long id);

    OrderAddress readOrderAddressByOrderId(Long orderId);
}

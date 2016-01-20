package cn.globalph.b2c.order.dao;

import cn.globalph.b2c.order.domain.OrderAddress;
import cn.globalph.common.dao.BasicEntityDao;

public interface OrderAddressDao extends BasicEntityDao<OrderAddress> {

    OrderAddress create();

    void deleteOrderAddressById(Long id);

    OrderAddress readOrderAddressByOrderId(Long orderId);

    OrderAddress save(OrderAddress orderAddress);
}

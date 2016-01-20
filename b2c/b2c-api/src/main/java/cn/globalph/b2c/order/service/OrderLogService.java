package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.domain.OrderLog;

import java.util.List;

public interface OrderLogService {

    OrderLog save(OrderLog orderLog);

    OrderLog readOrderLogById(Long id);

    OrderLog create();

    void delete(OrderLog orderLog);

    void deleteById(Long id);

    List<OrderLog> findOrderLogsByOrderId(Long orderId, Boolean isDisplay);
}

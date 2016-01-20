package cn.globalph.b2c.order.dao;

import cn.globalph.b2c.order.domain.OrderLog;
import cn.globalph.common.dao.BasicEntityDao;

import java.util.List;

public interface OrderLogDao extends BasicEntityDao<OrderLog> {

    OrderLog create();

    void deleteById(Long id);

    List<OrderLog> findOrderLogsByOrderId(Long orderId, Boolean isDisplay);
}

package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.dao.OrderLogDao;
import cn.globalph.b2c.order.domain.OrderLog;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author steven
 * @date 6/19/15
 */
@Repository("phOrderLogService")
public class OrderLogServiceImpl implements OrderLogService {

    @Resource(name = "phOrderLogDao")
    protected OrderLogDao orderLogDao;

    @Override
    public OrderLog save(OrderLog orderLog) {
        return orderLogDao.persist(orderLog);
    }

    @Override
    public OrderLog readOrderLogById(Long id) {
        return orderLogDao.get(id);
    }

    @Override
    public OrderLog create() {
        return orderLogDao.create();
    }

    @Override
    public void delete(OrderLog orderLog) {
        orderLogDao.delete(orderLog);
    }

    @Override
    public void deleteById(Long id) {
        orderLogDao.deleteById(id);
    }

    @Override
    public List<OrderLog> findOrderLogsByOrderId(Long orderId, Boolean isDisplay) {
        return orderLogDao.findOrderLogsByOrderId(orderId, isDisplay);
    }
}

package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.dao.OrderAddressDao;
import cn.globalph.b2c.order.domain.OrderAddress;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author steven
 * @date 6/19/15
 */
@Service("nphOrderAddressService")
public class OrderAddressServiceImpl implements OrderAddressService {

    @Resource(name = "nphOrderAddressDao")
    protected OrderAddressDao orderAddressDao;

    @Override
    public OrderAddress save(OrderAddress orderAddress) {
        return orderAddressDao.save(orderAddress);
    }

    @Override
    public OrderAddress readOrderAddressById(Long id) {
        return orderAddressDao.get(id);
    }

    @Override
    public OrderAddress create() {
        return orderAddressDao.create();
    }

    @Override
    public void delete(OrderAddress orderAddress) {
        orderAddressDao.delete(orderAddress);
    }

    @Override
    public void deleteOrderAddressById(Long id) {
        orderAddressDao.deleteOrderAddressById(id);
    }

    @Override
    public OrderAddress readOrderAddressByOrderId(Long orderId) {
        return orderAddressDao.readOrderAddressByOrderId(orderId);
    }
}

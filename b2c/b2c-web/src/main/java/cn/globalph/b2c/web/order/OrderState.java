package cn.globalph.b2c.web.order;

import cn.globalph.b2c.order.dao.OrderDao;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.passport.domain.Customer;

import javax.annotation.Resource;

import java.util.HashMap;

/**
 * 请求范围的用来存当前订单ID的容器。 
 * 在一个请求中，如果需要使用订单可以通过这个对象获取。
 * OrderState使用DAO获得完全的完整的订单信息。
 */
public class OrderState {

    private final HashMap<Long, Long> orders = new HashMap<Long, Long>();

    @Resource(name = "blOrderDao")
    private OrderDao orderDao;
    
    private boolean updateSalePriceToRetailPriceIfNone = true;
    
    public boolean isUpdateSalePriceToRetailPriceIfNone() {
		return updateSalePriceToRetailPriceIfNone;
	}

	public void setUpdateSalePriceToRetailPriceIfNone(
			boolean updateSalePriceToRetailPriceIfNone) {
		this.updateSalePriceToRetailPriceIfNone = updateSalePriceToRetailPriceIfNone;
	}

	public Order getOrder(Customer customer) {
        final Long customerOrderId = orders.get(customer.getId());
    	if ( customerOrderId == null) {
            return null;
        } else {
        	return orderDao.get( customerOrderId );
        }
    }

    public Order setOrder(Customer customer, Order order) {
        if (customer != null && order != null) {
            orders.put(customer.getId(), order.getId());
            if  (updateSalePriceToRetailPriceIfNone ) {
                order = orderDao.updateSalePriceToRetailPriceIfNone(order);
            }
        }
        return order;
    }
}

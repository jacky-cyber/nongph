package cn.globalph.b2c.order.dao;

import cn.globalph.b2c.order.domain.OrderAddress;
import cn.globalph.b2c.order.domain.OrderAddressImpl;
import cn.globalph.common.dao.BasicEntityDaoImpl;
import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

/**
 * @author steven
 * @date 6/19/15
 */
@Repository("nphOrderAddressDao")
public class OrderAddressDaoImpl extends BasicEntityDaoImpl<OrderAddress> implements OrderAddressDao {

    @Override
    public OrderAddress create() {
        return (OrderAddress) entityConfiguration.createEntityInstance(OrderAddress.class.getName());
    }

    @Override
    public void deleteOrderAddressById(Long id) {
        OrderAddress orderAddress = get(id);
        if (orderAddress != null) {
            em.remove(orderAddress);
        }
    }

    @Override
    public OrderAddress readOrderAddressByOrderId(Long orderId) {
        TypedQuery<OrderAddress> query = em.createNamedQuery("PH_READ_ORDER_ADDRESS_BY_ORDER_ID", OrderAddress.class);
        query.setParameter("orderId", orderId);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");

        return query.getResultList().isEmpty() ? null : query.getResultList().get(0);
    }

    @Override
    public OrderAddress save(OrderAddress orderAddress) {
        return em.merge(orderAddress);
    }

    @Override
    public Class<? extends OrderAddress> getImplClass() {
        return OrderAddressImpl.class;
    }
}

package cn.globalph.b2c.order.dao;

import cn.globalph.b2c.order.domain.OrderLog;
import cn.globalph.b2c.order.domain.OrderLogImpl;
import cn.globalph.common.dao.BasicEntityDaoImpl;
import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author steven
 * @since 6/29/15
 */
@Repository("phOrderLogDao")
public class OrderLogDaoImpl extends BasicEntityDaoImpl<OrderLog> implements OrderLogDao {
    @Override
    public OrderLog create() {
        return (OrderLog) entityConfiguration.createEntityInstance(OrderLog.class.getName());
    }

    @Override
    public void deleteById(Long id) {
        OrderLog orderLog = get(id);
        if (orderLog != null) {
            em.remove(orderLog);
        }
    }

    @Override
    public List<OrderLog> findOrderLogsByOrderId(Long orderId, Boolean isDisplay) {
        TypedQuery<OrderLog> query = em.createNamedQuery("PH_FIND_ORDER_LOG_BY_ORDER_ID", OrderLog.class);
        query.setParameter("orderId", orderId);
        query.setParameter("isDisplay", isDisplay);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");

        return query.getResultList();
    }

    @Override
    public Class<? extends OrderLog> getImplClass() {
        return OrderLogImpl.class;
    }
}

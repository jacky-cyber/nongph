package cn.globalph.b2c.order.dao;

import java.util.List;

import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.OrderItemImpl;
import cn.globalph.b2c.order.domain.OrderItemPriceDetail;
import cn.globalph.b2c.order.domain.OrderItemPriceDetailImpl;
import cn.globalph.b2c.order.domain.OrderItemQualifier;
import cn.globalph.b2c.order.domain.OrderItemQualifierImpl;
import cn.globalph.b2c.order.service.type.OrderItemType;
import cn.globalph.common.persistence.EntityConfiguration;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository("blOrderItemDao")
public class OrderItemDaoImpl implements OrderItemDao {

    @PersistenceContext(unitName="blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public OrderItem save(final OrderItem orderItem) {
        return em.merge(orderItem);
    }

    public OrderItem readOrderItemById(final Long orderItemId) {
        return em.find(OrderItemImpl.class, orderItemId);
    }

    public void delete(OrderItem orderItem) {
        if (!em.contains(orderItem)) {
            orderItem = readOrderItemById(orderItem.getId());
           }
        em.remove(orderItem);
        em.flush();
     }

    public OrderItem create(final OrderItemType orderItemType) {
        final OrderItem item = (OrderItem) entityConfiguration.createEntityInstance(orderItemType.getType());
        item.setOrderItemType(orderItemType);
        return item;
    }
    
    public OrderItem saveOrderItem(final OrderItem orderItem) {
        return em.merge(orderItem);
    }

    public OrderItemPriceDetail createOrderItemPriceDetail() {
        return new OrderItemPriceDetailImpl();
    }

    public OrderItemQualifier createOrderItemQualifier() {
        return new OrderItemQualifierImpl();
    }

    public OrderItemPriceDetail initializeOrderItemPriceDetails(OrderItem item) {
        OrderItemPriceDetail detail = createOrderItemPriceDetail();
        detail.setOrderItem(item);
        detail.setQuantity(item.getQuantity());
        detail.setUseSalePrice(item.getIsOnSale());
        item.getOrderItemPriceDetails().add(detail);
        return detail;
    }
	@Override
	public List<OrderItem> readOrderItemsByOrderId(Long orderId) {
		final Query query = em.createNamedQuery("PH_READ_ORDER_ITEMS_BY_ORDER_ID");
		query.setParameter("orderId", orderId);
		try{
			List<OrderItem> orderItems = query.getResultList();
			return orderItems;
		}catch(Exception e){
			
			e.printStackTrace();
		}
	   return null;
	}
}

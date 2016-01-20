package cn.globalph.b2c.order.dao;

import cn.globalph.b2c.delivery.DeliveryType;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderImpl;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.b2c.payment.domain.OrderPayment;
import cn.globalph.b2c.payment.domain.PaymentTransaction;
import cn.globalph.common.dao.BasicEntityDaoImpl;
import cn.globalph.passport.dao.CustomerDao;
import cn.globalph.passport.domain.Customer;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("blOrderDao")
public class OrderDaoImpl extends BasicEntityDaoImpl<Order> implements OrderDao{

    @Resource(name = "blCustomerDao")
    protected CustomerDao customerDao;

    @Resource(name = "blOrderDaoExtensionManager")
    protected OrderDaoExtensionManager extensionManager;

    @Override
    public Order readOrderById(final Long orderId, boolean refresh) {
        Order order = get(orderId);
        if (refresh) {
            em.refresh(order);
        }
        return order;
    }

    @Override
    public Order save(Order order) {
        return em.merge(order);
    }

    @Override
    public void delete(Order salesOrder) {
        if (!em.contains(salesOrder)) {
            salesOrder = get(salesOrder.getId());
        }

        //need to null out the reference to the Order for all the OrderPayments
        //as they are not deleted but Archived.
        for (OrderPayment payment : salesOrder.getPayments()) {
            payment.setOrder(null);
            payment.setArchived('Y');
            for (PaymentTransaction transaction : payment.getTransactions()) {
                transaction.setArchived('Y');
            }
        }

        em.remove(salesOrder);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> readOrdersForCustomer(final Customer customer, final OrderStatus orderStatus) {
        if (orderStatus == null) {
            return readOrdersForCustomer(customer.getId());
        } else {
            final Query query = em.createNamedQuery("BC_READ_ORDERS_BY_CUSTOMER_ID_AND_STATUS");
            query.setParameter("customerId", customer.getId());
            query.setParameter("orderStatus", orderStatus.getType());
            return query.getResultList();
        }
    }
    
    @Override
	public List<Order> readOrdersForCustomer(Customer customer,
			List<OrderStatus> sts, int pageSize, int page) {
        if (sts == null || sts.size() == 0) {
            return null;
        } else {
            final Query query = em.createNamedQuery("BC_READ_ORDERS_BY_CUSTOMER_ID_AND_STS");
            query.setParameter("customerId", customer.getId());
            List<String> orderSts = new ArrayList<String>();
            for(OrderStatus st : sts){
            	orderSts.add(st.getType());
            }
            query.setParameter("orderSts", orderSts);
            int start = pageSize * (page - 1);
            query.setFirstResult(start);
            query.setMaxResults(pageSize);
            return query.getResultList();
        }
	}

	@Override
	public Integer getOrdersForCustomerTotal(Customer customer,
			List<OrderStatus> sts) {
		 if (sts == null || sts.size() == 0) {
	            return new Integer(0);
	        } else {
	            final Query query = em.createNamedQuery("PH_READ_TOTAL_ORDERS_BY_CUSTOMER_ID_AND_STS");
	            query.setParameter("customerId", customer.getId());
	            List<String> orderSts = new ArrayList<String>();
	            for(OrderStatus st : sts){
	            	orderSts.add(st.getType());
	            }
	            query.setParameter("orderSts", orderSts);
	            return (Integer)query.getSingleResult();
	        }
	}

	@Override
    @SuppressWarnings("unchecked")
    public List<Order> readOrdersForCustomer(final Customer customer, final List<OrderStatus> sts) {
        if (sts == null) {
            return readOrdersForCustomer(customer.getId());
        } else {
            final Query query = em.createNamedQuery("BC_READ_ORDERS_BY_CUSTOMER_ID_AND_STS");
            query.setParameter("customerId", customer.getId());
            List<String> orderSts = new ArrayList<String>();
            for(OrderStatus st : sts){
            	orderSts.add(st.getType());
            }
            query.setParameter("orderSts", orderSts);
            return query.getResultList();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> readOrdersForCustomer(final Long customerId) {
        final Query query = em.createNamedQuery("BC_READ_ORDERS_BY_CUSTOMER_ID");
        query.setParameter("customerId", customerId);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> readSuborderList(Long orderId) {
        final Query query = em.createNamedQuery("PH_READ_SUBORDERS_BY_ORDER_ID");
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    @Override
    public Order readCartForCustomer(final Customer customer) {
        Order order = null;
        final Query query = em.createNamedQuery("BC_READ_ORDERS_BY_CUSTOMER_ID_AND_NAME_NULL");
        query.setParameter("customerId", customer.getId());
        query.setParameter("orderStatus", OrderStatus.IN_PROCESS.getType());
//        query.setHint(QueryHints.HINT_CACHEABLE, true);
//        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");
        @SuppressWarnings("rawtypes")
        final List temp = query.getResultList();
        if (temp != null && !temp.isEmpty()) {
            order = (Order) temp.get(0);
        }
        return order;
    }

    @Override
    public Order createNewCartForCustomer(Customer customer) {
        Order order = create();
        if (customer.getLoginName() == null) {
            customer.setLoginName(String.valueOf(customer.getId()));
            customer = customerDao.persist(customer);
        }
        order.setCustomer(customer);
        order.setStatus(OrderStatus.IN_PROCESS);

        if (extensionManager != null) {
            extensionManager.getHandlerProxy().attachAdditionalDataToNewCart(customer, order);
        }
        
        order = persist(order);

        if (extensionManager != null) {
            extensionManager.getHandlerProxy().processPostSaveNewCart(customer, order);
        }

        return order;
    }

    @Override
    public Order submitOrder(final Order cartOrder) {
        cartOrder.setStatus(OrderStatus.CONFIRMED);
        return persist(cartOrder);
    }

    @Override
    public Order create() {
        final Order order = ((Order) entityConfiguration.createEntityInstance("cn.globalph.b2c.order.domain.Order"));

        return order;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Order readNamedOrderForCustomer(final Customer customer, final String name) {
        final Query query = em.createNamedQuery("BC_READ_NAMED_ORDER_FOR_CUSTOMER");
        query.setParameter("customerId", customer.getId());
        query.setParameter("orderStatus", OrderStatus.NAMED.getType());
        query.setParameter("orderName", name);
        List<Order> orders = query.getResultList();
            
        // Apply any additional filters that extension modules have registered
        if (orders != null && !orders.isEmpty() && extensionManager != null) {
            extensionManager.getHandlerProxy().applyAdditionalOrderLookupFilter(customer, name, orders);
           }
        
        return orders == null || orders.isEmpty() ? null : orders.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Order readOrderByOrderNumber(final String orderNumber) {
        if (orderNumber == null || "".equals(orderNumber)) {
            return null;
        }

        final Query query = em.createNamedQuery("BC_READ_ORDER_BY_ORDER_NUMBER");
        query.setParameter("orderNumber", orderNumber);
        List<Order> orders = query.getResultList();
        return orders == null || orders.isEmpty() ? null : orders.get(0);
    }

    @Override
    public Order updateSalePriceToRetailPriceIfNone(Order order) {
        order = em.merge(order);
        if (order.updatePrices()) {
            order = persist(order);
        }
        return order;
    }

	@Override
	public Class<? extends Order> getImplClass() {
		return OrderImpl.class;
	}
	
	@Override
	public List<Order> readNoCommentOrdersForCustomer(Customer customer){
        final Query query = em.createNamedQuery("BC_READ_NO_COMMENT_ORDER_FOR_CUSTOMER");
        query.setParameter("customerId", customer.getId());
        query.setParameter("orderStatus", OrderStatus.COMPLETED.getType());
        List<Order> orders = query.getResultList();
        return orders;
	}
	
	@Override
    public List<Order> readNoCommentOrdersForCustomer(Customer customer, int pageSize, int page){
        final Query query = em.createNamedQuery("BC_READ_NO_COMMENT_ORDER_FOR_CUSTOMER");
        query.setParameter("customerId", customer.getId());
        query.setParameter("orderStatus", OrderStatus.COMPLETED.getType());
        int start = pageSize * (page -1);
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        List<Order> orders = query.getResultList();
        return orders;
	}
    
	@Override
    public Integer readNoCommentOrdersForCustomerTotal(Customer customer){
        final Query query = em.createNamedQuery("PH_READ_NO_COMMENT_ORDER_FOR_CUSTOMER_TOTAL");
        query.setParameter("customerId", customer.getId());
        query.setParameter("orderStatus", OrderStatus.COMPLETED.getType());
        return (Integer)query.getSingleResult();
	}
	
	@Override
	public List<Order> readOrderByDateRange(Date from, Date to, OrderStatus stauts){
		final Query query = em.createNamedQuery("PH_READ_DATE_RANGE_ORDER");
		query.setParameter("fromDate", from);
		query.setParameter("toDate", to);
		query.setParameter("orderStatus", stauts.getType());
		List<Order> orders = query.getResultList();
	    return orders;
	}

    @Override
    public int completeOverdueOrders() {
        //todo 需要判断 is shipped
        String sql = "UPDATE NPH_ORDER SET ORDER_STATUS ='COMPLETED' WHERE SUBMIT_DATE < SUBDATE(NOW(), 3) AND ORDER_STATUS = 'CONFIRMED'";
        final Query update = em.createNativeQuery(sql);
        return update.executeUpdate();
    }
    
    @Override
    public List<Order> findCompleteOverdueOrders() {
        final Query query = em.createNamedQuery("PH_FIND_COMPLETE_OVERDUE_ORDER");
        return query.getResultList();
    }

    @Override
    public int cancelOverdueOrders() {
        String sql = "UPDATE NPH_ORDER SET ORDER_STATUS ='CANCELLED' WHERE SUBMIT_DATE < SUBTIME(NOW(), '00:15:00.000000') AND ORDER_STATUS = 'SUBMITTED'";
        final Query update = em.createNativeQuery(sql);
        return update.executeUpdate();
    }
    
    @Override
    public List<Order> findOverdueOrders() {
        final Query query = em.createNamedQuery("PH_FIND_OVERDUE_ORDER");
        return query.getResultList();
    }


    @Override
    public List<Order> findNotSentDeliveryInfoOrders() {
        final Query query = em.createNamedQuery("PH_FIND_NOT_SENT_DELIVERY_INFO_ORDER");
        return query.getResultList();
    }

    @Override
    public int determineLogisticsOrders(int minCount) {
        final Query update = em.createNativeQuery(determineDeliveryTypeOrderSQL(DeliveryType.LOGISTICS, minCount), OrderImpl.class);
        return update.executeUpdate();
    }

    @Override
    public int determineExpressOrdersWithCommunity(int minCount) {
        final Query update = em.createNativeQuery(determineDeliveryTypeOrderSQL(DeliveryType.EXPRESS, minCount), OrderImpl.class);
        return update.executeUpdate();
    }

    @Override
    public int determineExpressOrdersWithoutCommunity() {
        final Query update = em.createNativeQuery(determineExpressOrdersWithoutCommunitySQL(), OrderImpl.class);
        return update.executeUpdate();
    }

    @Override
    public List<Order> findNotShippedOrders() {
        final Query query = em.createNamedQuery("PH_FIND_NOT_SHIPPED_ORDER");
        return query.getResultList();
    }

    private String determineExpressOrdersWithoutCommunitySQL() {
        return "UPDATE nph_order o " +
                "SET    o.delivery_type = '" + DeliveryType.EXPRESS.getType() + "' " +
                "WHERE  o.order_id IN (SELECT t.order_id " +
                "                    FROM  (SELECT o1.order_id " +
                "                           FROM   nph_order o1 " +
                "                                  LEFT JOIN nph_order_address oa1 " +
                "                                         ON o1.order_id = oa1.order_id " +
                "                           WHERE  oa1.community IS NULL " +
                "                                  AND o1.provider_id IS NOT NULL " +
                "                                  AND o1.is_shipped = false " +
                "                                  AND o1.delivery_type IS NULL " +
                "                                  AND o1.order_status IN ( 'CONFIRMED', 'SUBORDER' )) t); ";
    }

    private String determineDeliveryTypeOrderSQL(DeliveryType deliveryType, int minCount) {
        String sql = "UPDATE nph_order o " +
                "SET    o.delivery_type = '" + deliveryType.getType() + "' " +
                "WHERE  o.order_id IN ( select t.order_id from (" +
                "       SELECT o1.order_id" +
                "                    FROM   nph_order o1 " +
                "                           JOIN nph_order_address oa1 " +
                "                             ON o1.order_id = oa1.order_id " +
                "                           JOIN (SELECT o2.provider_id, oa2.province, " +
                "                                        oa2.city, " +
                "                                        oa2.district, " +
                "                                        oa2.community " +
                "                                 FROM   nph_order o2, " +
                "                                        nph_order_address oa2 " +
                "                                 WHERE  o2.order_id = oa2.order_id " +
                "                                        AND O2.provider_id is not null" +
                "                                        AND OA2.community IS NOT NULL " +
                "                                        AND o2.order_status IN ( " +
                "                                            'CONFIRMED', 'SUBORDER' ) " +
                "                                        AND O2.is_shipped = false " +
                "                                        AND O2.delivery_type IS NULL " +
                "                                 GROUP  BY o2.provider_id, oa2.province, " +
                "                                           oa2.city, " +
                "                                           oa2.district, " +
                "                                           oa2.community ";
        if (DeliveryType.LOGISTICS.equals(deliveryType)) {
            sql += "HAVING COUNT(1) >= " + minCount;
        } else {
            sql += "HAVING COUNT(1) < " + minCount;
        }
        sql += ") ov " +
                "                             ON o1.provider_id = ov.provider_id AND oa1.province = ov.province " +
                "                                AND oa1.city = ov.city " +
                "                                AND oa1.district = ov.district " +
                "                                AND oa1.community = ov.community " +
                "                    WHERE  oa1.community IS NOT NULL " +
                "                           AND o1.provider_id is not null" +
                "                           AND o1.is_shipped = false " +
                "                           AND o1.delivery_type IS NULL " +
                "                           AND o1.order_status IN ( 'CONFIRMED', 'SUBORDER' )) t)";
        return sql;
    }

	@Override
	public int getSkuCountInOrders(Long skuId, Long customerId) {
        final Query query = em.createNamedQuery("PH_COUNT_CUSTOMER_SKU");
        query.setParameter("customerId", customerId);
        query.setParameter("skuId", skuId);
        Long count =  (Long)query.getSingleResult();
        return count == null ? 0 : count.intValue();
	}
}

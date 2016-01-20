package cn.globalph.b2c.order.dao;

import java.util.Date;
import java.util.List;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.common.dao.BasicEntityDao;
import cn.globalph.passport.domain.Customer;

public interface OrderDao extends BasicEntityDao<Order> {

    Order readOrderById(Long orderId, boolean refresh);

    List<Order> readOrdersForCustomer(Customer customer, OrderStatus orderStatus);
    
    List<Order> readOrdersForCustomer(Customer customer, List<OrderStatus> orderSts);
    
    List<Order> readOrdersForCustomer(Customer customer, List<OrderStatus> orderSts, int pageSize, int page);
    
    Integer getOrdersForCustomerTotal(Customer customer, List<OrderStatus> orderSts);

    List<Order> readOrdersForCustomer(Long id);

    List<Order> readSuborderList(Long orderId);

    Order readNamedOrderForCustomer(Customer customer, String name);

    Order readCartForCustomer(Customer customer);

    Order submitOrder(Order cartOrder);

    Order create();

    Order createNewCartForCustomer(Customer customer);

    Order readOrderByOrderNumber(String orderNumber);
    
    Order updateSalePriceToRetailPriceIfNone(Order order);
    
    List<Order> readNoCommentOrdersForCustomer(Customer customer);
    
    List<Order> readNoCommentOrdersForCustomer(Customer customer, int pageSize, int page);
   
    Integer readNoCommentOrdersForCustomerTotal(Customer customer);

    List<Order> readOrderByDateRange(Date from, Date to, OrderStatus stauts);

    int completeOverdueOrders();

    int cancelOverdueOrders();
    
    List<Order> findOverdueOrders();
    
    List<Order> findCompleteOverdueOrders();

    List<Order> findNotShippedOrders();

    int determineLogisticsOrders(int minCount);

    int determineExpressOrdersWithCommunity(int minCount);

    int determineExpressOrdersWithoutCommunity();

    List<Order> findNotSentDeliveryInfoOrders();

    Order save(Order order);
    
    int getSkuCountInOrders(Long skuId, Long customerId);
}

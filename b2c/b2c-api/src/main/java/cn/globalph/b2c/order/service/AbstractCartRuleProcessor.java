package cn.globalph.b2c.order.service;

import cn.globalph.b2c.order.dao.OrderDao;
import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.common.rule.AbstractRuleProcessor;
import cn.globalph.common.structure.dto.ItemCriteriaDTO;
import cn.globalph.passport.domain.Customer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public abstract class AbstractCartRuleProcessor<T> extends AbstractRuleProcessor<T> {

    protected OrderDao orderDao;

    protected Order lookupOrderForCustomer(Customer c) {
        Order o = null;
        if (c != null) {
            o = orderDao.readCartForCustomer(c);
        }

        return o;
    }

    protected boolean checkItemCriteria(ItemCriteriaDTO itemCriteria, List<OrderItem> orderItems) {
        Map<String,Object> vars = new HashMap<String, Object>();
        int foundCount = 0;
        Iterator<OrderItem> items = orderItems.iterator();
        while (foundCount < itemCriteria.getQty() && items.hasNext()) {
            OrderItem currentItem = items.next();
            vars.put("discreteOrderItem", currentItem);
            vars.put("orderItem", currentItem);
            boolean match = executeExpression(itemCriteria.getMatchRule(), vars);

            if (match) {
                foundCount = foundCount + currentItem.getQuantity();
            }
        }
        return (foundCount >= itemCriteria.getQty().intValue());
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

}

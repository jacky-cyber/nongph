package cn.globalph.b2c.util.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderImpl;
import cn.globalph.b2c.order.service.type.OrderStatus;
import cn.globalph.passport.domain.Customer;
import cn.globalph.passport.domain.CustomerImpl;

import org.springframework.stereotype.Repository;

/**
 * @author Jeff Fischer
 */
@Repository("blResourcePurgeDao")
public class ResourcePurgeDaoImpl implements ResourcePurgeDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Override
    public List<Order> findCarts(String[] names, OrderStatus[] statuses, Date dateCreatedMinThreshold, Boolean isPreview) {
        TypedQuery<Order> query = buildCartQuery(names, statuses, dateCreatedMinThreshold, isPreview, Order.class);
        return query.getResultList();
    }

    @Override
    public List<Order> findCarts(String[] names, OrderStatus[] statuses, Date dateCreatedMinThreshold, Boolean isPreview, int startPos, int length) {
        TypedQuery<Order> query = buildCartQuery(names, statuses, dateCreatedMinThreshold, isPreview, Order.class);
        query.setFirstResult(startPos);
        query.setMaxResults(length);
        return query.getResultList();
    }

    @Override
    public Long findCartsCount(String[] names, OrderStatus[] statuses, Date dateCreatedMinThreshold, Boolean isPreview) {
        TypedQuery<Long> query = buildCartQuery(names, statuses, dateCreatedMinThreshold, isPreview, Long.class);
        return query.getSingleResult();
    }

    @Override
    public List<Customer> findCustomers(Date dateCreatedMinThreshold, Boolean registered, Boolean deactivated, Boolean isPreview) {
        TypedQuery<Customer> query = buildCustomerQuery(dateCreatedMinThreshold, registered, deactivated, isPreview, Customer.class);
        return query.getResultList();
    }

    @Override
    public List<Customer> findCustomers(Date dateCreatedMinThreshold, Boolean registered, Boolean deactivated, Boolean isPreview, int startPos, int length) {
        TypedQuery<Customer> query = buildCustomerQuery(dateCreatedMinThreshold, registered, deactivated, isPreview,
                Customer.class);
        query.setFirstResult(startPos);
        query.setMaxResults(length);
        return query.getResultList();
    }

    @Override
    public Long findCustomersCount(Date dateCreatedMinThreshold, Boolean registered, Boolean deactivated, Boolean isPreview) {
        TypedQuery<Long> query = buildCustomerQuery(dateCreatedMinThreshold, registered, deactivated, isPreview, Long.class);
        return query.getSingleResult();
    }

    protected <T> TypedQuery<T> buildCustomerQuery(Date dateCreatedMinThreshold, Boolean registered, Boolean deactivated, Boolean isPreview, Class<T> returnType) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(returnType);
        Root<CustomerImpl> root = criteria.from(CustomerImpl.class);
        if (Long.class.equals(returnType)) {
            criteria.select((Selection<? extends T>) builder.count(root));
        } else {
            criteria.select((Selection<? extends T>) root);
        }

        //find only customers that do not have any orders, otherwise a purge would fail because of referential integrity
        Subquery<Long> subquery = criteria.subquery(Long.class);
        Root orderRoot = subquery.from(OrderImpl.class);
        subquery.select(builder.count(orderRoot));
        subquery.where(builder.equal(orderRoot.get("customer"),root));

        List<Predicate> restrictions = new ArrayList<Predicate>();
        restrictions.add(builder.equal(subquery, 0L));
        if (registered != null) {
            if (registered) {
                restrictions.add(builder.isTrue(root.get("registered").as(Boolean.class)));
            } else {
                restrictions.add(builder.or(builder.isNull(root.get("registered")),
                        builder.isFalse(root.get("registered").as(Boolean.class))));
            }
        }
        if (deactivated != null) {
            if (deactivated) {
                restrictions.add(builder.isTrue(root.get("deactivated").as(Boolean.class)));
            } else {
                restrictions.add(builder.or(builder.isNull(root.get("deactivated")),
                        builder.isFalse(root.get("deactivated").as(Boolean.class))));
            }
        }
        if (dateCreatedMinThreshold != null) {
            restrictions.add(builder.lessThan(root.get("auditable").get("dateCreated").as(Date.class), dateCreatedMinThreshold));
        }
        if (isPreview != null) {
            if (isPreview) {
                restrictions.add(builder.isTrue(root.get("previewable").get("isPreview").as(Boolean.class)));
            } else {
                restrictions.add(builder.or(builder.isNull(root.get("previewable").get("isPreview")),
                        builder.isFalse(root.get("previewable").get("isPreview").as(Boolean.class))));
            }
        }
        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
        return em.createQuery(criteria);
    }

    protected <T> TypedQuery<T> buildCartQuery(String[] names, OrderStatus[] statuses, Date dateCreatedMinThreshold, Boolean isPreview, Class<T> returnType) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(returnType);
        Root<OrderImpl> root = criteria.from(OrderImpl.class);
        if (Long.class.equals(returnType)) {
            criteria.select((Selection<? extends T>) builder.count(root));
        } else {
            criteria.select((Selection<? extends T>) root);
        }
        List<Predicate> restrictions = new ArrayList<Predicate>();
        List<String> statusList = new ArrayList<String>();
        if (statuses != null) {
            for (OrderStatus status : statuses) {
                statusList.add(status.getType());
            }
        } else {
            statusList.add("IN_PROCESS");
        }
        restrictions.add(root.get("status").in(statusList));
        if (names != null) {
            restrictions.add(root.get("name").in(Arrays.asList(names)));
        }
        if (dateCreatedMinThreshold != null) {
            restrictions.add(builder.lessThan(root.get("auditable").get("dateCreated").as(Date.class), dateCreatedMinThreshold));
        }
        if (isPreview != null) {
            if (isPreview) {
                restrictions.add(builder.isTrue(root.get("previewable").get("isPreview").as(Boolean.class)));
            } else {
                restrictions.add(builder.or(builder.isNull(root.get("previewable").get("isPreview")),
                        builder.isFalse(root.get("previewable").get("isPreview").as(Boolean.class))));
            }
        }
        criteria.where(restrictions.toArray(new Predicate[restrictions.size()]));
        return em.createQuery(criteria);
    }
}

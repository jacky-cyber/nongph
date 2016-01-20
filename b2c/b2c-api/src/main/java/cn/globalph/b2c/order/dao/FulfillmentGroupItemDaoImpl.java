package cn.globalph.b2c.order.dao;

import cn.globalph.b2c.order.domain.FulfillmentGroup;
import cn.globalph.b2c.order.domain.FulfillmentGroupItem;
import cn.globalph.b2c.order.domain.FulfillmentGroupItemImpl;
import cn.globalph.common.persistence.EntityConfiguration;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Repository("blFulfillmentGroupItemDao")
public class FulfillmentGroupItemDaoImpl implements FulfillmentGroupItemDao {

    @PersistenceContext(unitName="blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    public void delete(FulfillmentGroupItem fulfillmentGroupItem) {
        if (!em.contains(fulfillmentGroupItem)) {
            fulfillmentGroupItem = readFulfillmentGroupItemById(fulfillmentGroupItem.getId());
        }
        em.remove(fulfillmentGroupItem);
    }

    public FulfillmentGroupItem save(final FulfillmentGroupItem fulfillmentGroupItem) {
        return em.merge(fulfillmentGroupItem);
    }

    public FulfillmentGroupItem readFulfillmentGroupItemById(final Long fulfillmentGroupItemId) {
        return (FulfillmentGroupItem) em.find(FulfillmentGroupItemImpl.class, fulfillmentGroupItemId);
    }

    @SuppressWarnings("unchecked")
    public List<FulfillmentGroupItem> readFulfillmentGroupItemsForFulfillmentGroup(final FulfillmentGroup fulfillmentGroup) {
        final Query query = em.createNamedQuery("BC_READ_FULFILLMENT_GROUP_ITEM_BY_FULFILLMENT_GROUP_ID");
        query.setParameter("fulfillmentGroupId", fulfillmentGroup.getId());
        return query.getResultList();
    }

    public FulfillmentGroupItem create() {
        return ((FulfillmentGroupItem) entityConfiguration.createEntityInstance("cn.globalph.b2c.order.domain.FulfillmentGroupItem"));
    }
}

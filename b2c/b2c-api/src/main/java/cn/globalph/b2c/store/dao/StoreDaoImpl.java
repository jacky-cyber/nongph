package cn.globalph.b2c.store.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import cn.globalph.b2c.store.domain.Store;
import cn.globalph.b2c.store.domain.StoreImpl;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

@Repository("blStoreDao")
public class StoreDaoImpl implements StoreDao {

    @PersistenceContext(unitName = "blPU")
    private EntityManager em;

    public Store readStoreById(Long id) {
        return em.find(StoreImpl.class, id);
    }

    @SuppressWarnings("unchecked")
    public Store readStoreByStoreName(final String storeName) {
        Query query = em.createNamedQuery("BC_FIND_STORE_BY_STORE_NAME");
        query.setParameter("storeName", storeName);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        List result = query.getResultList();
        return (result.size() > 0) ? (Store) result.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    public Store readStoreByStoreCode(final String storeCode) {
        Query query = em.createNamedQuery("BC_FIND_STORE_BY_STORE_NAME");
        query.setParameter("storeName", storeCode.toUpperCase());
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        List result = query.getResultList();
        return (result.size() > 0) ? (Store) result.get(0) : null;
    }

    @SuppressWarnings("unchecked")
    public List<Store> readAllStores() {
        Query query = em.createNamedQuery("BC_FIND_ALL_STORES");
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Store> readAllStoresByState(final String state) {
        Query query = em.createNamedQuery("BC_FIND_ALL_STORES_BY_STATE");
        query.setParameter("state", state);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }
}

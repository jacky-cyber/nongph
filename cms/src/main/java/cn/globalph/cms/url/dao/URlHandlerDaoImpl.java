package cn.globalph.cms.url.dao;

import cn.globalph.cms.url.domain.URLHandler;
import cn.globalph.cms.url.domain.URLHandlerImpl;
import cn.globalph.common.persistence.EntityConfiguration;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by ppatel.
 */
@Repository("blURLHandlerDao")
public class URlHandlerDaoImpl implements URLHandlerDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public URLHandler findURLHandlerByURI(String uri) {
        TypedQuery<URLHandler> query = em.createNamedQuery("BC_READ_OUTGOING_URL", URLHandler.class);
        query.setParameter("incomingURL", uri);
        query.setHint(QueryHints.HINT_CACHEABLE, true);

        List<URLHandler> results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        } else {
            return null;
        }
    }

    @Override
    public URLHandler findURLHandlerById(Long id) {
        return em.find(URLHandlerImpl.class, id);
    }
    
    @Override
    public List<URLHandler> findAllURLHandlers() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<URLHandler> criteria = builder.createQuery(URLHandler.class);
        Root<URLHandlerImpl> handler = criteria.from(URLHandlerImpl.class);
        criteria.select(handler);
        TypedQuery<URLHandler> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<URLHandler>();
        }
    }
    
    public URLHandler saveURLHandler(URLHandler handler) {
        return em.merge(handler);
    }

}

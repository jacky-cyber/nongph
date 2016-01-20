package cn.globalph.b2c.search.dao;

import org.springframework.stereotype.Repository;

import cn.globalph.b2c.search.domain.SearchIntercept;
import cn.globalph.b2c.search.redirect.dao.SearchRedirectDaoImpl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

/**
 * @deprecated Replaced in functionality by {@link SearchRedirectDaoImpl}
 */
@Repository("blSearchInterceptDao")
@Deprecated
public class SearchInterceptDaoImpl implements SearchInterceptDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Override
    public SearchIntercept findInterceptByTerm(String term) {
        Query query = em.createNamedQuery("BC_READ_SEARCH_INTERCEPT_BY_TERM");
        query.setParameter("searchTerm", term);
        SearchIntercept result;
        try {
            result = (SearchIntercept) query.getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SearchIntercept> findAllIntercepts() {
        Query query = em.createNamedQuery("BC_READ_ALL_SEARCH_INTERCEPTS");
        List<SearchIntercept> result;
        try {
            result = query.getResultList();
        } catch (NoResultException e) {
            result = null;
        }

        return result;
        
    }

    @Override
    public void createIntercept(SearchIntercept intercept) {
        em.persist(intercept);
    }

    @Override
    public void deleteIntercept(SearchIntercept intercept) {
        em.remove(intercept);
    }

    @Override
    public void updateIntercept(SearchIntercept intercept) {
        em.merge(intercept);
    }

}

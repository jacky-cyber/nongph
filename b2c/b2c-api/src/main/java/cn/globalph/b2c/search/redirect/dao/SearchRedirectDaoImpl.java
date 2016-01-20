package cn.globalph.b2c.search.redirect.dao;

import cn.globalph.b2c.search.redirect.domain.SearchRedirect;
import cn.globalph.common.time.SystemTime;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by ppatel.
 */
@Repository("blSearchRedirectDao")
public class SearchRedirectDaoImpl implements SearchRedirectDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    protected Long currentDateResolution = 10000L;
    protected Date cachedDate = SystemTime.asDate();

    protected Date getCurrentDateAfterFactoringInDateResolution() {
        Date returnDate = SystemTime.getCurrentDateWithinTimeResolution(cachedDate, currentDateResolution);
        if (returnDate != cachedDate) {
            if (SystemTime.shouldCacheDate()) {
                cachedDate = returnDate;
            }
        }
        return returnDate;
    }

    @Override
    public SearchRedirect findSearchRedirectBySearchTerm(String searchTerm) {
        Query query;
        query = em.createNamedQuery("BC_READ_SEARCH_URL");
        query.setParameter("searchTerm", searchTerm);
        query.setParameter("now", getCurrentDateAfterFactoringInDateResolution());
        query.setMaxResults(1);
        query.setHint(QueryHints.HINT_CACHEABLE, true);

        List<SearchRedirect> results = query.getResultList();
        if (results != null && !results.isEmpty()) {
            return results.get(0);
        } else {
            return null;
        }
    }

    public Long getCurrentDateResolution() {
        return currentDateResolution;
    }

    public void setCurrentDateResolution(Long currentDateResolution) {
        this.currentDateResolution = currentDateResolution;
    }

}

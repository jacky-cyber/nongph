package cn.globalph.logistics.dao;

import cn.globalph.common.dao.BasicEntityDaoImpl;
import cn.globalph.logistics.ph.Express;
import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author steven
 * @since 7/24/15
 */
@Repository("phExpressDao")
public class ExpressDaoImpl extends BasicEntityDaoImpl<Express> implements ExpressDao {
    @Override
    public Express save(Express express) {
        return em.merge(express);
    }

    @Override
    public Express create() {
        return (Express) entityConfiguration.createEntityInstance(Express.class.getName());
    }

    @Override
    public Express readByExpressNo(String expressNo) {
        TypedQuery<Express> query = em.createNamedQuery("PH_READ_EXPRESS_BY_EXPRESS_NO", Express.class);
        query.setParameter("expressNo", expressNo);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");

        return query.getResultList().isEmpty() ? null : query.getResultList().get(0);
    }

    @Override
    public Express readByOrderNo(String orderNo) {
        TypedQuery<Express> query = em.createNamedQuery("PH_READ_EXPRESS_BY_ORDER_NO", Express.class);
        query.setParameter("orderNo", orderNo);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        query.setHint(QueryHints.HINT_CACHE_REGION, "query.Order");

        return query.getResultList().isEmpty() ? null : query.getResultList().get(0);
    }

    @Override
    public Class<? extends Express> getImplClass() {
        return Express.class;
    }
}

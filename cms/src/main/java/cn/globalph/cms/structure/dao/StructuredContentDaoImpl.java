package cn.globalph.cms.structure.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cn.globalph.cms.structure.domain.StructuredContent;
import cn.globalph.cms.structure.domain.StructuredContentImpl;
import cn.globalph.cms.structure.domain.StructuredContentType;
import cn.globalph.cms.structure.domain.StructuredContentTypeImpl;
import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.sandbox.domain.SandBoxImpl;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * Created by bpolster.
 */
@Repository("blStructuredContentDao")
public class StructuredContentDaoImpl implements StructuredContentDao {

    private static SandBox DUMMY_SANDBOX = new SandBoxImpl();
    {
        DUMMY_SANDBOX.setId(-1l);
    }

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public StructuredContent findStructuredContentById(Long contentId) {
        return em.find(StructuredContentImpl.class, contentId);
    }

    @Override
    public StructuredContentType findStructuredContentTypeById(Long contentTypeId) {
        return em.find(StructuredContentTypeImpl.class, contentTypeId);
    }

    @Override
    public List<StructuredContentType> retrieveAllStructuredContentTypes() {
        Query query = em.createNamedQuery("BC_READ_ALL_STRUCTURED_CONTENT_TYPES");
        return query.getResultList();
    }
    
    @Override
    public List<StructuredContent> findAllContentItems() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<StructuredContent> criteria = builder.createQuery(StructuredContent.class);
        Root<StructuredContentImpl> sc = criteria.from(StructuredContentImpl.class);

        criteria.select(sc);

        try {
            return em.createQuery(criteria).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<StructuredContent>();
        }
    }

    @Override
    public StructuredContent addOrUpdateContentItem(StructuredContent content) {
        return em.merge(content);
    }

    @Override
    public void delete(StructuredContent content) {
        if (! em.contains(content)) {
            content = findStructuredContentById(content.getId());
        }
        em.remove(content);
    }
    
    @Override
    public StructuredContentType saveStructuredContentType(StructuredContentType type) {
        return em.merge(type);
    }

    @Override
    public List<StructuredContent> findActiveStructuredContentByType(StructuredContentType type) {
        String queryName = "BC_ACTIVE_STRUCTURED_CONTENT_BY_TYPE";

        Query query = em.createNamedQuery(queryName);
        query.setParameter("contentType", type);

        return query.getResultList();
    }

    @Override
    public List<StructuredContent> findActiveStructuredContentByNameAndType(StructuredContentType type, String name) {
       
        final Query query = em.createNamedQuery("BC_ACTIVE_STRUCTURED_CONTENT_BY_TYPE_AND_NAME");
        query.setParameter("contentType", type);
        query.setParameter("contentName", name);

        return query.getResultList();
    }

    @Override
    public List<StructuredContent> findActiveStructuredContentByName(String name) {
        String queryName = "BC_ACTIVE_STRUCTURED_CONTENT_BY_NAME";
  
        Query query = em.createNamedQuery(queryName);
        query.setParameter("contentName", name);

        return query.getResultList();
    }

    @Override
    public StructuredContentType findStructuredContentTypeByName(String name) {
        Query query = em.createNamedQuery("BC_READ_STRUCTURED_CONTENT_TYPE_BY_NAME");
        query.setParameter("name",name);
        query.setHint(QueryHints.HINT_CACHEABLE, true);

        List<StructuredContentType> results = query.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void detach(StructuredContent sc) {
        em.detach(sc);
    }
}

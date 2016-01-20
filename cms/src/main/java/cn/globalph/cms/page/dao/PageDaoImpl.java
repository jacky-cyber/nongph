package cn.globalph.cms.page.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import cn.globalph.cms.page.domain.Page;
import cn.globalph.cms.page.domain.PageImpl;
import cn.globalph.cms.page.domain.PageTemplate;
import cn.globalph.cms.page.domain.PageTemplateImpl;
import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.sandbox.domain.SandBoxImpl;

import org.springframework.stereotype.Repository;

/**
 * Created by bpolster.
 */
@Repository("blPageDao")
public class PageDaoImpl implements PageDao {

    private static SandBox DUMMY_SANDBOX = new SandBoxImpl();
    {
        DUMMY_SANDBOX.setId(-1l);
    }

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name="blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public Page readPageById(Long id) {
        return em.find(PageImpl.class, id);
    }

    @Override
    public PageTemplate readPageTemplateById(Long id) {
        return em.find(PageTemplateImpl.class, id);
    }
    

    @Override
    public PageTemplate savePageTemplate(PageTemplate template) {
        return em.merge(template);
    }

    @Override
    public Page updatePage(Page page) {
        return em.merge(page);
    }

    @Override
    public void delete(Page page) {
        if (!em.contains(page)) {
            page = readPageById(page.getId());
        }
        em.remove(page);
    }

    @Override
    public Page addPage(Page clonedPage) {
        return em.merge(clonedPage);
    }

    @Override
    public List<Page> findPageByURI(String uri) {
        Query query;
        query = em.createNamedQuery("BC_READ_PAGE_BY_URI");
        query.setParameter("uri", uri);
        return query.getResultList();
    }
    
    @Override
    public List<Page> readAllPages() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Page> criteria = builder.createQuery(Page.class);
        Root<PageImpl> page = criteria.from(PageImpl.class);

        criteria.select(page);

        try {
            return em.createQuery(criteria).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<Page>();
        }
    }
    
    @Override
    public List<Page> readOnlineAndIncludedPages(int limit, int offset, String sortBy) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Page> criteria = builder.createQuery(Page.class);
        Root<PageImpl> page = criteria.from(PageImpl.class);
        criteria.select(page);
        criteria.where(builder.and(
                builder.or(builder.isFalse(page.get("offlineFlag").as(Boolean.class)), builder.isNull(page.get("offlineFlag").as(Boolean.class))),
                builder.or(builder.isFalse(page.get("excludeFromSiteMap").as(Boolean.class)), builder.isNull(page.get("excludeFromSiteMap").as(Boolean.class)))));
        criteria.orderBy(builder.asc(page.get(sortBy)));
        TypedQuery<Page> query = em.createQuery(criteria);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public List<PageTemplate> readAllPageTemplates() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<PageTemplate> criteria = builder.createQuery(PageTemplate.class);
        Root<PageTemplateImpl> template = criteria.from(PageTemplateImpl.class);

        criteria.select(template);

        try {
            return em.createQuery(criteria).getResultList();
        } catch (NoResultException e) {
            return new ArrayList<PageTemplate>();
        }
    }

    @Override
    public void detachPage(Page page) {
        em.detach(page);
    }

}

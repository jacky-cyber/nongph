package cn.globalph.common.site.dao;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.common.site.domain.Catalog;
import cn.globalph.common.site.domain.CatalogImpl;
import cn.globalph.common.site.domain.Site;
import cn.globalph.common.site.domain.SiteImpl;
import cn.globalph.common.site.service.type.SiteResolutionType;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository("blSiteDao")
public class SiteDaoImpl implements SiteDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Override
    public Site create() {
        return (Site) entityConfiguration.createEntityInstance(Site.class.getName());
    }

    @Override
    public Site retrieve(Long id) {
        return em.find(SiteImpl.class, id);
    }
    
    @Override
    public Catalog retrieveCatalog(Long id) {
        return em.find(CatalogImpl.class, id);
    }
    
    @Override
    public List<Site> readAllActiveSites() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Site> criteria = builder.createQuery(Site.class);
        Root<SiteImpl> site = criteria.from(SiteImpl.class);
        criteria.select(site);
        criteria.where(
            builder.and(
                builder.or(builder.isNull(site.get("archiveStatus").get("archived").as(String.class)),
                    builder.notEqual(site.get("archiveStatus").get("archived").as(Character.class), 'Y')),
                builder.or(builder.isNull(site.get("deactivated").as(Boolean.class)),
                    builder.notEqual(site.get("deactivated").as(Boolean.class), true))
            )
        );
        
        TypedQuery<Site> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        
        return query.getResultList();
    }

    @Override
    public Site retrieveSiteByDomainOrDomainPrefix(String domain, String domainPrefix) {
        if (domain == null) {
            return null;
        }

        List<String> siteIdentifiers = new ArrayList<String>();
        siteIdentifiers.add(domain);
        siteIdentifiers.add(domainPrefix);

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Site> criteria = builder.createQuery(Site.class);
        Root<SiteImpl> site = criteria.from(SiteImpl.class);
        criteria.select(site);

        criteria.where(site.get("siteIdentifierValue").as(String.class).in(siteIdentifiers));
        TypedQuery<Site> query = em.createQuery(criteria);
        query.setHint(QueryHints.HINT_CACHEABLE, true);

        List<Site> results = query.getResultList();
        
        for (Site currentSite : results) {
            if (SiteResolutionType.DOMAIN.equals(currentSite.getSiteResolutionType())) {
                if (domain.equals(currentSite.getSiteIdentifierValue())) {
                    return currentSite;
                }
            }

            if (SiteResolutionType.DOMAIN_PREFIX.equals(currentSite.getSiteResolutionType())) {
                if (domainPrefix.equals(currentSite.getSiteIdentifierValue())) {
                    return currentSite;
                }
            }
            
            // We need to forcefully load this collection.
            currentSite.getCatalogs().size();
        }

        return null;
    }

    @Override
    public Site save(Site site) {
        return em.merge(site);
    }

    @Override
    public Site retrieveDefaultSite() {
        return null;
    }
}

package cn.globalph.common.site.dao;

import cn.globalph.common.site.domain.Catalog;
import cn.globalph.common.site.domain.Site;

import java.util.List;

public interface SiteDao {

    /**
     * Creates an instance of Site based on the class matching the bean id of 
     * "cn.globalph.common.site.domain.Site"
     * 
     * @return
     */
    public Site create();

    /**
     * Finds a site by its id.
     * @param id
     * @return
     */
    public Site retrieve(Long id);

    /**
     * Finds a site by its domain or domain prefix.
     * @param domain
     * @param prefix
     * @return
     */
    public Site retrieveSiteByDomainOrDomainPrefix(String domain, String prefix);

    /**
     * Persists the site changes.
     * @param site
     * @return
     */
    public Site save(Site site);

    /**
     * Returns a default site.   This method returns null in the out of box implementation of Broadleaf.
     * Extend for implementation specific behavior. 
     * 
     * @return
     */
    public Site retrieveDefaultSite();

    /**
     * @return a List of all sites in the system
     */
    public List<Site> readAllActiveSites();

    /**
     * Finds a catalog by its id.
     * 
     * @param id
     * @return the catalog
     */
    public Catalog retrieveCatalog(Long id);
}

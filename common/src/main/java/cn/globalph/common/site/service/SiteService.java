package cn.globalph.common.site.service;

import cn.globalph.common.site.dao.SiteDao;
import cn.globalph.common.site.dao.SiteDaoImpl;
import cn.globalph.common.site.domain.Catalog;
import cn.globalph.common.site.domain.Site;

import java.util.List;

public interface SiteService {

    /**
     * Creates an instance of Site.   Default implementation delegates to {@link SiteDao#create()}.
     * 
     * @return
     */
    public Site createSite();

    /**
     * Find a site by its id.
     * @param id
     * @return
     */
    public Site retrieveSiteById(Long id);

    /**
     * Find a site by its domain
     * @param id
     * @return
     */
    public Site retrieveSiteByDomainName(String domain);

    /**
     * Save updates to a site.
     * @param id
     * @return
     */
    public Site save(Site site);

    /**
     * Returns the default site.  
     * 
     * @see {@link SiteDaoImpl}
     * 
     * @param id
     * @return
     */
    public Site retrieveDefaultSite();

    /**
     * @return a List of all sites in the system
     */
    public List<Site> findAllActiveSites();

    /**
     * Finds a catalog by its id.
     * 
     * @param id
     * @return the catalog
     */
    public Catalog findCatalogById(Long id);

}

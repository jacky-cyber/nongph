package cn.globalph.common.sitemap.domain;

import java.util.List;

/**
 * CustomSiteMapGenerator is controlled by this configuration.
 * 
 * @author bpolster
 */
public interface CustomUrlSiteMapGeneratorConfiguration extends SiteMapGeneratorConfiguration {

    /**
     * Returns a list of custom SiteMapURLEntrys.
     * 
     * @return
     */
    public List<SiteMapUrlEntry> getCustomURLEntries();

    /**
     * Sets a list of custom SiteMapURLEntrys.
     * 
     * @param customURLEntries
     */
    public void setCustomURLEntries(List<SiteMapUrlEntry> customURLEntries);

}

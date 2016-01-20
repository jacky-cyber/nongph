package cn.globalph.common.sitemap.domain;

import cn.globalph.common.sitemap.service.type.SiteMapChangeFreqType;
import cn.globalph.common.sitemap.service.type.SiteMapPriorityType;

import java.io.Serializable;
import java.util.Date;

/**
 * Sample URL entry
 */
public interface SiteMapUrlEntry extends Serializable {

    /**
     * Returns the SiteMapURLEntry Id.
     * 
     * @return
     */
    public Long getId();

    /**
     * Sets the SiteMapURLEntry Id.
     * 
     * @param id
     */
    public void setId(Long id);

    /**
     * Returns the URL location.
     * 
     * @return
     */
    public String getLocation();
    
    /**
     * Sets the URL location.
     * 
     * @param location
     */
    public void setLocation(String location);
    
    /**
     * Returns the last modified date.
     * 
     * @return
     */
    public Date getLastMod();
    
    /**
     * Sets the last modified date.
     * 
     * @param date
     */
    public void setLastMod(Date date);

    /**
     * Returns the SiteMapChangeFreqType.
     * 
     * @return
     */
    public SiteMapChangeFreqType getSiteMapChangeFreq();
    
    /**
     * Sets the SiteMapChangeFreqType.
     * 
     * @param siteMapChangeFreq
     */
    public void setSiteMapChangeFreq(SiteMapChangeFreqType siteMapChangeFreq);
    
    /**
     * Returns the SiteMapPriority.
     * 
     * @return
     */
    public SiteMapPriorityType getSiteMapPriority();

    /**
     * Sets the SiteMapPriority.  Must be a two digit value between 0.0 and 1.0.
     * 
     * @param siteMapPriority
     */
    public void setSiteMapPriority(SiteMapPriorityType siteMapPriority);

    /**
     * Returns the SiteMapGeneratorConfiguration.
     * 
     * @return
     */
    public CustomUrlSiteMapGeneratorConfiguration getCustomUrlSiteMapGeneratorConfiguration();

    /**
     * Sets the SiteMapGeneratorConfiguration.
     * 
     * 
     * @param siteMapGeneratorConfiguration
     */
    public void setCustomUrlSiteMapGeneratorConfiguration(CustomUrlSiteMapGeneratorConfiguration customUrlSiteMapGeneratorConfiguration);

}

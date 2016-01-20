package cn.globalph.common.sitemap.domain;

import cn.globalph.common.sitemap.service.type.SiteMapChangeFreqType;
import cn.globalph.common.sitemap.service.type.SiteMapGeneratorType;
import cn.globalph.common.sitemap.service.type.SiteMapPriorityType;

import java.io.Serializable;

/**
 * Sample URL tag generated and controlled by this configuration.
 */
public interface SiteMapGeneratorConfiguration extends Serializable {
    
    /**
     * Returns the SiteMapGeneratorConfiguration Id.
     * 
     * @return
     */
    public Long getId();

    /**
     * Sets the SiteMapGeneratorConfiguration Id.
     * 
     * @param id
     */
    public void setId(Long id);

    /**
     * Returns the "disabled" boolean.
     * 
     * @return
     */
    public Boolean isDisabled();

    /**
     * Sets the "disabled" boolean.
     * 
     * @param disabled
     */
    public void setDisabled(Boolean disabled);

    /**
     * Returns the list of SiteMapChangeFreqTypes.
     * 
     * @return
     */
    public SiteMapChangeFreqType getSiteMapChangeFreq();

    /**
     * Sets the list of SiteMapChangeFreqTypes.
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
     * Returns the list of SiteMapGeneratorTypes.
     * 
     * @return
     */
    public SiteMapGeneratorType getSiteMapGeneratorType();
    
    /**
     * Sets the list of SiteMapGeneratorTypes.
     * 
     * @param siteMapGeneratorType
     */
    public void setSiteMapGeneratorType(SiteMapGeneratorType siteMapGeneratorType);

    /**
     * Returns the SiteMapConfiguration.
     * 
     * @return
     */
    public SiteMapConfiguration getSiteMapConfiguration();

    /**
     * Sets the SiteMapConfiguration.
     * 
     * @param siteMapConfiguration
     */
    public void setSiteMapConfiguration(SiteMapConfiguration siteMapConfiguration);

}

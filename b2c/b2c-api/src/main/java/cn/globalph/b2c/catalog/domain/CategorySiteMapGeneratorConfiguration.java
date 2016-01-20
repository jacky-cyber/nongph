package cn.globalph.b2c.catalog.domain;

import cn.globalph.common.sitemap.domain.SiteMapGeneratorConfiguration;

/**
 * 	 is controlled by this configuration.
 * 
 */
public interface CategorySiteMapGeneratorConfiguration extends SiteMapGeneratorConfiguration {

    /**
     * Returns the root category.
     * 
     * @return
     */
    public Category getRootCategory();

    /**
     * Sets the root category.
     * 
     * @param rootCategory
     */
    public void setRootCategory(Category rootCategory);

    /**
     * Returns the starting depth.
     * 
     * @return
     */
    public int getStartingDepth();

    /**
     * Sets the starting depth.
     * 
     * @param startingDepth
     */
    public void setStartingDepth(int startingDepth);

    /**
     * Returns the ending depth.
     * 
     * @return
     */
    public int getEndingDepth();

    /**
     * Sets the ending depth.
     * 
     * @param endingDepth
     */
    public void setEndingDepth(int endingDepth);

}

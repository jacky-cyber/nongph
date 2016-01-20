package cn.globalph.common.site.domain;

import java.io.Serializable;
import java.util.List;

import cn.globalph.common.site.service.type.SiteResolutionType;

public interface Site extends Serializable {

    /**
     * Unique/internal id for a site.
     * @return
     */
    public Long getId();

    /**
     * Sets the internal id for a site.
     * @param id
     */
    public void setId(Long id);

    /**
     * The display name for a site.
     * @return
     */
    public String getName();

    /**
     * Sets the displayName for a site.
     * @param name
     */
    public void setName(String name);
    
    /**
     * Used along with {@link #getSiteResolutionType()} to determine the current
     * Site for a given request.
     *
     * @return
     */
    public String getSiteIdentifierValue();

    /**
     *
     * @param siteIdentifierValue
     */
    public void setSiteIdentifierValue(String siteIdentifierValue);
    
    /**
     * Intended to be used along with the #getSiteIdentifierValue()
     * by an implementation of SiteResolver to determine 
     * if this is the current site.   
     *
     * @return
     */
    public SiteResolutionType getSiteResolutionType();

    /** 
     * Sets the site resolution type.
     * @see #getSiteResolutionType()
     * @param siteResolutionType
     */
    public void setSiteResolutionType(SiteResolutionType siteResolutionType);

    /**
     * Retrieve a list of product, category and offer groupings that
     * this site has access to
     *
     * @return a list of catalog groupings
     */
    public List<Catalog> getCatalogs();

    /**
     * Set the list of product, category and offer groupings that
     * this site has access to
     *
     * @param catalogs a list of catalog groupings
     */
    public void setCatalogs(List<Catalog> catalogs);

    /**
     * Retrieve an deep copy of this site. Not bound by
     * entity manager scope.
     *
     * @return a deep copy of this site
     */
    public Site clone();

    public boolean isDeactivated();

    public void setDeactivated(boolean deactivated);
}

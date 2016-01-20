package cn.globalph.common.sitemap.domain;

import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationCollection;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * CustomSiteMapGenerator is controlled by this configuration.
 * 
 */
@Entity
@Table(name = "NPH_CUST_SITE_MAP_GEN_CFG")
@AdminPresentationClass(friendlyName = "CustomUrlSiteMapGeneratorConfigurationImpl")
public class CustomUrlSiteMapGeneratorConfigurationImpl extends SiteMapGeneratorConfigurationImpl implements CustomUrlSiteMapGeneratorConfiguration {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "customUrlSiteMapGeneratorConfiguration", targetEntity = SiteMapUrlEntryImpl.class, cascade = { CascadeType.ALL }, orphanRemoval = true)
    @AdminPresentationCollection(friendlyName = "CustomUrlSiteMapGeneratorConfigurationImpl_Custom_URL_Entries")
    protected List<SiteMapUrlEntry> customURLEntries = new ArrayList<SiteMapUrlEntry>();

    @Override
    public List<SiteMapUrlEntry> getCustomURLEntries() {
        return customURLEntries;
    }

    @Override
    public void setCustomURLEntries(List<SiteMapUrlEntry> customURLEntries) {
        this.customURLEntries = customURLEntries;
    }

}

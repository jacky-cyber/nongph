package cn.globalph.b2c.catalog.domain;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationToOneLookup;
import cn.globalph.common.sitemap.domain.SiteMapGeneratorConfigurationImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * CategorySiteMapGenerator is controlled by this configuration.
 */
@Entity
@Table(name = "NPH_CAT_SITE_MAP_GEN_CFG")
@AdminPresentationClass(friendlyName = "CategorySiteMapGeneratorConfigurationImpl")
public class CategorySiteMapGeneratorConfigurationImpl extends SiteMapGeneratorConfigurationImpl implements CategorySiteMapGeneratorConfiguration {

    private static final long serialVersionUID = 1L;

    @ManyToOne(targetEntity = CategoryImpl.class)
    @JoinColumn(name = "ROOT_CATEGORY_ID", nullable = false)
    @AdminPresentation(friendlyName = "CategorySiteMapGeneratorConfigurationImpl_Root_Category")
    @AdminPresentationToOneLookup()
    protected Category rootCategory;

    @Column(name = "STARTING_DEPTH", nullable = false)
    @AdminPresentation(friendlyName = "CategorySiteMapGeneratorConfigurationImpl_Starting_Depth")
    protected int startingDepth = 1;

    @Column(name = "ENDING_DEPTH", nullable = false)
    @AdminPresentation(friendlyName = "CategorySiteMapGeneratorConfigurationImpl_Ending_Depth")
    protected int endingDepth = 1;

    @Override
    public Category getRootCategory() {
        return rootCategory;
    }

    @Override
    public void setRootCategory(Category rootCategory) {
        this.rootCategory = rootCategory;
    }

    @Override
    public int getStartingDepth() {
        return startingDepth;
    }

    @Override
    public void setStartingDepth(int startingDepth) {
        this.startingDepth = startingDepth;
    }

    @Override
    public int getEndingDepth() {
        return endingDepth;
    }

    @Override
    public void setEndingDepth(int endingDepth) {
        this.endingDepth = endingDepth;
    }
}
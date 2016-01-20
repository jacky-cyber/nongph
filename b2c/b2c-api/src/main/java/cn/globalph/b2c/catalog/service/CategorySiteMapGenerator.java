package cn.globalph.b2c.catalog.service;

import cn.globalph.b2c.catalog.dao.CategoryDao;
import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.CategorySiteMapGeneratorConfiguration;
import cn.globalph.common.sitemap.domain.SiteMapGeneratorConfiguration;
import cn.globalph.common.sitemap.service.SiteMapBuilder;
import cn.globalph.common.sitemap.service.SiteMapGenerator;
import cn.globalph.common.sitemap.service.type.SiteMapGeneratorType;
import cn.globalph.common.sitemap.wrapper.SiteMapURLWrapper;
import cn.globalph.common.storage.service.GlobalphFileUtils;

import org.hibernate.tool.hbm2x.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 * Responsible for generating site map entries for Category.
 * 
 * @author Joshua Skorton (jskorton)
 */
@Component("blCategorySiteMapGenerator")
public class CategorySiteMapGenerator implements SiteMapGenerator {

    @Resource(name = "blCategoryDao")
    protected CategoryDao categoryDao;

    @Value("${category.site.map.generator.row.limit}")
    protected int rowLimit;

    @Override
    public boolean canHandleSiteMapConfiguration(SiteMapGeneratorConfiguration siteMapGeneratorConfiguration) {
        return SiteMapGeneratorType.CATEGORY.equals(siteMapGeneratorConfiguration.getSiteMapGeneratorType());
    }

    @Override
    public void addSiteMapEntries(SiteMapGeneratorConfiguration smgc, SiteMapBuilder siteMapBuilder) {

        CategorySiteMapGeneratorConfiguration categorySMGC = (CategorySiteMapGeneratorConfiguration) smgc;

        addCategorySiteMapEntries(categorySMGC.getRootCategory(), 1, categorySMGC, siteMapBuilder);
        
    }

    protected void addCategorySiteMapEntries(Category parentCategory, int currentDepth, CategorySiteMapGeneratorConfiguration categorySMGC, SiteMapBuilder siteMapBuilder) {
        
        int rowOffset = 0;
        List<Category> categories;
        
        do {
            categories = categoryDao.readActiveSubCategoriesByCategory(parentCategory, rowLimit, rowOffset);
            rowOffset += categories.size();
            for (Category category : categories) {
                if (StringUtils.isEmpty(category.getUrl())) {
                    continue;
                }

                if (currentDepth < categorySMGC.getEndingDepth()) {
                    addCategorySiteMapEntries(category, currentDepth + 1, categorySMGC, siteMapBuilder);
                }

                if (currentDepth < categorySMGC.getStartingDepth()) {
                    continue;
                }
                
                SiteMapURLWrapper siteMapUrl = new SiteMapURLWrapper();

                // location
                siteMapUrl.setLoc(generateUri(siteMapBuilder, category));

                // change frequency
                siteMapUrl.setChangeFreqType(categorySMGC.getSiteMapChangeFreq());

                // priority
                siteMapUrl.setPriorityType(categorySMGC.getSiteMapPriority());

                // lastModDate
                siteMapUrl.setLastModDate(generateDate(category));

                siteMapBuilder.addUrl(siteMapUrl);
            }
        } while (categories.size() == rowLimit);

    }

    protected String generateUri(SiteMapBuilder siteMapBuilder, Category category) {
        return GlobalphFileUtils.appendUnixPaths(siteMapBuilder.getBaseUrl(), category.getUrl());
    }

    protected Date generateDate(Category category) {
        return new Date();
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public int getRowLimit() {
        return rowLimit;
    }

    public void setRowLimit(int rowLimit) {
        this.rowLimit = rowLimit;
    }

}

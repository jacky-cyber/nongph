package cn.globalph.b2c.catalog.domain.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.CategoryAttribute;
import cn.globalph.b2c.catalog.domain.wrap.CategoryAttributeWrap;
import cn.globalph.b2c.catalog.domain.wrap.CategoryWrap;
import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.wrap.ProductWrap;
import cn.globalph.b2c.product.domain.wrapper.ProductWrapper;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.b2c.search.domain.SkuSearchResult;
import cn.globalph.b2c.search.service.SearchService;
import cn.globalph.common.domain.wrapper.APIWrapper;

/**
 *  This is a JAXB wrapper for a Category.  There may be several reasons to extend this class.
 *  First, you may want to extend Broadleaf's CategroryImpl and expose those extensions via a RESTful
 *  service.  You may also want to suppress properties that are being serialized.  To expose new properties, or
 *  suppress properties that have been exposed, do the following:<br/>
 *  <br/>
 *  1. Extend this class   <br/>
 *  2. Override the <code>wrap</code> method. <br/>
 *  3. Within the wrap method, either override all properties that you want to set, or call <code>super.wrap(Category)</code>   <br/>
 *  4. Set additional property values that you have added.  <br/>
 *  5. Set super properties to null if you do not want them serialized. (e.g. <code>super.name = null;</code>  <br/>
 */
public class CategoryWrapper implements APIWrapper<Category, CategoryWrap> {
	
	@Resource(name="blSearchService")
	private SearchService searchService;
	
	private CategoryAttributeWrapper categoryAttributeWrapper;
	
	private ProductWrapper productWrapper; 
	
	@Resource(name="blCatalogService")
	private CatalogService catalogService;
	
	public CategoryWrap wrapDetails(Category category){
		return wrapDetails( category, null, null, null, null );
	}
	
    public CategoryWrap wrapDetails(Category category, 
    		                        Integer productLimit, Integer productOffset, 
    		                        Integer subcategoryLimit, Integer subcategoryOffset) {
    	CategoryWrap wrap = new CategoryWrap();
    	wrap.setId( category.getId() );
    	wrap.setName( category.getName() );
    	wrap.setDescription( category.getDescription() );
    	wrap.setActive( category.isActive() );
    	wrap.setActiveEndDate( category.getActiveEndDate() );
    	wrap.setUrl( category.getUrl() );
    	wrap.setUrlKey( category.getUrlKey() );

        if (category.getCategoryAttributes() != null && !category.getCategoryAttributes().isEmpty()) {
            List<CategoryAttributeWrap>categoryAttributes = new ArrayList<CategoryAttributeWrap>();
            for (CategoryAttribute attribute : category.getCategoryAttributes()) {
                categoryAttributes.add( categoryAttributeWrapper.wrapSummary(attribute) );
            }
            wrap.setCategoryAttributes( categoryAttributes );
        }

        if (productLimit != null && productOffset == null) {
            productOffset = 1;
        }

        if (subcategoryLimit != null && subcategoryOffset == null) {
            subcategoryOffset = 1;
        }

        if (productLimit != null && productOffset != null) {
            SkuSearchCriteria searchCriteria = new SkuSearchCriteria();
            searchCriteria.setPage(productOffset);
            searchCriteria.setPageSize(productLimit);
            searchCriteria.setFilterCriteria(new HashMap<String, String[]>());
            SkuSearchResult result = searchService.findSkusByCategory(category, searchCriteria);
            List<Sku> skuList = result.getSkus();
            if (skuList != null && !skuList.isEmpty()) {
                if (wrap.getProducts() == null) {
                	wrap.setProducts( new ArrayList<ProductWrap>() );
                }

                for (Sku s : skuList) {
                    wrap.getProducts().add( productWrapper.wrapSummary(s.getProduct() ) );
                }
            }
        }

        if (subcategoryLimit != null && subcategoryOffset != null) {
            wrap.setSubcategories( buildSubcategoryTree(category, subcategoryLimit, subcategoryOffset) );
        }
        return wrap;
    }

    @Override
    public CategoryWrap wrapSummary(Category category) {
    	CategoryWrap wrap = new CategoryWrap();
    	wrap.setId( category.getId() );
    	wrap.setName( category.getName() );
    	wrap.setDescription( category.getDescription() );
    	wrap.setActive( category.isActive() );
    	return wrap;
    }

    private List<CategoryWrap> buildSubcategoryTree(Category parent, Integer subcategoryLimit, Integer subcategoryOffset) {
    	
    	List<CategoryWrap> wrapList = new ArrayList<CategoryWrap>(); 
        List<Category> subcategories = catalogService.findActiveSubCategoriesByCategory(parent, subcategoryLimit, subcategoryOffset);

        for (Category c : subcategories) {
            wrapList.add( wrapSummary(c) );
        }

        return wrapList;
    }
}
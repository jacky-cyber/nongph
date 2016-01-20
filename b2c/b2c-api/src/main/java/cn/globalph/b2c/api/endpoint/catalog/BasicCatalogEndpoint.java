package cn.globalph.b2c.api.endpoint.catalog;

import org.apache.commons.lang.StringUtils;

import cn.globalph.b2c.api.WebServicesException;
import cn.globalph.b2c.api.endpoint.BaseEndpoint;
import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.CategoryAttribute;
import cn.globalph.b2c.catalog.domain.wrap.CategoriesWrap;
import cn.globalph.b2c.catalog.domain.wrap.CategoryAttributeWrap;
import cn.globalph.b2c.catalog.domain.wrap.CategoryWrap;
import cn.globalph.b2c.catalog.domain.wrapper.CategoriesWrapper;
import cn.globalph.b2c.catalog.domain.wrapper.CategoryAttributeWrapper;
import cn.globalph.b2c.catalog.domain.wrapper.CategoryWrapper;
import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.inventory.service.InventoryService;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.wrap.MediaWrap;
import cn.globalph.b2c.product.domain.wrap.SkuWrap;
import cn.globalph.b2c.product.domain.wrapper.MediaWrapper;
import cn.globalph.b2c.product.domain.wrapper.SkuWrapper;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.b2c.search.domain.SkuSearchResult;
import cn.globalph.b2c.search.domain.wrapper.SearchResultsWrapper;
import cn.globalph.b2c.search.service.SearchFacetDTOService;
import cn.globalph.b2c.search.service.SearchService;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.media.domain.Media;
import cn.globalph.common.security.service.ExploitProtectionService;
import cn.globalph.common.storage.service.StaticAssetPathService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

public abstract class BasicCatalogEndpoint extends BaseEndpoint {

    @Resource(name="blCatalogService")
    private CatalogService catalogService;

    @Resource(name = "blSearchService")
    private SearchService searchService;

    @Resource(name = "blSearchFacetDTOService")
    protected SearchFacetDTOService facetService;

    @Resource(name = "blExploitProtectionService")
    private ExploitProtectionService exploitProtectionService;

    @Resource(name = "blStaticAssetPathService")
    private StaticAssetPathService staticAssetPathService;
    
    @Resource(name = "blInventoryService")
    private InventoryService inventoryService;
    
    protected SearchResultsWrapper searchResultsWrapper;
    
    private CategoriesWrapper categoriesWrapper;
    
    private CategoryWrapper categoryWrapper;
    
    private CategoryAttributeWrapper categoryAttributeWrapper; 
    
    private MediaWrapper mediaWrapper;
    
    private SkuWrapper skuWrapper;
    /**
     * This uses Broadleaf's search service to search for products within a category.
     * @param request
     * @param q
     * @param categoryId
     * @param pageSize
     * @param page
     * @return
     */
    public SkuSearchResult findProductsByCategoryAndQuery( SkuSearchCriteria searchCriteria,
    														 Long categoryId,
            												 String q, 
            												 Integer page, 
            												 Integer pageSize ) {
        try {
            if (StringUtils.isNotEmpty(q)) {
                q = StringUtils.trim(q);
                q = exploitProtectionService.cleanString(q);
            } else {
                throw WebServicesException.build(Response.Status.BAD_REQUEST.getStatusCode())
                        .addMessage(WebServicesException.SEARCH_QUERY_EMPTY);
            }
        } catch (ServiceException e) {
            throw WebServicesException.build(Response.Status.BAD_REQUEST.getStatusCode())
                    .addMessage(WebServicesException.SEARCH_QUERY_MALFORMED, q);
        }

        if (categoryId == null) {
            throw WebServicesException.build(Response.Status.BAD_REQUEST.getStatusCode())
                    .addMessage(WebServicesException.INVALID_CATEGORY_ID, categoryId);
        }

        Category category = null;
        category = catalogService.findCategoryById(categoryId);
        if (category == null) {
            throw WebServicesException.build(Response.Status.BAD_REQUEST.getStatusCode())
                    .addMessage(WebServicesException.INVALID_CATEGORY_ID, categoryId);
        }
       
        try {
            return searchService.findProductsByCategoryAndQuery(category, q, searchCriteria);
        } catch (ServiceException e) {
            throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }
    
     /*
    public SkuWrapper findDefaultSkuByProductId(HttpServletRequest request, Long id) {
        Product product = catalogService.findProductById(id);
        if (product != null && product.getDefaultSku() != null) {
            SkuWrapper wrapper = (SkuWrapper)context.getBean(SkuWrapper.class.getName());
            wrapper.wrapDetails(product.getDefaultSku(), request);
            return wrapper;
           }
        throw BroadleafWebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(BroadleafWebServicesException.PRODUCT_NOT_FOUND, id);
    }
    */ 
    public CategoriesWrap findAllCategories( String name,
            								 int limit,
            								 int offset) {
        List<Category> categories;
        if (name != null) {
            categories = catalogService.findCategoriesByName(name, limit, offset);
        } else {
            categories = catalogService.findAllCategories(limit, offset);
        }
        return categoriesWrapper.wrapDetails(categories);
    }

    public CategoriesWrap findSubCategories( Long id,
            int limit,
            int offset,
            boolean active) {
        Category category = catalogService.findCategoryById(id);
        if (category != null) {
            List<Category> categories;
            if (active) {
                categories = catalogService.findActiveSubCategoriesByCategory(category, limit, offset);
            } else {
                categories = catalogService.findAllSubCategories(category, limit, offset);
            }
            return categoriesWrapper.wrapDetails(categories);
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CATEGORY_NOT_FOUND, id);

    }

    public CategoriesWrap findActiveSubCategories(Long id, int limit, int offset) {
        return findSubCategories(id, limit, offset, true);
    }

    public CategoryWrap findCategoryById( Long id,
								          int productLimit,
								             int productOffset,
								             int subcategoryLimit,
								             int subcategoryOffset ) {
        Category cat = catalogService.findCategoryById(id);
        if (cat != null) {
            return categoryWrapper.wrapDetails(cat, productLimit, productOffset, subcategoryLimit, subcategoryOffset);
        }
        throw WebServicesException.build( Response.Status.NOT_FOUND.getStatusCode() )
                .addMessage(WebServicesException.CATEGORY_NOT_FOUND, id);
    }

    /**
     * Allows you to search for a category by ID or by name.
     * @param request
     * @param searchParameter
     * @param productLimit
     * @param productOffset
     * @param subcategoryLimit
     * @param subcategoryOffset
     * @return
     */
    public CategoryWrap findCategoryByIdOrNameOrUrl(String searchParameter,
            int productLimit,
            int productOffset,
            int subcategoryLimit,
            int subcategoryOffset) {

        Category cat = null;

        if (searchParameter != null) {
            try {
                cat = catalogService.findCategoryById(Long.parseLong(searchParameter));
            } catch (NumberFormatException e) {
            	if(searchParameter.startsWith("/")){
            		cat = catalogService.findCategoryByURI(searchParameter);
            	}else{
	                List<Category> categories = catalogService.findCategoriesByName(searchParameter);
	                if (categories != null && !categories.isEmpty()) {
	                    cat = categories.get(0);
	                }
            	}
            }
        }
        if (cat != null) {
            return categoryWrapper.wrapDetails(cat, productLimit, productOffset, subcategoryLimit, subcategoryOffset);
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CATEGORY_NOT_FOUND, searchParameter);
    }

    public List<CategoryAttributeWrap> findCategoryAttributesForCategory(Long id) {
        Category category = catalogService.findCategoryById(id);
        if (category != null) {
            ArrayList<CategoryAttributeWrap> out = new ArrayList<CategoryAttributeWrap>();
            if (category.getCategoryAttributes() != null) {
                for (CategoryAttribute attribute : category.getCategoryAttributes()) {
                    out.add( categoryAttributeWrapper.wrapSummary(attribute) );
                }
            }
            return out;
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.CATEGORY_NOT_FOUND, id);
    }

    public List<MediaWrap> findMediaForCategory(Long id) {
        Category category = catalogService.findCategoryById(id);
        if (category != null) {
            ArrayList<MediaWrap> out = new ArrayList<MediaWrap>();
            Map<String, Media> media = category.getCategoryMedia();
            for (Media med : media.values()) {
                out.add( mediaWrapper.wrapSummary(med) );
            }
            return out;
        }
        throw WebServicesException.build( Response.Status.NOT_FOUND.getStatusCode() ).addMessage(WebServicesException.CATEGORY_NOT_FOUND, id);
    }
    
    public List<SkuWrap> findCategorySkus( SkuSearchCriteria searchCriteria, Long categoryId ) {
    	 Category category = catalogService.findCategoryById(categoryId);
         if (category != null) {
        	 try{
	        	 List<Sku> skus = searchService.findSkusByCategory(category, searchCriteria).getSkus();
	        	 List<SkuWrap> result = new ArrayList<SkuWrap>();
	        	 for( Sku sku : skus ) {
	        		 result.add( skuWrapper.wrapSummary( sku ) );
	        	 }
	        	 return result;
        	 }catch(Exception e){
        		 throw WebServicesException.build( 500, e );
        	 }
         } else {
        	 throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode()).addMessage(WebServicesException.CATEGORY_NOT_FOUND, categoryId);
         }
    }
}
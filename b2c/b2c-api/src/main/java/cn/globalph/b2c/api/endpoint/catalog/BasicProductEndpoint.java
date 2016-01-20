package cn.globalph.b2c.api.endpoint.catalog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import cn.globalph.b2c.api.WebServicesException;
import cn.globalph.b2c.api.endpoint.BaseEndpoint;
import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.catalog.domain.CategoryProductXref;
import cn.globalph.b2c.catalog.domain.RelatedProduct;
import cn.globalph.b2c.catalog.domain.wrap.CategoriesWrap;
import cn.globalph.b2c.catalog.domain.wrapper.CategoriesWrapper;
import cn.globalph.b2c.catalog.service.CatalogService;
import cn.globalph.b2c.inventory.domain.wrap.InventoryWrap;
import cn.globalph.b2c.inventory.domain.wrapper.InventoryWrapper;
import cn.globalph.b2c.inventory.service.InventoryService;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.ProductAttribute;
import cn.globalph.b2c.product.domain.Sku;
import cn.globalph.b2c.product.domain.SkuAttribute;
import cn.globalph.b2c.product.domain.wrap.MediaWrap;
import cn.globalph.b2c.product.domain.wrap.ProductAttributeWrap;
import cn.globalph.b2c.product.domain.wrap.ProductWrap;
import cn.globalph.b2c.product.domain.wrap.RelatedProductWrap;
import cn.globalph.b2c.product.domain.wrap.SkuAttributeWrap;
import cn.globalph.b2c.product.domain.wrap.SkuWrap;
import cn.globalph.b2c.product.domain.wrapper.MediaWrapper;
import cn.globalph.b2c.product.domain.wrapper.ProductAttributeWrapper;
import cn.globalph.b2c.product.domain.wrapper.ProductWrapper;
import cn.globalph.b2c.product.domain.wrapper.RelatedProductWrapper;
import cn.globalph.b2c.product.domain.wrapper.SkuAttributeWrapper;
import cn.globalph.b2c.product.domain.wrapper.SkuWrapper;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.b2c.search.domain.SkuSearchResult;
import cn.globalph.b2c.search.domain.wrapper.SearchResultsWrapper;
import cn.globalph.b2c.search.service.SearchService;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.media.domain.Media;
import cn.globalph.common.security.service.ExploitProtectionService;
import cn.globalph.common.storage.service.StaticAssetPathService;

public class BasicProductEndpoint extends BaseEndpoint{
	@Resource(name="blCatalogService")
    private CatalogService catalogService;
	
	@Resource(name = "blSearchService")
    private SearchService searchService;
	
	@Resource(name = "blExploitProtectionService")
    private ExploitProtectionService exploitProtectionService;
	
	@Resource(name = "blStaticAssetPathService")
    private StaticAssetPathService staticAssetPathService;
	
	@Resource(name = "blInventoryService")
    private InventoryService inventoryService;
	
	private ProductWrapper productWrapper;
	
	private SkuWrapper skuWrapper;
	
	protected SearchResultsWrapper searchResultsWrapper;
	
	private RelatedProductWrapper relatedProductWrapper;
	
	private CategoriesWrapper categoriesWrapper;
	
	private MediaWrapper mediaWrapper;
	
	private InventoryWrapper inventoryWrapper;
	
	private ProductAttributeWrapper productAttributeWrapper;
	
	private SkuAttributeWrapper skuAttributeWrapper;
	
    /**
     * Search for {@code Product} by product id
     *
     * @param id the product id
     * @return the product instance with the given product id
     */
    public ProductWrap findProductById(Long id) {
        Product product = catalogService.findProductById(id);
        if (product != null) {
            return productWrapper.wrapDetails(product);
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.PRODUCT_NOT_FOUND, id);
    }
    
    /**
     * Queries the products. The parameter q, which represents the query, is required. It can be any 
     * string, but is typically a name or keyword, similar to a search engine search.
     * @param request
     * @param q
     * @param pageSize
     * @param page
     * @return
     */
    public SkuSearchResult findProductsByQuery( SkuSearchCriteria searchCriteria,
									            String q,
									            Integer pageSize,
									            Integer page) {
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

        
        try {
            return searchService.findSkusByQuery(q, searchCriteria);
        } catch (ServiceException e) {
            throw WebServicesException.build(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .addMessage(WebServicesException.SEARCH_ERROR);
        }
    }
    
    /**
     * Search for {@code Sku} instances for a given product
     *
     * @param id
     * @return the list of sku instances for the product
     */
    public List<SkuWrap> findSkusByProductById(Long productId) {
        Product product = catalogService.findProductById(productId);
        if (product != null) {
            List<Sku> skus = product.getAllSkus();
            List<SkuWrap> out = new ArrayList<SkuWrap>();
            if (skus != null) {
                for (Sku sku : skus) {
                    out.add(skuWrapper.wrapSummary(sku));
                }
                return out;
            }
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.PRODUCT_NOT_FOUND, productId);
    }
    
    public List<RelatedProductWrap> findUpSaleProductsByProduct( Long id,
            													 int limit,
            													 int offset ) {
        Product product = catalogService.findProductById(id);
        if (product != null) {
            List<RelatedProductWrap> out = new ArrayList<RelatedProductWrap>();

            //TODO: Write a service method that accepts offset and limit
            List<RelatedProduct> relatedProds = product.getUpSaleProducts();
            if (relatedProds != null) {
                for (RelatedProduct prod : relatedProds) {
                    out.add(relatedProductWrapper.wrapSummary(prod));
                }
            }
            return out;
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.PRODUCT_NOT_FOUND, id);
    }

    public List<RelatedProductWrap> findCrossSaleProductsByProduct( Long id,
            														int limit,
            													    int offset ) {
        Product product = catalogService.findProductById(id);
        if (product != null) {
            List<RelatedProductWrap> out = new ArrayList<RelatedProductWrap>();

            //TODO: Write a service method that accepts offset and limit
            List<RelatedProduct> xSellProds = product.getCrossSaleProducts();
            if (xSellProds != null) {
                for (RelatedProduct prod : xSellProds) {
                    out.add(relatedProductWrapper.wrapSummary(prod));
                }
            }
            return out;
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.PRODUCT_NOT_FOUND, id);
    }
    
    public List<ProductAttributeWrap> findProductAttributesForProduct(Long id) {
        Product product = catalogService.findProductById(id);
        if (product != null) {
            List<ProductAttributeWrap> out = new ArrayList<ProductAttributeWrap>();
            if (product.getProductAttributes() != null) {
                for (Map.Entry<String, ProductAttribute> entry : product.getProductAttributes().entrySet()) {
                    out.add( productAttributeWrapper.wrapSummary(entry.getValue()));
                }
            }
            return out;
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.PRODUCT_NOT_FOUND, id);
    }

    public List<SkuAttributeWrap> findSkuAttributesForSku(Long id) {
        Sku sku = catalogService.findSkuById(id);
        if (sku != null) {
            ArrayList<SkuAttributeWrap> out = new ArrayList<SkuAttributeWrap>();
            if (sku.getSkuAttributes() != null) {
                for (Map.Entry<String, SkuAttribute> entry : sku.getSkuAttributes().entrySet()) {
                    out.add( skuAttributeWrapper.wrapSummary(entry.getValue()));
                }
            }
            return out;
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.SKU_NOT_FOUND, id);
    }

    public List<MediaWrap> findMediaForSku(Long id) {
        Sku sku = catalogService.findSkuById(id);
        if (sku != null) {
            List<MediaWrap> medias = new ArrayList<MediaWrap>();
            if (sku.getSkuMedia() != null && ! sku.getSkuMedia().isEmpty()) {
                for (Media media : sku.getSkuMedia().values()) {
                	MediaWrap wrap = mediaWrapper.wrapSummary(media);
                    if (wrap.isAllowOverrideUrl()){
                    	wrap.setUrl(staticAssetPathService.convertAssetPath(media.getUrl(), "", false));
                    }
                    medias.add(wrap);
                }
            }
            return medias;
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.SKU_NOT_FOUND, id);
    }

    public SkuWrap findSkuById(Long id) {
        Sku sku = catalogService.findSkuById(id);
        if (sku != null) {
            return skuWrapper.wrapDetails(sku);
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.SKU_NOT_FOUND, id);
    }
    
    public List<InventoryWrap> findInventoryForSkus(List<Long> ids) {
        List<Sku> skus = catalogService.findSkusByIds(ids);
        if (CollectionUtils.isNotEmpty(skus)) {
            Map<Sku, Integer> quantities = inventoryService.retrieveQuantitiesAvailable(new HashSet<Sku>(skus));
            List<InventoryWrap> out = new ArrayList<InventoryWrap>();
            for (Map.Entry<Sku, Integer> entry : quantities.entrySet()) {
                out.add( inventoryWrapper.wrapSummary(entry.getKey(), entry.getValue()));
            }
            return out;
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.SKU_NOT_FOUND, skus.toArray());
    }

    public List<MediaWrap> findMediaForProduct(Long id) {
        Product product = catalogService.findProductById(id);
        if (product != null) {
            ArrayList<MediaWrap> out = new ArrayList<MediaWrap>();
            Map<String, Media> media = product.getAllSkuMedia();
            if (media != null) {
                for (Media med : media.values()) {
                	MediaWrap wrap = mediaWrapper.wrapSummary(med);
                    if (wrap.isAllowOverrideUrl()){
                    	wrap.setUrl(staticAssetPathService.convertAssetPath(med.getUrl(), "", false));
                    }
                    out.add(wrap);
                }
            }
            return out;
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.PRODUCT_NOT_FOUND, id);
    }
    
    public CategoriesWrap findParentCategoriesForProduct( Long id ) {
        Product product = catalogService.findProductById(id);
        if (product != null) {
            List<Category> categories = new ArrayList<Category>();
            for (CategoryProductXref categoryXref : product.getAllParentCategoryXrefs()) {
                categories.add(categoryXref.getCategory());
            }
            return categoriesWrapper.wrapDetails(categories);
        }
        throw WebServicesException.build(Response.Status.NOT_FOUND.getStatusCode())
                .addMessage(WebServicesException.PRODUCT_NOT_FOUND, id);
    }

}

package cn.globalph.b2c.api.rest.endpoint.catalog;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import cn.globalph.b2c.api.endpoint.catalog.BasicProductEndpoint;
import cn.globalph.b2c.catalog.domain.wrap.CategoriesWrap;
import cn.globalph.b2c.inventory.domain.wrap.InventoryWrap;
import cn.globalph.b2c.product.domain.wrap.MediaWrap;
import cn.globalph.b2c.product.domain.wrap.ProductAttributeWrap;
import cn.globalph.b2c.product.domain.wrap.ProductWrap;
import cn.globalph.b2c.product.domain.wrap.RelatedProductWrap;
import cn.globalph.b2c.product.domain.wrap.SkuAttributeWrap;
import cn.globalph.b2c.product.domain.wrap.SkuWrap;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.b2c.search.domain.SkuSearchResult;
import cn.globalph.b2c.search.domain.wrap.SearchResultsWrap;
import cn.globalph.b2c.search.service.SearchFacetDTOService;

@Component
@Path("/product/")
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class ProductEndpoint extends BasicProductEndpoint{
	@Resource(name = "blSearchFacetDTOService")
    protected SearchFacetDTOService facetService;
	
	@GET
	@Path("{id}")
	public ProductWrap findProductById(@Context HttpServletRequest request, @PathParam("id") Long id) {
	    return super.findProductById(id);
	}
	
	@GET
	@Path("search")
	public SearchResultsWrap findProductsByQuery(@Context HttpServletRequest request,
	        @QueryParam("q") String q,
	        @QueryParam("pageSize") @DefaultValue("15") Integer pageSize,
	        @QueryParam("page") @DefaultValue("1") Integer page) {
		SkuSearchCriteria searchCriteria = facetService.buildSearchCriteria(request);
		SkuSearchResult result = super.findProductsByQuery(searchCriteria, q, pageSize, page);
		facetService.setActiveFacetResults(result.getFacets(), request);
		return searchResultsWrapper.wrapDetails(result);
	}
	
    @GET
    @Path("{id}/skus")
    public List<SkuWrap> findSkusByProductById(@Context HttpServletRequest request, 
    		                                      @PathParam("id") Long id) {
        return super.findSkusByProductById(id);
    }
    
    @GET
    @Path("{id}/related-products/upsale")
    public List<RelatedProductWrap> findUpSaleProductsByProduct(@Context HttpServletRequest request,
            @PathParam("id") Long id,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset) {
        return super.findUpSaleProductsByProduct(id, limit, offset);
    }

    @GET
    @Path("{id}/related-products/crosssale")
    public List<RelatedProductWrap> findCrossSaleProductsByProduct(@Context HttpServletRequest request,
            @PathParam("id") Long id,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset) {
        return super.findCrossSaleProductsByProduct(id, limit, offset);
    }

    @GET
    @Path("{id}/product-attributes")
    public List<ProductAttributeWrap> findProductAttributesForProduct(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findProductAttributesForProduct(id);
    }

    @GET
    @Path("sku/{id}/sku-attributes")
    public List<SkuAttributeWrap> findSkuAttributesForSku(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findSkuAttributesForSku(id);
    }

    @GET
    @Path("sku/{id}/media")
    public List<MediaWrap> findMediaForSku(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findMediaForSku(id);
    }

    @GET
    @Path("sku/{id}")
    public SkuWrap findSkuById(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findSkuById(id);
    }
    
    @GET
    @Path("sku/inventory")
    public List<InventoryWrap> findInventoryForSkus(@Context HttpServletRequest request,
            @QueryParam("id") List<Long> ids) {
        return super.findInventoryForSkus(ids);
    }

    @GET
    @Path("{id}/media")
    public List<MediaWrap> findMediaForProduct(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findMediaForProduct(id);
    }
    
    @GET
    @Path("{id}/categories")
    public CategoriesWrap findParentCategoriesForProduct(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findParentCategoriesForProduct(id);
    }
}
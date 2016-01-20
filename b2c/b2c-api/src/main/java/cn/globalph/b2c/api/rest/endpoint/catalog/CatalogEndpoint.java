package cn.globalph.b2c.api.rest.endpoint.catalog;

import cn.globalph.b2c.api.endpoint.catalog.BasicCatalogEndpoint;
import cn.globalph.b2c.catalog.domain.wrap.CategoriesWrap;
import cn.globalph.b2c.catalog.domain.wrap.CategoryAttributeWrap;
import cn.globalph.b2c.catalog.domain.wrap.CategoryWrap;
import cn.globalph.b2c.product.domain.wrap.MediaWrap;
import cn.globalph.b2c.product.domain.wrap.SkuWrap;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.b2c.search.domain.SkuSearchResult;
import cn.globalph.b2c.search.domain.wrap.SearchResultsWrap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

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

/**
 * This is a reference REST API endpoint for catalog. This can be modified, used as is, or removed. 
 * The purpose is to provide an out of the box RESTful catalog service implementation, but also 
 * to allow the implementor to have fine control over the actual API, URIs, and general JAX-RS annotations.
 * 
 * This class exposes catalog services as RESTful APIs.  It is dependent on
 * a JAX-RS implementation such as Jersey.  This class must be extended, with appropriate JAX-RS 
 * annotations, such as: <br></br> 
 * 
 * <code>javax.ws.rs.@Scope</code> <br></br> 
 * <code>javax.ws.rs.@Path</code> <br></br> 
 * <code>javax.ws.rs.@Produces</code> <br></br> 
 * <code>javax.ws.rs.@Consumes</code> <br></br> 
 * <code>javax.ws.rs.@Context</code> <br></br> 
 * etc... <br></br>
 * 
 * ... in the subclass.  The subclass must also be a Spring Bean.  The subclass can then override 
 * the methods, and specify custom inputs and outputs.  It will also specify 
 * <code>javax.ws.rs.@Path annotations</code>, <code>javax.ws.rs.@Context</code>, 
 * <code>javax.ws.rs.@PathParam</code>, <code>javax.ws.rs.@QueryParam</code>, 
 * <code>javax.ws.rs.@GET</code>, <code>javax.ws.rs.@POST</code>, etc...  Essentially, the subclass 
 * will override and extend the methods of this class, add new methods, and control the JAX-RS behavior 
 * using annotations according to the JAX-RS specification.
 */
@Component
@Scope("singleton")
@Path("/catalog/")
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class CatalogEndpoint extends BasicCatalogEndpoint {

	/**
     * 通过栏目名字查询栏目。
     * 没有名字参数查询所有栏目。
     */
    @GET
    @Path("categories")
    public CategoriesWrap findAllCategories(@Context HttpServletRequest request,
            								   @QueryParam("name") String name,
            								   @QueryParam("limit") @DefaultValue("20") int limit,
            								   @QueryParam("offset") @DefaultValue("0") int offset) {
        return super.findAllCategories(name, limit, offset);
    }
    
    @GET
    @Path("search")
    public CategoryWrap findCategoryByIdOrNameOrUrl(@Context HttpServletRequest request,
            @QueryParam("searchParameter") String searchParameter,
            @QueryParam("productOffset") @DefaultValue("0") int productOffset,
            @QueryParam("productLimit") @DefaultValue("20") int productLimit,
            @QueryParam("subcategoryOffset") @DefaultValue("0") int subcategoryOffset,
            @QueryParam("subcategoryLimit") @DefaultValue("20") int subcategoryLimit) {
        return super.findCategoryByIdOrNameOrUrl(searchParameter,
                productLimit, productOffset, subcategoryLimit, subcategoryOffset);
    }
	
    @GET
    @Path("{categoryId}/search/products")
    public SearchResultsWrap findProductsByCategoryAndQuery(@Context HttpServletRequest request,
            @PathParam("categoryId") Long categoryId,
            @QueryParam("q") String q,
            @QueryParam("page") @DefaultValue("1") Integer page,
            @QueryParam("pageSize") @DefaultValue("50") Integer pageSize) {
		SkuSearchCriteria searchCriteria = facetService.buildSearchCriteria(request);
		SkuSearchResult result = super.findProductsByCategoryAndQuery(searchCriteria, categoryId, q, pageSize, page);
		facetService.setActiveFacetResults(result.getFacets(), request);
		return searchResultsWrapper.wrapDetails(result);
    }
    
    @GET
    @Path("{id}")
    public CategoryWrap findCategoryById(@Context HttpServletRequest request,
            @PathParam("id") Long id,
            @QueryParam("productOffset") @DefaultValue("0") int productOffset,
            @QueryParam("productLimit") @DefaultValue("20") int productLimit,
            @QueryParam("subcategoryOffset") @DefaultValue("0") int subcategoryOffset,
            @QueryParam("subcategoryLimit") @DefaultValue("20") int subcategoryLimit) {
        return super.findCategoryById(id, productLimit, productOffset, subcategoryLimit, subcategoryOffset);
    }
    
    @GET
    @Path("{id}/categories")
    public CategoriesWrap findSubCategories(@Context HttpServletRequest request,
            @PathParam("id") Long id,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("active") @DefaultValue("true") boolean active) {
        return super.findSubCategories(id, limit, offset, active);
    }

    @GET
    @Path("{id}/activeCategories")
    public CategoriesWrap findActiveSubCategories(@Context HttpServletRequest request,
            @PathParam("id") Long id,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset) {
        return super.findActiveSubCategories(id, limit, offset);
    }
    
    @GET
    @Path("{id}/category-attributes")
    public List<CategoryAttributeWrap> findCategoryAttributesForCategory(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findCategoryAttributesForCategory(id);
    }

    @GET
    @Path("{id}/media")
    public List<MediaWrap> findMediaForCategory(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findMediaForCategory(id);
    }
    
    @GET
    @Path("{id}/skus")
    public List<SkuWrap> findCategorySkus(@Context HttpServletRequest request, @PathParam("id") Long id){
    	SkuSearchCriteria searchCriteria = facetService.buildSearchCriteria(request);
    	return super.findCategorySkus(searchCriteria, id);
    }
}
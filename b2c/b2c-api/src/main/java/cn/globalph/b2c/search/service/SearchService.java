package cn.globalph.b2c.search.service;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.search.domain.SearchFacetDTO;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.b2c.search.domain.SkuSearchResult;
import cn.globalph.b2c.search.solr.SolrIndexService;
import cn.globalph.common.exception.ServiceException;

import java.io.IOException;
import java.util.List;

public interface SearchService {

    /**
     * This method delegates to {@link SolrIndexService#rebuildIndex()}. It is here to preserve backwards-compatibility
     * with sites that were originally configured to run Broadleaf with Solr before 2.2.0.
     * 
     * @throws ServiceException
     * @throws IOException
     */
    public void rebuildIndex() throws ServiceException, IOException;
    
    /**
     * 搜索栏目SKU
     * Performs a search for skus in the given category, taking into consideration the SkuSearchCriteria
     * This method will return skus that are in any sub-level of a given category. For example, if you had a 
     * "Routers" category and a "Enterprise Routers" sub-category, asking for products in "Routers", would return
     * products that are in the "Enterprise Routers" category. 
     * 
     * @see #findExplicitSkusByCategory(Category, SkuSearchCriteria)
     * 
     * @param category
     * @param searchCriteria
     * @return the result of the search
     * @throws ServiceException 
     */
    public SkuSearchResult findSkusByCategory(Category category, SkuSearchCriteria searchCriteria) throws ServiceException;
    
    /**
     * Performs a search for products in the given category, taking into consideration the SkuSearchCriteria
     * 
     * This method will NOT return products that are in a sub-level of a given category. For example, if you had a 
     * "Routers" category and a "Enterprise Routers" sub-category, asking for products in "Routers", would NOT return
     * products that are in the "Enterprise Routers" category. 
     * 
     * @see #findSkusByCategory(Category, SkuSearchCriteria)
     * 
     * @param category
     * @param searchCriteria
     * @return
     * @throws ServiceException
     */
    public SkuSearchResult findExplicitSkusByCategory(Category category, SkuSearchCriteria searchCriteria) throws ServiceException;
    
    /**
     * Performs a search for products across all categories for the given query, taking into consideration
     * the ProductSearchCriteria
     * 
     * @param query
     * @param searchCriteria
     * @return the result of the search
     * @throws ServiceException 
     */
    public SkuSearchResult findSkusByQuery(String query, SkuSearchCriteria searchCriteria) throws ServiceException;
    
    /**
     * Performs a search for products in the given category for the given query, taking into consideration 
     * the ProductSearchCriteria
     * 
     * @param category
     * @param query
     * @param searchCriteria
     * @throws ServiceException
     */
    public SkuSearchResult findProductsByCategoryAndQuery(Category category, String query,
            SkuSearchCriteria searchCriteria) throws ServiceException;

    /**
     * Gets all available facets for search results page
     * 
     * @return the available facets
     */
    public List<SearchFacetDTO> getSearchFacets();

    /**
     * Gets all available facets for a given category
     * 
     * @param category
     * @return the available facets
     */
    public List<SearchFacetDTO> getCategoryFacets(Category category);

}
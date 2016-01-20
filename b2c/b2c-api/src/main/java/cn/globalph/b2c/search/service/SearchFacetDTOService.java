package cn.globalph.b2c.search.service;

import cn.globalph.b2c.search.domain.SearchFacetDTO;
import cn.globalph.b2c.search.domain.SearchFacetResultDTO;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * Provides methods that facilitate interactions with SearchFacetDTOs and SearchFacetResultDTOs
 * 
 * @author Andre Azzolini (apazzolini)
 */
public interface SearchFacetDTOService {

    /**
     * Given a servlet request and a list of available facets for this request (could be search or category based),
     * this method will build out a ProductSearchCriteria object to be used by the ProductSearchService. It will
     * perform translations from query string parameters to the ProductSearchCriteria.
     * 
     * @param request
     * @param availableFacets
     * @return the ProductSearchCriteria 
     */
    public SkuSearchCriteria buildSearchCriteria(HttpServletRequest request);

    /**
     * Sets the "active" boolean on a given SearchFacetResultDTO as determined by the current request
     * 
     * @param facets
     * @param request
     */
    public void setActiveFacetResults(List<SearchFacetDTO> facets, HttpServletRequest request);

    /**
     * Returns whether or not the SearchFacetResultDTO's key/value pair is present in the servlet request
     * 
     * @param result
     * @param request
     * @return if the result is active
     */
    public boolean isActive(SearchFacetResultDTO result, HttpServletRequest request);
    
    /**
     * Gets the url abbreviation associated with a given SearchFacetResultDTO.
     * 
     * @param result
     * @return the key associated with a SearchFacetResultDTO
     */
    public String getUrlKey(SearchFacetResultDTO result);

    /**
     * Gets the value of the given SearchFacetResultDTO.
     * The default Broadleaf implementation will return the String value of the result if the value
     * is not empty, or "range[<min-value>:<max-value>]" if the value was empty.
     * 
     * @param result
     * @return the value of the SearchFacetResultDTO
     */
    public String getValue(SearchFacetResultDTO result);


}

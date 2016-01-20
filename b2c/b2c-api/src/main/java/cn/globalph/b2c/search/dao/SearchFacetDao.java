package cn.globalph.b2c.search.dao;

import cn.globalph.b2c.search.domain.SearchFacet;

import java.util.List;

/**
 * DAO used to interact with the database search facets
 * 
 */
public interface SearchFacetDao {

    /**
     * Returns the distinct values for the given fieldName inside of the search clas sas a list of the specified 
     * type. For example, reading the distinct values for "manufacturer" in the ProductImpl class and specifying
     * the value class as String would search the ProductImpl entity's distinct manufacturers and return a 
     * List<String> of these values.
     * 
     * @param fieldName
     * @param fieldValueClass
     * @return  the distinct values for the field
     */
    public <T> List<T> readDistinctValuesForField(String fieldName, Class<T> fieldValueClass);

    /**
     * Returns all SearchFacets that are tagged with showOnSearch
     * 
     * @return the facets to display on searches
     */
    public List<SearchFacet> readAllSearchFacets();

    /**
     * Persist to the data layer.
     *
     * @param searchFacet the instance to persist
     * @return the instance after it has been persisted
     */
    public SearchFacet save(SearchFacet searchFacet);
}

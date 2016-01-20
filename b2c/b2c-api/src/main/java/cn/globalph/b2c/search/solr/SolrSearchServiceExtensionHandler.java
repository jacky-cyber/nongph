package cn.globalph.b2c.search.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrInputDocument;

import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.search.domain.Field;
import cn.globalph.b2c.search.domain.SearchFacetDTO;
import cn.globalph.b2c.search.domain.SearchFacetRange;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.common.extension.ExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 * @author Andre Azzolini (apazzolini), bpolster
 */                                      
public interface SolrSearchServiceExtensionHandler extends ExtensionHandler {

    /**
     * Returns a prefix if required for the passed in facet.
     */
    public ExtensionResultStatusType buildPrefixListForSearchableFacet(Field field, List<String> prefixList);

    /**
     * Returns a prefix if required for the passed in searchable field. 
     */
    public ExtensionResultStatusType buildPrefixListForSearchableField(Field field, FieldType searchableFieldType,
            List<String> prefixList);

    /**
     * Builds the search facet ranges for the provided dto.
     * 
     * @param context
     * @param dto
     * @param ranges
     */
    public ExtensionResultStatusType filterSearchFacetRanges(SearchFacetDTO dto, List<SearchFacetRange> ranges);

    /**
     * Given the input field, populates the values array with the fields needed for the 
     * passed in field.   
     * 
     * For example, a handler might create multiple fields for the given passed in field.
     * @param product
     * @param field
     * @param values
     * @param propertyName
     * @param locales
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public ExtensionResultStatusType addPropertyValues(Product product, Field field, FieldType fieldType,
            Map<String, Object> values, String propertyName)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;

    /**
     * Provides an extension point to modify the SolrQuery.
     * 
     * @param context
     * @param query
     * @param qualifiedSolrQuery
     * @param facets
     * @param searchCriteria
     * @param defaultSort
     *      * @return
     */
    public ExtensionResultStatusType modifySolrQuery(SolrQuery query, String qualifiedSolrQuery,
            List<SearchFacetDTO> facets, SkuSearchCriteria searchCriteria, String defaultSort);

    /**
     * Allows the extension additional fields to the document that are not configured via the DB.
     */
    public ExtensionResultStatusType attachAdditionalBasicFields(Product product, SolrInputDocument document,
            SolrHelperService shs);
    
    /**
     * In certain scenarios, we may want to produce a different Solr document id than the default.
     * If this method returns {@link ExtensionResultStatusType#HANDLED}, the value placed in the 0th element
     * in the returnContainer should be used.
     * 
     * @param document
     * @param product
     * @return the extension result status type
     */
    public ExtensionResultStatusType getSolrDocumentId(SolrInputDocument document, Product product, String[] returnContainer);

    /**
     * In certain scenarios, the requested category id might not be the one that should be used in Solr.
     * If this method returns {@link ExtensionResultStatusType#HANDLED}, the value placed in the 0th element
     * in the returnContainer should be used.
     * 
     * @param tentativeId
     * @param returnContainer
     * @return the extension result status type
     */
    public ExtensionResultStatusType getCategoryId(Long tentativeId, Long[] returnContainer);

    /**
     * In certain scenarios, the requested product id might not be the one that should be used in Solr.
     * If this method returns {@link ExtensionResultStatusType#HANDLED}, the value placed in the 0th element
     * in the returnContainer should be used.
     * 
     * @param tentativeId
     * @param returnContainer
     * @return the extension result status type
     */
    public ExtensionResultStatusType getProductId(Long tentativeId, Long[] returnContainer);
    
}

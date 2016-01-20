package cn.globalph.b2c.search.solr;

import org.apache.solr.common.SolrInputDocument;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.search.domain.Field;
import cn.globalph.common.exception.ServiceException;

import java.util.List;

/**
 * @author Andre Azzolini (apazzolini)
 */
public interface SolrHelperService {

    /**
     * Swaps the primary and reindex cores.
     * If the reindex core is null, we are operating in single core mode. In this scenario, no swap occurs.
     * 
     * @throws ServiceException 
     */
    public void swapActiveCores() throws ServiceException;

    /**
     * Determines the current namespace we are operating on. For example, if you have multiple sites set up, 
     * you may want to filter that here. 
     * 
     * <ul>
     *     <li>Note: This method should ALWAYS return a non-empty string.</li>
     * </ul>
     * 
     * @return the global namespace 
     */
    public String getCurrentNamespace();

    /**
     * This property is needed to be non-null to allow filtering by multiple facets at one time and have the results
     * be an AND of the facets. Apart from being non-empty, the actual value does not matter.
     * 
     * @return the non-empty global facet tag field
     */
    public String getGlobalFacetTagField();

    /**
     * Returns the property name for the given field, field type, and prefix
     * 
     * @param field
     * @param searchableFieldType
     * @param prefix
     * @return the property name for the field and fieldtype
     */
    public String getPropertyNameForFieldSearchable(Field field, FieldType searchableFieldType, String prefix);

    /**
     * Returns the property name for the given field, its configured facet field type, and the given prefix
     * 
     * @param field
     * @param prefix
     * @return the property name for the facet type of this field
     */
    public String getPropertyNameForFieldFacet(Field field, String prefix);
    
    /**
     * Returns the searchable field types for the given field. If there were none configured, will return
     * a list with TEXT FieldType.
     * 
     * @param field
     * @return the searchable field types for the given field
     */
    public List<FieldType> getSearchableFieldTypes(Field field);

    /**
     * Returns the property name for the given field and field type. This will apply the global prefix to the field,
     * and it will also apply either the locale prefix or the pricelist prefix, depending on whether or not the field
     * type was set to FieldType.PRICE
     * 
     * @param field
     * @param searchableFieldType
     * @return the property name for the field and fieldtype
     */
    public String getPropertyNameForFieldSearchable(Field field, FieldType searchableFieldType);

    /**
     * Returns the property name for the given field and its configured facet field type. This will apply the global prefix 
     * to the field, and it will also apply either the locale prefix or the pricelist prefix, depending on whether or not 
     * the field type was set to FieldType.PRICE
     * 
     * @param field
     * @return the property name for the facet type of this field
     */
    public String getPropertyNameForFieldFacet(Field field);
    
    /**
     * @param product
     * @return the Solr id of this product
     */
    public String getSolrDocumentId(SolrInputDocument document, Product product);

    /**
     * @return the name of the field that keeps track what namespace this document belongs to
     */
    public String getNamespaceFieldName();

    /**
     * @return the id field name, with the global prefix as appropriate
     */
    public String getIdFieldName();
    
    /**
     * @return the productId field name
     */
    public String getProductIdFieldName();

    /**
     * @return the category field name, with the global prefix as appropriate
     */
    public String getCategoryFieldName();

    /**
     * @return the explicit category field name, with the global prefix as appropriate
     */
    public String getExplicitCategoryFieldName();

    /**
     * @param category
     * @return the default sort field name for this category
     */
    public String getCategorySortFieldName(Category category);

    /**
     * @param categoryId
     * @return the default sort field name for this category
     */
    public String getCategorySortFieldName(Long categoryId);

    /**
     * In certain cases, the category id used for Solr indexing is different than the direct id on the product.
     * This method provides a hook to substitute the category id if necessary.
     * 
     * @param tentativeCategoryId
     * @return the category id to use
     */
    public Long getCategoryId(Long tentativeCategoryId);

    /**
     * In certain cases, the product id used for Solr indexing is different than the direct id on the product.
     * This method provides a hook to substitute the product id if necessary.
     * 
     * @param tentativeCategoryId
     * @return the category id to use
     */
    public Long getProductId(Long tentativeProductId);

}

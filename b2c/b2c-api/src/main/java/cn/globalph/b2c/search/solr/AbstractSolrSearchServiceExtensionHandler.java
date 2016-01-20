package cn.globalph.b2c.search.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrInputDocument;

import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.search.domain.Field;
import cn.globalph.b2c.search.domain.SearchFacetDTO;
import cn.globalph.b2c.search.domain.SearchFacetRange;
import cn.globalph.b2c.search.domain.SkuSearchCriteria;
import cn.globalph.common.extension.AbstractExtensionHandler;
import cn.globalph.common.extension.ExtensionResultStatusType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;


/**
 * Implementors of the SolrSearchServiceExtensionHandler interface should extend this class so that if 
 * additional extension points are added which they don't care about, their code will not need to be
 * modified.
 * 
 * @author bpolster
 */                                      
public abstract class AbstractSolrSearchServiceExtensionHandler extends AbstractExtensionHandler
        implements SolrSearchServiceExtensionHandler {

    @Override
    public ExtensionResultStatusType buildPrefixListForSearchableFacet(Field field, List<String> prefixList) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType buildPrefixListForSearchableField(Field field, FieldType searchableFieldType, List<String> prefixList) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType filterSearchFacetRanges(SearchFacetDTO dto, List<SearchFacetRange> ranges) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType addPropertyValues(Product product, Field field, FieldType fieldType,
            Map<String, Object> values, String propertyName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType modifySolrQuery(SolrQuery query, String qualifiedSolrQuery, List<SearchFacetDTO> facets, SkuSearchCriteria searchCriteria, String defaultSort) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType attachAdditionalBasicFields(Product product, SolrInputDocument document,
            SolrHelperService shs) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType getSolrDocumentId(SolrInputDocument document, Product product, String[] returnContainer) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType getCategoryId(Long tentativeId, Long[] returnContainer) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

    @Override
    public ExtensionResultStatusType getProductId(Long tentativeId, Long[] returnContainer) {
        return ExtensionResultStatusType.NOT_HANDLED;
    }

}

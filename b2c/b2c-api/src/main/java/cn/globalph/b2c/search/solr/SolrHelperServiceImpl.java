package cn.globalph.b2c.search.solr;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;

import cn.globalph.b2c.catalog.domain.Category;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.search.domain.Field;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.extension.ExtensionResultStatusType;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

/**
 * Provides utility methods that are used by other Solr service classes
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Service("blSolrHelperService")
public class SolrHelperServiceImpl implements SolrHelperService {

    private static final Log LOG = LogFactory.getLog(SolrHelperServiceImpl.class);

    // The value of these two fields has no special significance, but they must be non-blank
    protected static final String GLOBAL_FACET_TAG_FIELD = "a";
    protected static final String DEFAULT_NAMESPACE = "d";

    protected static final String PREFIX_SEPARATOR = "_";

    protected static SolrServer server;

    @Resource(name = "blSolrSearchServiceExtensionManager")
    protected SolrSearchServiceExtensionManager extensionManager;

    @Override
    public void swapActiveCores() throws ServiceException {
        if (SolrContext.isSingleCoreMode()) {
            LOG.debug("In single core mode. There are no cores to swap.");
        } else {
            LOG.debug("Swapping active cores");

            CoreAdminRequest car = new CoreAdminRequest();
            car.setCoreName(SolrContext.PRIMARY);
            car.setOtherCoreName(SolrContext.REINDEX);
            car.setAction(CoreAdminAction.SWAP);

            try {
                SolrContext.getAdminServer().request(car);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw new ServiceException("Unable to swap cores", e);
            }
        }
    }

    @Override
    public String getCurrentNamespace() {
        return DEFAULT_NAMESPACE;
    }

    @Override
    public String getGlobalFacetTagField() {
        return GLOBAL_FACET_TAG_FIELD;
    }

    @Override
    public String getPropertyNameForFieldSearchable(Field field, FieldType searchableFieldType, String prefix) {
        return new StringBuilder()
                .append(prefix)
                .append(field.getAbbreviation()).append("_").append(searchableFieldType.getType())
                .toString();
    }

    @Override
    public String getPropertyNameForFieldFacet(Field field, String prefix) {
        if (field.getFacetFieldType() == null) {
            return null;
        }

        return new StringBuilder()
                .append(prefix)
                .append(field.getAbbreviation()).append("_").append(field.getFacetFieldType().getType())
                .toString();
    }
    
    @Override
    public List<FieldType> getSearchableFieldTypes(Field field) {
        // We will index all configured searchable field types
        List<FieldType> typesToConsider = new ArrayList<FieldType>();
        if (CollectionUtils.isNotEmpty(field.getSearchableFieldTypes())) {
            typesToConsider.addAll(field.getSearchableFieldTypes());
        }
        
        // If there were no searchable field types configured, we will use TEXT as a default one
        if (CollectionUtils.isEmpty(typesToConsider)) {
            typesToConsider.add(FieldType.TEXT);
        }
        
        return typesToConsider;
    }

    @Override
    public String getPropertyNameForFieldSearchable(Field field, FieldType searchableFieldType) {
        List<String> prefixList = new ArrayList<String>();
        extensionManager.getHandlerProxy().buildPrefixListForSearchableField(field, searchableFieldType, prefixList);
        String prefix = convertPrefixListToString(prefixList);
        return getPropertyNameForFieldSearchable(field, searchableFieldType, prefix);
    }

    @Override
    public String getPropertyNameForFieldFacet(Field field) {
        FieldType fieldType = field.getFacetFieldType();
        if (fieldType == null) {
            return null;
        }

        List<String> prefixList = new ArrayList<String>();

        extensionManager.getHandlerProxy().buildPrefixListForSearchableFacet(field, prefixList);
        String prefix = convertPrefixListToString(prefixList);

        return getPropertyNameForFieldFacet(field, prefix);
    }

    protected String convertPrefixListToString(List<String> prefixList) {
        StringBuilder prefixString = new StringBuilder();
        for (String prefix : prefixList) {
            if (prefix != null && !prefix.isEmpty()) {
                prefixString = prefixString.append(prefix).append(PREFIX_SEPARATOR);
            }
        }
        return prefixString.toString();
    }
    
    @Override
    public Long getCategoryId(Long tentativeCategoryId) {
        Long[] returnId = new Long[1];
        ExtensionResultStatusType result = extensionManager.getHandlerProxy().getCategoryId(tentativeCategoryId, returnId);
        if (result.equals(ExtensionResultStatusType.HANDLED)) {
            return returnId[0];
        }
        return tentativeCategoryId;
    }

    @Override
    public Long getProductId(Long tentativeProductId) {
        Long[] returnId = new Long[1];
        ExtensionResultStatusType result = extensionManager.getHandlerProxy().getProductId(tentativeProductId, returnId);
        if (result.equals(ExtensionResultStatusType.HANDLED)) {
            return returnId[0];
        }
        return tentativeProductId;
    }

    @Override
    public String getSolrDocumentId(SolrInputDocument document, Product product) {
        String[] returnId = new String[1];
        ExtensionResultStatusType result = extensionManager.getHandlerProxy().getSolrDocumentId(document, product, returnId);
        if (result.equals(ExtensionResultStatusType.HANDLED)) {
            return returnId[0];
        }
        return String.valueOf(product.getId());
    }
    
    @Override
    public String getNamespaceFieldName() {
        return "namespace";
    }

    @Override
    public String getIdFieldName() {
        return "id";
    }
    
    @Override
    public String getProductIdFieldName() {
        return "productId";
    }

    @Override
    public String getCategoryFieldName() {
        return "category";
    }

    @Override
    public String getExplicitCategoryFieldName() {
        return "explicitCategory";
    }

    @Override
    public String getCategorySortFieldName(Category category) {
        Long categoryId = getCategoryId(category.getId());
        return new StringBuilder()
                .append(getCategoryFieldName())
                .append("_").append(categoryId).append("_").append("sort_d")
                .toString();
    }

    @Override
    public String getCategorySortFieldName(Long categoryId) {
        categoryId = getCategoryId(categoryId);
        return new StringBuilder()
                .append(getCategoryFieldName())
                .append("_").append(categoryId).append("_").append("sort_d")
                .toString();
    }
}

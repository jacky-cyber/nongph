package cn.globalph.b2c.search.solr;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import cn.globalph.b2c.catalog.service.dynamic.DynamicSkuActiveDatesService;
import cn.globalph.b2c.catalog.service.dynamic.DynamicSkuPricingService;
import cn.globalph.b2c.catalog.service.dynamic.SkuActiveDateConsiderationContext;
import cn.globalph.b2c.catalog.service.dynamic.SkuPricingConsiderationContext;
import cn.globalph.b2c.product.dao.ProductDao;
import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.search.dao.CatalogStructure;
import cn.globalph.b2c.search.dao.FieldDao;
import cn.globalph.b2c.search.dao.SolrIndexDao;
import cn.globalph.b2c.search.domain.Field;
import cn.globalph.common.exception.ExceptionHelper;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.extension.ExtensionResultStatusType;
import cn.globalph.common.util.BLCCollectionUtils;
import cn.globalph.common.util.StopWatch;
import cn.globalph.common.util.TransactionUtils;
import cn.globalph.common.util.TypedTransformer;
import cn.globalph.common.web.WebRequestContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;


/**
 * Responsible for building and rebuilding the Solr index
 * 
 * @author Andre Azzolini (apazzolini)
 * @author Jeff Fischer
 */
@Service("blSolrIndexService")
public class SolrIndexServiceImpl implements SolrIndexService {

    private static final Log LOG = LogFactory.getLog(SolrIndexServiceImpl.class);

    @Value("${solr.index.product.pageSize}")
    protected int pageSize;

    @Resource(name = "blProductDao")
    protected ProductDao productDao;

    @Resource(name = "blFieldDao")
    protected FieldDao fieldDao;

    @Resource(name = "blSolrHelperService")
    protected SolrHelperService shs;

    @Resource(name = "blSolrSearchServiceExtensionManager")
    protected SolrSearchServiceExtensionManager extensionManager;

    @Resource(name = "blTransactionManager")
    protected PlatformTransactionManager transactionManager;

    @Resource(name = "blSolrIndexDao")
    protected SolrIndexDao solrIndexDao;

    public static String ATTR_MAP = "productAttributes";

    @Override
    public void performCachedOperation(SolrIndexCachedOperation.CacheOperation cacheOperation) throws ServiceException {
        try {
            CatalogStructure cache = new CatalogStructure();
            SolrIndexCachedOperation.setCache(cache);
            cacheOperation.execute();
        } finally {
            if (LOG.isInfoEnabled()) {
                LOG.info("Cleaning up Solr index cache from memory - size approx: " + getCacheSizeInMemoryApproximation(SolrIndexCachedOperation.getCache()) + " bytes");
            }
            SolrIndexCachedOperation.clearCache();
        }
    }

    protected int getCacheSizeInMemoryApproximation(CatalogStructure structure) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(structure);
            oos.close();
            return baos.size();
        } catch (IOException e) {
            throw ExceptionHelper.refineException(e);
        }
    }

    @Override
    public void rebuildIndex() throws ServiceException, IOException {
        LOG.info("Rebuilding the solr index...");
        StopWatch s = new StopWatch();

        // If we are in single core mode, we have to delete the documents before reindexing
        if (SolrContext.isSingleCoreMode()) {
            SolrIndexServiceImpl.this.deleteAllDocuments();
        }

        Object[] pack = saveState();
        try {
            final Long numProducts = productDao.readCountAllActiveProducts();
            if (LOG.isDebugEnabled()) {
                LOG.debug("There are " + numProducts + " total products");
            }
            performCachedOperation(new SolrIndexCachedOperation.CacheOperation() {
                @Override
                public void execute() throws ServiceException {
                    int page = 0;
                    while ((page * pageSize) < numProducts) {
                        buildIncrementalIndex(page, pageSize);
                        page++;
                    }
                }
            });
            optimizeIndex(SolrContext.getReindexServer());
        } finally {
            restoreState(pack);
        }

        // Swap the active and the reindex cores
        shs.swapActiveCores();

        // If we are not in single core mode, we delete the documents for the unused core after swapping
        if (!SolrContext.isSingleCoreMode()) {
            deleteAllDocuments();
        }

        LOG.info(String.format("Finished building index in %s", s.toLapString()));
    }

    protected void deleteAllDocuments() throws ServiceException {
        try {
            String deleteQuery = shs.getNamespaceFieldName() + ":(\"" + shs.getCurrentNamespace() + "\")";
            LOG.debug("Deleting by query: " + deleteQuery);
            SolrContext.getReindexServer().deleteByQuery(deleteQuery);
            SolrContext.getReindexServer().commit();
        } catch (Exception e) {
            throw new ServiceException("Could not delete documents", e);
        }
    }

    protected void buildIncrementalIndex(int page, int pageSize) throws ServiceException {
        buildIncrementalIndex(page, pageSize, true);
    }

    @Override
    public void buildIncrementalIndex(int page, int pageSize, boolean useReindexServer) throws ServiceException {
        if (SolrIndexCachedOperation.getCache() == null) {
            LOG.warn("Consider using SolrIndexService.performCachedOperation() in combination with " +
                    "SolrIndexService.buildIncrementalIndex() for better caching performance during solr indexing");
        }
        TransactionStatus status = TransactionUtils.createTransaction("readProducts",
                TransactionDefinition.PROPAGATION_REQUIRED, transactionManager, true);
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Building index - page: [%s], pageSize: [%s]", page, pageSize));
        }
        StopWatch s = new StopWatch();
        boolean cacheOperationManaged = false;
        try {
            CatalogStructure cache = SolrIndexCachedOperation.getCache();
            if (cache != null) {
                cacheOperationManaged = true;
            } else {
                cache = new CatalogStructure();
                SolrIndexCachedOperation.setCache(cache);
            }
            List<Product> products = readAllActiveProducts(page, pageSize);
            List<Long> productIds = BLCCollectionUtils.collectList(products, new TypedTransformer<Long>() {
                @Override
                public Long transform(Object input) {
                    return ((Product) input).getId();
                }
            });
            solrIndexDao.populateCatalogStructure(productIds, SolrIndexCachedOperation.getCache());

            List<Field> fields = fieldDao.readAllProductFields();

            Collection<SolrInputDocument> documents = new ArrayList<SolrInputDocument>();

            for (Product product : products) {
                SolrInputDocument doc = buildDocument(product, fields);
                //If someone overrides the buildDocument method and determines that they don't want a product 
                //indexed, then they can return null. If the document is null it does not get added to 
                //to the index.
                if (doc != null) {
                    documents.add(doc);
                }
            }

            logDocuments(documents);

            if (!CollectionUtils.isEmpty(documents)) {
                SolrServer server = useReindexServer ? SolrContext.getReindexServer() : SolrContext.getServer();
                server.add(documents);
                server.commit();
            }
            TransactionUtils.finalizeTransaction(status, transactionManager, false);
        } catch (SolrServerException e) {
            TransactionUtils.finalizeTransaction(status, transactionManager, true);
            throw new ServiceException("Could not rebuild index", e);
        } catch (IOException e) {
            TransactionUtils.finalizeTransaction(status, transactionManager, true);
            throw new ServiceException("Could not rebuild index", e);
        } catch (RuntimeException e) {
            TransactionUtils.finalizeTransaction(status, transactionManager, true);
            throw e;
        } finally {
            if (!cacheOperationManaged) {
                SolrIndexCachedOperation.clearCache();
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Built index - page: [%s], pageSize: [%s] in [%s]", page, pageSize, s.toLapString()));
        }
    }

    /**
     * This method to read all active products will be slow if you have a large catalog. In this case, you will want to
     * read the products in a different manner. For example, if you know the fields that will be indexed, you can configure
     * a DAO object to only load those fields. You could also use a JDBC based DAO for even faster access. This default
     * implementation is only suitable for small catalogs.
     * 
     * @return the list of all active products to be used by the index building task
     */
    protected List<Product> readAllActiveProducts() {
        return productDao.readAllActiveProducts();
    }

    /**
     * This method to read active products utilizes paging to improve performance over {@link #readAllActiveProducts()}.
     * While not optimal, this will reduce the memory required to load large catalogs.
     * 
     * It could still be improved for specific implementations by only loading fields that will be indexed or by accessing
     * the database via direct JDBC (instead of Hibernate).
     * 
     * @return the list of all active products to be used by the index building task
     * @since 2.2.0
     */
    protected List<Product> readAllActiveProducts(int page, int pageSize) {
        return productDao.readAllActiveProducts(page, pageSize);
    }

    @Override
    public SolrInputDocument buildDocument(final Product product, List<Field> fields) {
        final SolrInputDocument document = new SolrInputDocument();

        attachBasicDocumentFields(product, document);

        // Keep track of searchable fields added to the index.   We need to also add the search facets if 
        // they weren't already added as a searchable field.
        List<String> addedProperties = new ArrayList<String>();

        for (Field field : fields) {
            try {
                // Index the searchable fields
                if (field.getSearchable()) {
                    List<FieldType> searchableFieldTypes = shs.getSearchableFieldTypes(field);
                    for (FieldType sft : searchableFieldTypes) {
                        Map<String, Object> propertyValues = getPropertyValues(product, field, sft);

                        // Build out the field for every prefix
                        for (Entry<String, Object> entry : propertyValues.entrySet()) {
                            String prefix = entry.getKey();
                            prefix = StringUtils.isBlank(prefix) ? prefix : prefix + "_";

                            String solrPropertyName = shs.getPropertyNameForFieldSearchable(field, sft, prefix);
                            Object value = entry.getValue();

                            document.addField(solrPropertyName, value);
                            addedProperties.add(solrPropertyName);
                        }
                    }
                }

                // Index the faceted field type as well
                FieldType facetType = field.getFacetFieldType();
                if (facetType != null) {
                    Map<String, Object> propertyValues = getPropertyValues(product, field, facetType);

                    // Build out the field for every prefix
                    for (Entry<String, Object> entry : propertyValues.entrySet()) {
                        String prefix = entry.getKey();
                        prefix = StringUtils.isBlank(prefix) ? prefix : prefix + "_";

                        String solrFacetPropertyName = shs.getPropertyNameForFieldFacet(field, prefix);
                        Object value = entry.getValue();

                        if (!addedProperties.contains(solrFacetPropertyName)) {
                            document.addField(solrFacetPropertyName, value);
                        }
                    }
                }
            } catch (Exception e) {
                LOG.trace("Could not get value for property[" + field.getQualifiedFieldName() + "] for product id["
                        + product.getId() + "]", e);
            }
        }

        return document;
    }

    /**
     * Adds the ID, category, and explicitCategory fields for the product to the document
     * 
     * @param product
     * @param document
     */
    protected void attachBasicDocumentFields(Product product, SolrInputDocument document) {
        boolean cacheOperationManaged = false;
        try {
            CatalogStructure cache = SolrIndexCachedOperation.getCache();
            if (cache != null) {
                cacheOperationManaged = true;
            } else {
                cache = new CatalogStructure();
                SolrIndexCachedOperation.setCache(cache);
                solrIndexDao.populateCatalogStructure(Arrays.asList(product.getId()), SolrIndexCachedOperation.getCache());
            }
            // Add the namespace and ID fields for this product
            document.addField(shs.getNamespaceFieldName(), shs.getCurrentNamespace());
            document.addField(shs.getIdFieldName(), shs.getSolrDocumentId(document, product));
            document.addField(shs.getProductIdFieldName(), product.getId());
            extensionManager.getHandlerProxy().attachAdditionalBasicFields(product, document, shs);

            // The explicit categories are the ones defined by the product itself
            if (cache.getParentCategoriesByProduct().containsKey(product.getId())) {
                for (Long categoryId : cache.getParentCategoriesByProduct().get(product.getId())) {
                    document.addField(shs.getExplicitCategoryFieldName(), shs.getCategoryId(categoryId));

                    String categorySortFieldName = shs.getCategorySortFieldName(shs.getCategoryId(categoryId));
                    String displayOrderKey = categoryId + "-" + shs.getProductId(product.getId());
                    BigDecimal displayOrder = cache.getDisplayOrdersByCategoryProduct().get(displayOrderKey);
                    if (displayOrder == null) {
                        displayOrderKey = categoryId + "-" + product.getId();
                        displayOrder = cache.getDisplayOrdersByCategoryProduct().get(displayOrderKey);
                    }

                    if (document.getField(categorySortFieldName) == null) {
                        document.addField(categorySortFieldName, displayOrder);
                    }

                    // This is the entire tree of every category defined on the product
                    buildFullCategoryHierarchy(document, cache, categoryId);
                }
            }
        } finally {
            if (!cacheOperationManaged) {
                SolrIndexCachedOperation.clearCache();
            }
        }
    }

    /**
     * Walk the category hierarchy upwards, adding a field for each level to the solr document
     *
     * @param document the solr document for the product
     * @param cache the catalog structure cache
     * @param categoryId the current category id
     */
    protected void buildFullCategoryHierarchy(SolrInputDocument document, CatalogStructure cache, Long categoryId) {
        Long catIdToAdd = shs.getCategoryId(categoryId); 

        Collection<Object> existingValues = document.getFieldValues(shs.getCategoryFieldName());
        if (existingValues == null || !existingValues.contains(catIdToAdd)) {
            document.addField(shs.getCategoryFieldName(), catIdToAdd);
        }

        Set<Long> parents = cache.getParentCategoriesByCategory().get(categoryId);
        for (Long parent : parents) {
            buildFullCategoryHierarchy(document, cache, parent);
        }
    }

    /**
     * Returns a map of prefix to value for the requested attributes. For example, if the requested field corresponds to
     * a Sku's description and the locales list has the en_US locale and the es_ES locale, the resulting map could be
     * 
     * { "en_US" : "A description",
     *   "es_ES" : "Una descripcion" }
     * 
     * @param product
     * @param field
     * @param fieldType
     * @param locales
     * @return the value of the property
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    protected Map<String, Object> getPropertyValues(Product product, Field field, FieldType fieldType) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        String propertyName = field.getPropertyName();
        Map<String, Object> values = new HashMap<String, Object>();

        if (extensionManager != null) {
            ExtensionResultStatusType result = extensionManager.getHandlerProxy().addPropertyValues(product, field, fieldType, values, propertyName);

            if (ExtensionResultStatusType.NOT_HANDLED.equals(result)) {
                Object propertyValue;
                if (propertyName.contains(ATTR_MAP)) {
                    propertyValue = PropertyUtils.getMappedProperty(product, ATTR_MAP, propertyName.substring(ATTR_MAP.length() + 1));
                    // It's possible that the value is an actual object, like ProductAttribute. We'll attempt to pull the 
                    // value field out of it if it exists.
                    if (propertyValue != null) {
                        try {
                            propertyValue = PropertyUtils.getProperty(propertyValue, "value");
                        } catch (NoSuchMethodException e) {
                            // Do nothing, we'll keep the existing value
                        }
                    }
                } else {
                    propertyValue = PropertyUtils.getProperty(product, propertyName);
                }
                values.put("", propertyValue);
            }
        }

        return values;
    }

    /**
     * Converts a propertyName to one that is able to reference inside a map. For example, consider the property
     * in Product that references a List<ProductAttribute>, "productAttributes". Also consider the utility method
     * in Product called "mappedProductAttributes", which returns a map of the ProductAttributes keyed by the name
     * property in the ProductAttribute. Given the parameters "productAttributes.heatRange", "productAttributes", 
     * "mappedProductAttributes" (which would represent a property called "productAttributes.heatRange" that 
     * references a specific ProductAttribute inside of a product whose "name" property is equal to "heatRange", 
     * this method will convert this property to mappedProductAttributes(heatRange).value, which is then usable 
     * by the standard beanutils PropertyUtils class to get the value.
     * 
     * @param propertyName
     * @param listPropertyName
     * @param mapPropertyName
     * @return the converted property name
     */
    protected String convertToMappedProperty(String propertyName, String listPropertyName, String mapPropertyName) {
        String[] splitName = StringUtils.split(propertyName, ".");
        StringBuilder convertedProperty = new StringBuilder();
        for (int i = 0; i < splitName.length; i++) {
            if (convertedProperty.length() > 0) {
                convertedProperty.append(".");
            }

            if (splitName[i].equals(listPropertyName)) {
                convertedProperty.append(mapPropertyName).append("(");
                convertedProperty.append(splitName[i + 1]).append(").value");
                i++;
            } else {
                convertedProperty.append(splitName[i]);
            }
        }
        return convertedProperty.toString();
    }

    @Override
    public Object[] saveState() {
         return new Object[] {
             WebRequestContext.getWebRequestContext(),
             SkuPricingConsiderationContext.getSkuPricingConsiderationContext(),
             SkuPricingConsiderationContext.getSkuPricingService(),
             SkuActiveDateConsiderationContext.getSkuActiveDatesService()
         };
     }
         
    @Override
    @SuppressWarnings("rawtypes")
    public void restoreState(Object[] pack) {
         WebRequestContext.setWebRequestContext((WebRequestContext) pack[0]);
         SkuPricingConsiderationContext.setSkuPricingConsiderationContext((HashMap) pack[1]);
         SkuPricingConsiderationContext.setSkuPricingService((DynamicSkuPricingService) pack[2]);
         SkuActiveDateConsiderationContext.setSkuActiveDatesService((DynamicSkuActiveDatesService) pack[3]);
     }
     
    @Override
    public void optimizeIndex(SolrServer server) throws ServiceException, IOException {
         try {
             if (LOG.isDebugEnabled()) {
                 LOG.debug("Optimizing the index...");
             }
             server.optimize();
         } catch (SolrServerException e) {
             throw new ServiceException("Could not optimize index", e);
         }
     }
    
    @Override
    public void logDocuments(Collection<SolrInputDocument> documents) {
        if (LOG.isTraceEnabled()) {
            for (SolrInputDocument document : documents) {
                LOG.trace(document);
            }
        }
    }

}

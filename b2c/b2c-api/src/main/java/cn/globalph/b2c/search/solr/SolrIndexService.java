package cn.globalph.b2c.search.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;

import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.search.domain.Field;
import cn.globalph.common.exception.ServiceException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Service exposing several methods for creating a Solr index based on catalog product data.
 *
 * @see cn.globalph.b2c.search.solr.SolrIndexCachedOperation
 * @author Andre Azzolini (apazzolini)
 * @author Jeff Fischer
 */
public interface SolrIndexService {

    /**
     * Rebuilds the current index. 
     * 
     * @throws IOException 
     * @throws ServiceException
     */
    public void rebuildIndex() throws ServiceException, IOException;
    
    /**
     * The internal method for building indexes. This is exposed via this interface in case someone would like to 
     * more granularly control the indexing strategy.
     * 
     * @see #restoreState(Object[])
     * @param page
     * @param pageSize
     * @param useReindexServer - if set to false will index directly on the primary server
     * @throws ServiceException
     */
    public void buildIncrementalIndex(int page, int pageSize, boolean useReindexServer) throws ServiceException;

    /**
     * Saves some global context that might be altered during indexing.
     * 
     * @return
     */
    public Object[] saveState();

    /**
     * Restores state that was saved prior to indexing that might have been altered.
     * 
     * @see #saveState()
     * @param pack
     */
    public void restoreState(Object[] pack);

    /**
     * Triggers the Solr optimize index function on the given server
     * 
     * @param server
     * @throws ServiceException
     * @throws IOException
     */
    public void optimizeIndex(SolrServer server) throws ServiceException, IOException;

    /**
     * Prints out the docs to the trace logger
     * 
     * @param documents
     */
    public void logDocuments(Collection<SolrInputDocument> documents);

    /**
     * Given a product, fields that relate to that product, and a list of locales and pricelists, builds a 
     * SolrInputDocument to be added to the Solr index.
     * 
     * @param product
     * @param fields
     * @param locales
     * @return the document
     */
    public SolrInputDocument buildDocument(Product product, List<Field> fields);

    /**
     * SolrIndexService exposes {@link #buildIncrementalIndex(int, int, boolean)}.
     * By wrapping the call to this method inside of a {@link cn.globalph.b2c.search.solr.SolrIndexCachedOperation.CacheOperation},
     * a single cache will be used for all the contained calls to buildIncrementalIndex. Here's an example:
     * {@code
     *  performCachedOperation(new SolrIndexCachedOperation.CacheOperation() {
     *        @Override
     *        public void execute() throws ServiceException {
     *            int page = 0;
     *            while ((page * pageSize) < numProducts) {
     *                buildIncrementalIndex(page, pageSize);
     *                page++;
     *            }
     *        }
     *    });
     * }
     * Note {@link #rebuildIndex()} already internally wraps its call using CacheOperation, so it is not necessary to call performCacheOperation
     * for calls to rebuildIndex().
     *
     * @param cacheOperation the block of code to perform using a single cache for best performance
     * @throws ServiceException
     */
    public void performCachedOperation(SolrIndexCachedOperation.CacheOperation cacheOperation) throws ServiceException;
}
package cn.globalph.b2c.search.solr;

import cn.globalph.b2c.search.dao.CatalogStructure;
import cn.globalph.common.exception.ServiceException;

/**
 * Provides a single cache while exposing a block of code for execution to
 * {@link cn.globalph.b2c.search.solr.SolrIndexService#performCachedOperation(cn.globalph.b2c.search.solr.SolrIndexCachedOperation.CacheOperation)}.
 * This serves to boost performance while executing multiple calls to {@link cn.globalph.b2c.search.solr.SolrIndexService#buildIncrementalIndex(int, int, boolean)}.
 *
 * @see cn.globalph.b2c.search.solr.SolrIndexService
 * @author Jeff Fischer
 */
public class SolrIndexCachedOperation {

    private static final ThreadLocal<CatalogStructure> CACHE = new ThreadLocal<CatalogStructure>();

    /**
     * Retrieve the cache bound to the current thread.
     *
     * @return The cache for the current thread, or null if not set
     */
    public static CatalogStructure getCache() {
        return CACHE.get();
    }

    /**
     * Set the cache on the current thread
     *
     * @param cache the cache object (usually an empty map)
     */
    public static void setCache(CatalogStructure cache) {
        CACHE.set(cache);
    }

    /**
     * Clear the thread local cache from the thread
     */
    public static void clearCache() {
        CACHE.remove();
    }

    /**
     * Basic interface representing a block of work to perform with a single cache instance
     */
    public interface CacheOperation {

        /**
         * Execute the block of work
         *
         * @throws ServiceException
         */
        void execute() throws ServiceException;

    }
}

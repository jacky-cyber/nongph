package cn.globalph.b2c.search.dao;

import java.util.List;

/**
 * Provides some specialized catalog retrieval methods for {@link cn.globalph.b2c.search.solr.SolrIndexService} for maximum
 * efficiency of solr document creation during indexing.
 *
 * @author Jeff Fischer
 */
public interface SolrIndexDao {

    /**
     * Populate the contents of a lightweight catalog structure for a list of products.
     *
     * @param productIds
     * @param catalogStructure lightweight container defining product and category hierarchies
     * @see cn.globalph.b2c.search.dao.CatalogStructure
     */
    void populateCatalogStructure(List<Long> productIds, CatalogStructure catalogStructure);

}

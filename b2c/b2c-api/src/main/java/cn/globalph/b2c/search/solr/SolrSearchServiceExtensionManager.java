package cn.globalph.b2c.search.solr;

import cn.globalph.common.extension.ExtensionManager;

import org.springframework.stereotype.Service;


/**
 * Manages extension points for SolrSearchService
 * @author bpolster
 */
@Service("blSolrSearchServiceExtensionManager")
public class SolrSearchServiceExtensionManager extends ExtensionManager<SolrSearchServiceExtensionHandler> {

    public SolrSearchServiceExtensionManager() {
        super(SolrSearchServiceExtensionHandler.class);
    }

}

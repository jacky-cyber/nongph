package cn.globalph.common.cache.engine;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * 
 * @felix.wu
 *
 */
@Deprecated
public class HydratedCacheElement extends Hashtable<String, Object> {

    private static final long serialVersionUID = 1L;

    public Object getCacheElementItem(String elementItemName, Serializable parentKey) {
        return get(elementItemName + "_" + parentKey);
    }
    
    public Object putCacheElementItem(String elementItemName, Serializable parentKey, Object value) {
        return put(elementItemName +"_"+parentKey, value);
    }
}

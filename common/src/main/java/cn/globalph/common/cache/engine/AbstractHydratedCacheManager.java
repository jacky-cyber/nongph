package cn.globalph.common.cache.engine;

import net.sf.ehcache.event.CacheEventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @felix.wu
 */
public abstract class AbstractHydratedCacheManager implements CacheEventListener, HydratedCacheManager, HydratedAnnotationManager {

    private static final Log LOG = LogFactory.getLog(AbstractHydratedCacheManager.class);

    private Map<String, HydrationDescriptor> hydrationDescriptors = Collections.synchronizedMap(new HashMap(100));

    @Override
    public HydrationDescriptor getHydrationDescriptor(Object entity) {
        if (hydrationDescriptors.containsKey(entity.getClass().getName())) {
            return hydrationDescriptors.get(entity.getClass().getName());
        }
        HydrationDescriptor descriptor = new HydrationDescriptor();
        Class<?> topEntityClass = getTopEntityClass(entity);
        HydrationScanner scanner = new HydrationScanner(topEntityClass, entity.getClass());
        scanner.init();
        descriptor.setHydratedMutators(scanner.getCacheMutators());
        Map<String, Method[]> mutators = scanner.getIdMutators();
        if (mutators.size() != 1) {
            throw new RuntimeException("Broadleaf Commerce Hydrated Cache currently only supports entities with a single @Id annotation.");
        }
        Method[] singleMutators = mutators.values().iterator().next();
        descriptor.setIdMutators(singleMutators);
        String cacheRegion = scanner.getCacheRegion();
        if (cacheRegion == null || "".equals(cacheRegion)) {
            cacheRegion = topEntityClass.getName();
        }
        descriptor.setCacheRegion(cacheRegion);
        hydrationDescriptors.put(entity.getClass().getName(), descriptor);
        return descriptor;
    }

    protected Class<?> getTopEntityClass(Object entity) {
        Class<?> myClass = entity.getClass();
        Class<?> superClass = entity.getClass().getSuperclass();
        while (superClass != null && superClass.getName().startsWith("org.broadleaf")) {
            myClass = superClass;
            superClass = superClass.getSuperclass();
        }
        return myClass;
    }

    @Override
    public void dispose() {
        if (LOG.isInfoEnabled()) {
            LOG.info("Disposing of all hydrated cache members");
        }
        hydrationDescriptors.clear();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return this;
    }

}

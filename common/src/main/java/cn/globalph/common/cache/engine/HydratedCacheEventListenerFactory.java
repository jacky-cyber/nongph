package cn.globalph.common.cache.engine;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * 
 * @felix.wu
 *
 */
public class HydratedCacheEventListenerFactory extends CacheEventListenerFactory {

    private static HydratedCacheManager manager = null;

    @Override
    public CacheEventListener createCacheEventListener(Properties props) {
        try {
            if (props == null || props.isEmpty()) {
                manager = EhcacheHydratedCacheManagerImpl.getInstance();
            } else {
                String managerClass = props.getProperty("managerClass");
                Class<?> clazz = Class.forName(managerClass);
                Method method = clazz.getDeclaredMethod("getInstance");
                manager = (HydratedCacheManager) method.invoke(null);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to create a CacheEventListener instance", e);
        }
        return (CacheEventListener) manager;
    }

    public static HydratedCacheManager getConfiguredManager() {
        return manager;
    }
}

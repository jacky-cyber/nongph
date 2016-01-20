package cn.globalph.openadmin.server.dao;

import cn.globalph.common.persistence.IdOverrideTableGenerator;
import cn.globalph.common.util.dao.DynamicDaoHelperImpl;
import cn.globalph.openadmin.server.service.DynamicEntityRemoteService;

import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Clear the static entity metadata caches from {@code DynamicEntityDao}
 * upon recycling of the session factory.
 *
 * @felix.wu
 */
public class SessionFactoryChangeListener implements SessionFactoryObserver {

    @Override
    public void sessionFactoryClosed(SessionFactory factory) {
        //do nothing
    }

    @Override
    public void sessionFactoryCreated(SessionFactory factory) {
        synchronized (DynamicDaoHelperImpl.LOCK_OBJECT) {
            DynamicEntityDaoImpl.METADATA_CACHE.clear();
            DynamicDaoHelperImpl.POLYMORPHIC_ENTITY_CACHE.clear();
            try {
                Field metadataCache = DynamicEntityRemoteService.class.getDeclaredField("METADATA_CACHE");
                metadataCache.setAccessible(true);
                ((Map) metadataCache.get(null)).clear();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            try {
                Field fieldCache = IdOverrideTableGenerator.class.getDeclaredField("FIELD_CACHE");
                fieldCache.setAccessible(true);
                ((Map) fieldCache.get(null)).clear();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

}

package cn.globalph.common.cache.engine;

/**
 * 
 * @felix.wu
 *
 */
public interface HydratedAnnotationManager {

    public HydrationDescriptor getHydrationDescriptor(Object entity);
    
}

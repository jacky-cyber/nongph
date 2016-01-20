package cn.globalph.common.util;

import cn.globalph.common.config.service.SystemPropertiesService;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Convenience class to faciliate getting system properties
 * 
 * Note that this class is scanned as a bean to pick up the applicationContext, but the methods
 * this class provides should be invoked statically.
 */
@Service("blBLCSystemProperty")
public class BLCSystemProperty implements ApplicationContextAware {

    protected static ApplicationContext applicationContext;
    
    /**
     * @see SystemPropertiesService#resolveSystemProperty(String)
     */
    public static String resolveSystemProperty(String name) {
        return getSystemPropertiesService().resolveSystemProperty(name);
    }

    /**
     * @see SystemPropertiesService#resolveIntSystemProperty(String)
     */
    public static int resolveIntSystemProperty(String name) {
        return getSystemPropertiesService().resolveIntSystemProperty(name);
    }

    /**
     * @see SystemPropertiesService#resolveBooleanSystemProperty(String)
     */
    public static boolean resolveBooleanSystemProperty(String name) {
        return getSystemPropertiesService().resolveBooleanSystemProperty(name);
    }

    /**
     * @see SystemPropertiesService#resolveLongSystemProperty(String)
     */
    public static long resolveLongSystemProperty(String name) {
        return getSystemPropertiesService().resolveLongSystemProperty(name);
    }
    
    /**
     * @return the "blSystemPropertiesService" bean from the application context
     */
    protected static SystemPropertiesService getSystemPropertiesService() {
        return (SystemPropertiesService) applicationContext.getBean("blSystemPropertiesService");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BLCSystemProperty.applicationContext = applicationContext;
    }

}

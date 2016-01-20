package cn.globalph.common.config.service;

import cn.globalph.common.config.domain.SystemProperty;
import cn.globalph.common.config.service.type.SystemPropertyFieldType;

public interface SystemPropertiesService {

    /**
     * Preferred method for looking up properties.   The method will return the configured value or 
     * if no override value is found, it will return the value passed in to the method as the default value.
     * 
     * @param name
     * @return
     */
    String resolveSystemProperty(String name);

    /**
     * Resolves an int system property.  Returns 0 when no matching property
     * is found.
     * 
     * @param name
     * @return
     */
    int resolveIntSystemProperty(String name);

    /**
     * Resolves a boolean system property.   Returns false when no matching
     * system property is found. 
     * 
     * @param name
     * @return
     */
    boolean resolveBooleanSystemProperty(String name);

    /**
     * Resolves an long system property. Returns 0 when no matching property
     * is found.
     * @param name
     * @return
     */
    long resolveLongSystemProperty(String name);

    /**
     * Determines if the given value is valid for the specified type
     * 
     * @param sp
     * @return whether or not the SystemProperty is in a valid state
     */
    public boolean isValueValidForType(String value, SystemPropertyFieldType type);

    /**
     * Evicts the given SystemProperty from the cache
     * 
     * @param systemProperty
     */
    public void removeFromCache(SystemProperty systemProperty);

    /**
     * Finds a SystemProperty by its internal id
     * 
     * @param id
     * @return the {@link SystemProperty}
     */
    public SystemProperty findById(Long id);
}

package cn.globalph.common.config.dao;

import cn.globalph.common.config.domain.SystemProperty;

import java.util.List;

/**
 * This DAO enables access to manage system properties that can be stored in the database.
 * <p/>
 * User: Kelly Tisdell
 * Date: 6/25/12
 */
public interface SystemPropertiesDao {

    public SystemProperty saveSystemProperty(SystemProperty systemProperty);

    public void deleteSystemProperty(SystemProperty systemProperty);

    public List<SystemProperty> readAllSystemProperties();

    public SystemProperty readSystemPropertyByName(String name);

    public SystemProperty createNewSystemProperty();

    /**
     * Reads a SystemProperty by its internal database id
     * 
     * @param id
     * @return the {@link SystemProperty}
     */
    public SystemProperty readById(Long id);

    /**
     * Removes the SystemProperty from the null-capable cache.
     *
     * @param systemProperty the property instance
     */
    public void removeFromCache(SystemProperty systemProperty);
}

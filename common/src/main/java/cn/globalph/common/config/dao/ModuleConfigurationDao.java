package cn.globalph.common.config.dao;

import cn.globalph.common.config.domain.ModuleConfiguration;
import cn.globalph.common.config.service.type.ModuleConfigurationType;

import java.util.List;

public interface ModuleConfigurationDao {

    public ModuleConfiguration readById(Long id);

    public ModuleConfiguration save(ModuleConfiguration config);

    public void delete(ModuleConfiguration config);

    public List<ModuleConfiguration> readAllByType(ModuleConfigurationType type);

    public List<ModuleConfiguration> readActiveByType(ModuleConfigurationType type);

    public List<ModuleConfiguration> readByType(Class<? extends ModuleConfiguration> type);

    /**
     * Returns the number of milliseconds that the current date/time will be cached for queries before refreshing.
     * This aids in query caching, otherwise every query that utilized current date would be different and caching
     * would be ineffective.
     *
     * @return the milliseconds to cache the current date/time
     */
    public Long getCurrentDateResolution();

    /**
     * Sets the number of milliseconds that the current date/time will be cached for queries before refreshing.
     * This aids in query caching, otherwise every query that utilized current date would be different and caching
     * would be ineffective.
     *
     * @param currentDateResolution the milliseconds to cache the current date/time
     */
    public void setCurrentDateResolution(Long currentDateResolution);
}

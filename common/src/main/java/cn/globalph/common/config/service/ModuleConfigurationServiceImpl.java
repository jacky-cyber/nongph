package cn.globalph.common.config.service;

import cn.globalph.common.config.dao.ModuleConfigurationDao;
import cn.globalph.common.config.domain.ModuleConfiguration;
import cn.globalph.common.config.service.type.ModuleConfigurationType;
import cn.globalph.common.util.TransactionUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service("blModuleConfigurationService")
public class ModuleConfigurationServiceImpl implements ModuleConfigurationService {

    @Resource(name = "blModuleConfigurationDao")
    protected ModuleConfigurationDao moduleConfigDao;

    @Override
    public ModuleConfiguration findById(Long id) {
        return moduleConfigDao.readById(id);
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public ModuleConfiguration save(ModuleConfiguration config) {
        return moduleConfigDao.save(config);
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public void delete(ModuleConfiguration config) {
        moduleConfigDao.delete(config);
    }

    @Override
    public List<ModuleConfiguration> findActiveConfigurationsByType(ModuleConfigurationType type) {
        return moduleConfigDao.readActiveByType(type);
    }

    @Override
    public List<ModuleConfiguration> findAllConfigurationByType(ModuleConfigurationType type) {
        return moduleConfigDao.readAllByType(type);
    }

    @Override
    public List<ModuleConfiguration> findByType(Class<? extends ModuleConfiguration> type) {
        return moduleConfigDao.readByType(type);
    }

}

package cn.globalph.openadmin.server.security.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.security.domain.AdminUser;
import cn.globalph.openadmin.server.service.persistence.validation.GlobalValidationResult;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 * 
 * 
 * @author Phillip Verheyden (phillipuniverse)
 */
@Service("blRowLevelSecurityService")
public class RowLevelSecurityServiceImpl implements RowLevelSecurityService {
    
    private static final Log LOG = LogFactory.getLog(RowLevelSecurityServiceImpl.class);
    
    @Resource(name = "blRowLevelSecurityProviders")
    List<RowLevelSecurityProvider> providers;
    
    @Override
    public void addFetchRestrictions(AdminUser currentUser, String ceilingEntity, List<Predicate> restrictions, List<Order> sorts,
            Root entityRoot,
            CriteriaQuery criteria,
            CriteriaBuilder criteriaBuilder) {
        for (RowLevelSecurityProvider provider : getProviders()) {
            provider.addFetchRestrictions(currentUser, ceilingEntity, restrictions, sorts, entityRoot, criteria, criteriaBuilder);
        }
    }

    @Override
    public boolean canUpdate(AdminUser currentUser, Entity entity) {
        for (RowLevelSecurityProvider provider : getProviders()) {
            if (!provider.canUpdate(currentUser, entity)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canRemove(AdminUser currentUser, Entity entity) {
        for (RowLevelSecurityProvider provider : getProviders()) {
            if (!provider.canRemove(currentUser, entity)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public GlobalValidationResult validateUpdateRequest(AdminUser currentUser, Entity entity, PersistencePackage persistencePackage) {
        GlobalValidationResult validationResult = new GlobalValidationResult(true);
        for (RowLevelSecurityProvider provider : getProviders()) {
            GlobalValidationResult providerValidation = provider.validateUpdateRequest(currentUser, entity, persistencePackage);
            if (providerValidation.isNotValid()) {
                validationResult.setValid(false);
                validationResult.addErrorMessage(providerValidation.getErrorMessage());
            }
        }
        return validationResult;
    }

    @Override
    public GlobalValidationResult validateRemoveRequest(AdminUser currentUser, Entity entity, PersistencePackage persistencePackage) {
        GlobalValidationResult validationResult = new GlobalValidationResult(true);
        for (RowLevelSecurityProvider provider : getProviders()) {
            GlobalValidationResult providerValidation = provider.validateRemoveRequest(currentUser, entity, persistencePackage);
            if (providerValidation.isNotValid()) {
                validationResult.setValid(false);
                validationResult.addErrorMessage(providerValidation.getErrorMessage());
            }
        }
        return validationResult;
    }
    
    @Override
    public List<RowLevelSecurityProvider> getProviders() {
        return providers;
    }
    
    public void setProviders(List<RowLevelSecurityProvider> providers) {
        this.providers = providers;
    }

}

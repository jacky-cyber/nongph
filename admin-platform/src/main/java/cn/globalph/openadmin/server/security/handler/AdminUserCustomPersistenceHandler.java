package cn.globalph.openadmin.server.security.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.util.BLCSystemProperty;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.security.domain.AdminUser;
import cn.globalph.openadmin.server.security.remote.EntityOperationType;
import cn.globalph.openadmin.server.security.remote.SecurityVerifier;
import cn.globalph.openadmin.server.security.service.AdminSecurityService;
import cn.globalph.openadmin.server.service.handler.CustomPersistenceOperationHandlerAdapter;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;

import org.springframework.stereotype.Component;

import java.util.Map;

import javax.annotation.Resource;

/**
 * 
 * @felix.wu
 *
 */
@Component("blAdminUserCustomPersistenceHandler")
public class AdminUserCustomPersistenceHandler extends CustomPersistenceOperationHandlerAdapter {
    
    private static final Log LOG = LogFactory.getLog(AdminUserCustomPersistenceHandler.class);
    
    @Resource(name="blAdminSecurityService")
    protected AdminSecurityService adminSecurityService;

    @Resource(name="blAdminSecurityRemoteService")
    protected SecurityVerifier adminRemoteSecurityService;

    protected boolean getRequireUniqueEmailAddress() {
        return BLCSystemProperty.resolveBooleanSystemProperty("admin.user.requireUniqueEmailAddress");
    }

    @Override
    public Boolean willHandleSecurity(PersistencePackage persistencePackage) {
        return true;
    }

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        try {
            return persistencePackage.getRootEntityClassname() != null && AdminUser.class.isAssignableFrom(Class.forName(persistencePackage.getRootEntityClassname()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return canHandleAdd(persistencePackage);
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        adminRemoteSecurityService.securityCheck(persistencePackage, EntityOperationType.ADD);
        Entity entity  = persistencePackage.getEntity();
        try {
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            AdminUser adminInstance = (AdminUser) Class.forName(entity.getType()[0]).newInstance();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(AdminUser.class.getName(), persistencePerspective);
            adminInstance = (AdminUser) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);
            
            Entity errorEntity = validateLegalUsernameAndEmail(entity, adminInstance, true);
            if (errorEntity != null) {
                return errorEntity;
            }
            
            adminInstance.setUnencodedPassword(adminInstance.getPassword());
            adminInstance.setPassword(null);

            adminInstance = adminSecurityService.saveAdminUser(adminInstance);

            Entity adminEntity = helper.getRecord(adminProperties, adminInstance, null, null);

            return adminEntity;
        } catch (Exception e) {
            throw new ServiceException("Unable to add entity for " + entity.getType()[0], e);
        }
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {       
        Entity entity = persistencePackage.getEntity();
        try {
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(AdminUser.class.getName(), persistencePerspective);
            Object primaryKey = helper.getPrimaryKey(entity, adminProperties);
            AdminUser adminInstance = (AdminUser) dynamicEntityDao.retrieve(Class.forName(entity.getType()[0]), primaryKey);
            dynamicEntityDao.detach(adminInstance);
            
            Entity errorEntity = validateLegalUsernameAndEmail(entity, adminInstance, false);
            if (errorEntity != null) {
                return errorEntity;
            }

            String passwordBefore = adminInstance.getPassword();
            adminInstance.setPassword(null);
            adminInstance = (AdminUser) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);
            Property passwordProperty = entity.getPMap().get("password");
            if (passwordProperty != null) {
                if (StringUtils.isNotEmpty(passwordProperty.getValue())) {
                    adminInstance.setUnencodedPassword(passwordProperty.getValue());
                    adminInstance.setPassword(null);
                } else {
                    adminInstance.setPassword(passwordBefore);
                }
            }
            
            // The current user can update their data, but they cannot update other user's data.
            if (! adminRemoteSecurityService.getPersistentAdminUser().getId().equals(adminInstance.getId())) {
                adminRemoteSecurityService.securityCheck(persistencePackage, EntityOperationType.UPDATE);
            }
            
            adminInstance = adminSecurityService.saveAdminUser(adminInstance);
            Entity adminEntity = helper.getRecord(adminProperties, adminInstance, null, null);

            return adminEntity;

        } catch (Exception e) {
            throw new ServiceException("Unable to update entity for " + entity.getType()[0], e);
        }
    }
    
    
    protected Entity validateLegalUsernameAndEmail(Entity entity, AdminUser adminInstance, boolean isAdd) {
        String login = entity.findProperty("login").getValue();
        String email = entity.findProperty("email").getValue();

        // We know the username/email is ok if we're doing an update and they're unchanged
        boolean skipLoginCheck = false;
        boolean skipEmailCheck = !getRequireUniqueEmailAddress();
        if (!isAdd) {
            if (StringUtils.equals(login, adminInstance.getLogin())) {
                skipLoginCheck = true;
            }

            if (!getRequireUniqueEmailAddress() || StringUtils.equals(email, adminInstance.getEmail())) {
                skipEmailCheck = true;
            }
        }
        
        if (!skipLoginCheck && adminSecurityService.readAdminUserByUserName(login) != null) {
            entity.addValidationError("login", "admin.nonUniqueUsernameError");
            return entity;
        }
        
        if (!skipEmailCheck && CollectionUtils.isNotEmpty(adminSecurityService.readAdminUsersByEmail(email))) {
            entity.addValidationError("email", "admin.nonUniqueEmailError");
            return entity;
        }

        return null;
    }
    
}

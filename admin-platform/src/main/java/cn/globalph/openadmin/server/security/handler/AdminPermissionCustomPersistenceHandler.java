package cn.globalph.openadmin.server.security.handler;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.FilterAndSortCriteria;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.security.domain.AdminPermission;
import cn.globalph.openadmin.server.security.domain.AdminPermissionImpl;
import cn.globalph.openadmin.server.security.service.type.PermissionType;
import cn.globalph.openadmin.server.service.handler.CustomPersistenceOperationHandlerAdapter;
import cn.globalph.openadmin.server.service.persistence.module.PersistenceModule;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author felix.wu
 */
@Component("blAdminPermissionCustomPersistenceHandler")
public class AdminPermissionCustomPersistenceHandler extends CustomPersistenceOperationHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(AdminPermissionCustomPersistenceHandler.class);

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        String[] criteria = persistencePackage.getCustomCriteria();
        return !ArrayUtils.isEmpty(criteria) && criteria[0].equals("createNewPermission") && AdminPermission.class.getName().equals(ceilingEntityFullyQualifiedClassname);
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return canHandleAdd(persistencePackage);
    }
    
    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        return AdminPermissionImpl.class.getName().equals(ceilingEntityFullyQualifiedClassname);

    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        if (persistencePackage.getEntity().findProperty("id") != null && !StringUtils.isEmpty(persistencePackage.getEntity().findProperty("id").getValue())) {
            return update(persistencePackage, dynamicEntityDao, helper);
        }
        Entity entity = checkPermissionName(persistencePackage);
        try {
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            AdminPermission adminInstance = (AdminPermission) Class.forName(entity.getType()[0]).newInstance();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(AdminPermission.class.getName(), persistencePerspective);
            adminInstance = (AdminPermission) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);

            adminInstance = (AdminPermission) dynamicEntityDao.merge(adminInstance);

            Entity adminEntity = helper.getRecord(adminProperties, adminInstance, null, null);

            return adminEntity;
        } catch (Exception e) {
            throw new ServiceException("Unable to add entity for " + entity.getType()[0], e);
        }
    }

    protected Entity checkPermissionName(PersistencePackage persistencePackage) throws ServiceException {
        Entity entity  = persistencePackage.getEntity();
        Property prop = entity.findProperty("name");
        String name = prop.getValue();
        name = name.toUpperCase();
        if (!name.startsWith("PERMISSION_")) {
            throw new ServiceException("All Permission names must start with PERMISSION_");
        }
        String[] parts = name.split("_");
        if (parts.length < 3) {
            throw new ServiceException("All Permission names must adhere to the naming standard: PERMISSION_[Permission Type]_[User Defined Section]. E.g. PERMISSION_READ_CATEGORY");
        }
        if (PermissionType.getInstance(parts[1]) == null) {
            throw new ServiceException("All Permission names must specify a valid permission type as part of the name. The permission name you specified (" + name + ") denotes the permission type of (" + parts[1] + "), which is not valid. See cn.globalph.openadmin.server.security.service.type.PermissionType for valid permission types.");
        }
        prop.setValue(name);
        return entity;
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        Entity entity = checkPermissionName(persistencePackage);
        try {
            PersistenceAssociation persistencePerspective = persistencePackage.getPersistenceAssociation();
            Map<String, FieldMetadata> adminProperties = helper.getSimpleMergedProperties(AdminPermission.class.getName(), persistencePerspective);
            Object primaryKey = helper.getPrimaryKey(entity, adminProperties);
            AdminPermission adminInstance = (AdminPermission) dynamicEntityDao.retrieve(Class.forName(entity.getType()[0]), primaryKey);
            adminInstance = (AdminPermission) helper.createPopulatedInstance(adminInstance, entity, adminProperties, false);

            adminInstance = (AdminPermission) dynamicEntityDao.merge(adminInstance);

            Entity adminEntity = helper.getRecord(adminProperties, adminInstance, null, null);

            return adminEntity;
        } catch (Exception e) {
            throw new ServiceException("Unable to update entity for " + entity.getType()[0], e);
        }
    }

    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        if (ArrayUtils.contains(persistencePackage.getCustomCriteria(), "includeFriendlyOnly")) {
            addFriendlyRestriction(cto);
        }
        addDefaultSort(cto);

        PersistenceModule myModule = helper.getCompatibleModule(persistencePackage.getPersistenceAssociation().getPersistenceOperationType().getFetchType());
        DynamicResultSet results = myModule.fetch(persistencePackage, cto);
        
        return results;
    }
    
    protected void addFriendlyRestriction(CriteriaTransferObject cto) {
        cto.add(new FilterAndSortCriteria("isFriendly", "true"));
    }
    
    protected void addDefaultSort(CriteriaTransferObject cto) {
        boolean userSort = false;
        for (FilterAndSortCriteria fasc : cto.getCriteriaMap().values()) {
            if (fasc.getSortDirection() != null) {
                userSort = true;
                break;
            }
        }
        if (!userSort) {
            FilterAndSortCriteria sortFasc = new FilterAndSortCriteria("description");
            sortFasc.setSortAscending(true);
            cto.add(sortFasc);
        }
    }
    
}

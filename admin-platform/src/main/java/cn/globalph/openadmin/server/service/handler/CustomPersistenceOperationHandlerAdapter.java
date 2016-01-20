package cn.globalph.openadmin.server.service.handler;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationExecutor;
import cn.globalph.openadmin.server.service.persistence.module.InspectHelper;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class for those {@link cn.globalph.openadmin.server.service.handler.CustomPersistenceOperationHandler} implementations
 * that do not wish to implement all the methods of the interface.
 *
 * @author Jeff Fischer
 */
public class CustomPersistenceOperationHandlerAdapter implements CustomPersistenceOperationHandler {

    @Override
    public Boolean canHandleInspect(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public Boolean canHandleRemove(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public DynamicResultSet inspect(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, InspectHelper helper) throws ServiceException {
        throw new ServiceException("Inspect not supported");
    }

    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        throw new ServiceException("Fetch not supported");
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        throw new ServiceException("Add not supported");
    }

    @Override
    public void remove(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
       throw new ServiceException("Remove not supported");
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        throw new ServiceException("Update not supported");
    }

    @Override
    public Boolean willHandleSecurity(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public int getOrder() {
        return CustomPersistenceOperationHandler.DEFAULT_ORDER;
    }
    
    /**
     * This is a helper method that can be invoked as a first step in a custom inspect phase
     */
    protected Map<String, FieldMetadata> getMetadata(PersistencePackage persistencePackage, InspectHelper helper) 
            throws ServiceException {
        String entityName = persistencePackage.getRootEntityClassname();
        PersistenceAssociation perspective = persistencePackage.getPersistenceAssociation();
        return helper.getSimpleMergedProperties(entityName, perspective);
    }
    
    /**
     * This is a helper method that can be invoked as the last step in a custom inspect phase. It will assemble the
     * appropriate DynamicResultSet from the given parameters.
     */
    protected DynamicResultSet getResultSet(PersistencePackage persistencePackage, InspectHelper helper,
            Map<String, FieldMetadata> metadata) throws ServiceException {
        String entityName = persistencePackage.getRootEntityClassname();
        try {
            if (helper instanceof PersistenceOperationExecutor) {
                Class<?>[] entities = ((PersistenceOperationExecutor) helper).getPolymorphicEntities(entityName);
                Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties = 
                        new HashMap<MergedPropertyType, Map<String, FieldMetadata>>();
                allMergedProperties.put(MergedPropertyType.PRIMARY, metadata);
                ClassMetadata mergedMetadata = helper.getMergedClassMetadata(entities, allMergedProperties);
                DynamicResultSet results = new DynamicResultSet(mergedMetadata);
                return results;
            }
        } catch (ClassNotFoundException e) {
            throw new ServiceException(e);
        }
        return new DynamicResultSet();
    }
}

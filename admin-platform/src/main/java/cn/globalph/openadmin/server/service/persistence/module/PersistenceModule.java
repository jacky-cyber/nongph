package cn.globalph.openadmin.server.service.persistence.module;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.presentation.client.OperationScope;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationExecutor;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author felix.wu
 *
 */
public interface PersistenceModule {

    public boolean isCompatible(OperationScope operationType);
    
    public Entity add(PersistencePackage persistencePackage) throws ServiceException;
    
    public void updateMergedProperties(PersistencePackage persistencePackage, Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties) throws ServiceException;
    
    public void extractProperties(Class<?>[] inheritanceLine, Map<MergedPropertyType, Map<String, FieldMetadata>> mergedProperties, List<Property> properties);
    
    public Entity update(PersistencePackage persistencePackage) throws ServiceException;
    
    public void remove(PersistencePackage persistencePackage) throws ServiceException;
    
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException;
    
    public void setPersistenceManager(PersistenceOperationExecutor persistenceManager);
    
}

package cn.globalph.openadmin.server.service.persistence;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.service.handler.CustomPersistenceOperationHandler;

/**
 * 持久操作执行者
 * @author felix.wu
 *
 */
public interface PersistenceOperationExecutor {

    public abstract Class<?>[] getAllPolymorphicEntitiesFromCeiling(Class<?> ceilingClass);

    public abstract Class<?>[] getPolymorphicEntities(String ceilingEntityFullyQualifiedClassname) throws ClassNotFoundException;

    public abstract Map<String, FieldMetadata> getSimpleMergedProperties(String entityName, PersistenceAssociation persistencePerspective) throws ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException;

    public abstract ClassMetadata getMergedClassMetadata(Class<?>[] entities, Map<MergedPropertyType, Map<String, FieldMetadata>> mergedProperties) throws ClassNotFoundException, IllegalArgumentException;

    public abstract PersistenceResponse inspect(PersistencePackage persistencePackage) throws ServiceException, ClassNotFoundException;

    public abstract PersistenceResponse fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException;

    public abstract PersistenceResponse add(PersistencePackage persistencePackage) throws ServiceException;

    public abstract PersistenceResponse update(PersistencePackage persistencePackage) throws ServiceException;

    public abstract PersistenceResponse remove(PersistencePackage persistencePackage) throws ServiceException;

    public abstract DynamicEntityDao getDynamicEntityDao();

    public abstract void setDynamicEntityDao(DynamicEntityDao dynamicEntityDao);

    public abstract Map<String, String> getTargetEntityManagers();

    public abstract void setTargetEntityManagers(Map<String, String> targetEntityManagers);

    public abstract TargetModeType getTargetMode();

    public abstract void setTargetMode(TargetModeType targetMode);

    public abstract List<CustomPersistenceOperationHandler> getCustomPersistenceHandlers();

    public abstract void setCustomPersistenceHandlers(List<CustomPersistenceOperationHandler> customPersistenceHandlers);

    public abstract Class<?>[] getUpDownInheritance(Class<?> testClass);

    public abstract Class<?>[] getUpDownInheritance(String testClassname) throws ClassNotFoundException;

}

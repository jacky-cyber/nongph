package cn.globalph.openadmin.server.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.openadmin.dto.ClassTree;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.ForeignKey;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.server.dao.provider.metadata.FieldMetadataProvider;
import cn.globalph.openadmin.server.service.persistence.module.FieldManager;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.type.Type;

/**
 * 通用数据访问对象接口
 * @author felix.wu
 *
 */
public interface DynamicEntityDao {

    public abstract Class<?>[] getAllPolymorphicEntitiesFromCeiling(Class<?> ceilingClass);

    public abstract Class<?>[] getAllPolymorphicEntitiesFromCeiling(Class<?> ceilingClass, boolean includeUnqualifiedPolymorphicEntities);

    public ClassTree getClassTreeFromCeiling(Class<?> ceilingClass);

    public ClassTree getClassTree(Class<?>[] polymorphicClasses);
    
    public abstract Map<String, FieldMetadata> getPropertiesForPrimitiveClass(String propertyName, String friendlyPropertyName, Class<?> targetClass, Class<?> parentClass, MergedPropertyType mergedPropertyType);
    
    public abstract Map<String, FieldMetadata> getMergedProperties(String ceilingEntityFullyQualifiedClassname, Class<?>[] entities, ForeignKey foreignField, String[] additionalNonPersistentProperties, ForeignKey[] additionalForeignFields, MergedPropertyType mergedPropertyType, Boolean populateManyToOneFields, String[] includeManyToOneFields, String[] excludeManyToOneFields, String configurationKey, String prefix);
    
    public abstract <T> T persist(T entity);
    
    public abstract <T> T merge(T entity);

    public abstract Serializable retrieve(Class<?> entityClass, Object primaryKey);
    
    public abstract void remove(Serializable entity);
    
    public abstract void clear();
    
    public void flush();
    
    public void detach(Serializable entity);
    
    public void refresh(Serializable entity);

    public Object find(Class<?> entityClass, Object key);

    public EntityManager getStandardEntityManager();
    
    public void setStandardEntityManager(EntityManager entityManager);

    /**
     * Get the Hibernate PersistentClass instance associated with the fully-qualified
     * class name. Will return null if no persistent class is associated with this name.
     *
     * @param targetClassName
     * @return The PersistentClass instance
     */
    public PersistentClass getPersistentClass(String targetClassName);
    
    public Map<String, FieldMetadata> getSimpleMergedProperties(String entityName, PersistenceAssociation persistencePerspective);

    public FieldManager getFieldManager();

    public EntityConfiguration getEntityConfiguration();

    public void setEntityConfiguration(EntityConfiguration entityConfiguration);

    public Map<String, Object> getIdMetadata(Class<?> entityClass);

    public List<Type> getPropertyTypes(Class<?> entityClass);

    public List<String> getPropertyNames(Class<?> entityClass);

    public Criteria createCriteria(Class<?> entityClass);

    public Field[] getAllFields(Class<?> targetClass);

    public PresentationAnnoInspector getPresentationAnnoInspector();

    public void setPresentationAnnoInspector(PresentationAnnoInspector metadata);
    
    public FieldMetadataProvider getDefaultFieldMetadataProvider();

    public SessionFactory getSessionFactory();

}

package cn.globalph.openadmin.server.service.persistence.module.provider;

import org.apache.commons.lang.ArrayUtils;

import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.dao.FieldInfo;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationExecutor;
import cn.globalph.openadmin.server.service.persistence.module.FieldManager;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * @author Jeff Fischer
 */
public abstract class AbstractFieldPersistenceProvider implements FieldPersistenceProvider {

    protected Class<?> getListFieldType(Serializable instance, FieldManager fieldManager, Property property, PersistenceOperationExecutor persistenceManager) {
        Class<?> returnType = null;
        Field field = fieldManager.getField(instance.getClass(), property.getName());
        java.lang.reflect.Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Class<?> clazz = (Class<?>) pType.getActualTypeArguments()[0];
            Class<?>[] entities = persistenceManager.getDynamicEntityDao().getAllPolymorphicEntitiesFromCeiling(clazz);
            if (!ArrayUtils.isEmpty(entities)) {
                returnType = entities[entities.length-1];
            }
        }
        return returnType;
    }

    protected Class<?> getMapFieldType(Serializable instance, FieldManager fieldManager, Property property, PersistenceOperationExecutor persistenceManager) {
        Class<?> returnType = null;
        Field field = fieldManager.getField(instance.getClass(), property.getName().substring(0, property.getName().indexOf(FieldManager.MAPFIELDSEPARATOR)));
        java.lang.reflect.Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Class<?> clazz = (Class<?>) pType.getActualTypeArguments()[1];
            Class<?>[] entities = persistenceManager.getDynamicEntityDao().getAllPolymorphicEntitiesFromCeiling(clazz);
            if (!ArrayUtils.isEmpty(entities)) {
                returnType = entities[entities.length-1];
            }
        }
        return returnType;
    }

    protected FieldInfo buildFieldInfo(Field field) {
        FieldInfo info = new FieldInfo();
        info.setName(field.getName());
        info.setGenericType(field.getGenericType());
        ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
        if (manyToMany != null) {
            info.setManyToManyMappedBy(manyToMany.mappedBy());
            info.setManyToManyTargetEntity(manyToMany.targetEntity().getName());
        }
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        if (oneToMany != null) {
            info.setOneToManyMappedBy(oneToMany.mappedBy());
            info.setOneToManyTargetEntity(oneToMany.targetEntity().getName());
        }
        return info;
    }
}

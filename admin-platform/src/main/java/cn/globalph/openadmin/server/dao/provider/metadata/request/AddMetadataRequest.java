package cn.globalph.openadmin.server.dao.provider.metadata.request;

import cn.globalph.openadmin.server.dao.DynamicEntityDao;

import java.lang.reflect.Field;

/**
 * Contains the requested field, metadata and support classes.
 *
 * @author felix.wu
 */
public class AddMetadataRequest {

    private final Field requestedField;
    private final Class<?> parentClass;
    private final Class<?> targetClass;
    private final DynamicEntityDao dynamicEntityDao;
    private final String prefix;

    public AddMetadataRequest(Field requestedField, Class<?> parentClass, Class<?> targetClass, DynamicEntityDao dynamicEntityDao, String prefix) {
        this.requestedField = requestedField;
        this.parentClass = parentClass;
        this.targetClass = targetClass;
        this.dynamicEntityDao = dynamicEntityDao;
        this.prefix = prefix;
    }

    public Field getRequestedField() {
        return requestedField;
    }

    public Class<?> getParentClass() {
        return parentClass;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public DynamicEntityDao getDynamicEntityDao() {
        return dynamicEntityDao;
    }

    public String getPrefix() {
        return prefix;
    }
}

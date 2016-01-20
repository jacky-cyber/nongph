package cn.globalph.openadmin.server.dao.provider.metadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

import org.apache.commons.lang.StringUtils;

import cn.globalph.common.EnumerationType;
import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.OptionFilterParamType;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.presentation.override.AdminPresentationMergeEntry;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.override.FieldMetadataOverride;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.dao.FieldInfo;

/**
 * @author Jeff Fischer
 */
public abstract class AbstractFieldMetadataProvider implements FieldMetadataProvider {

    protected Map<String, Map<String, FieldMetadataOverride>> metadataOverrides;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    @Resource(name="blMetadataOverrides")
    public void setMetadataOverrides(Map metadataOverrides) {
        try {
            this.metadataOverrides = metadataOverrides;
        } catch (Throwable e) {
            throw new IllegalArgumentException(
                    "Unable to assign metadataOverrides. You are likely using an obsolete spring application context " +
                    "configuration for this value. Please utilize the xmlns:mo=\"http://schema.broadleafcommerce.org/mo\" namespace " +
                    "and http://schema.broadleafcommerce.org/mo http://schema.broadleafcommerce.org/mo/mo.xsd schemaLocation " +
                    "in the xml schema config for your app context. This will allow you to use the appropriate <mo:override> element to configure your overrides.", e);
        }
    }

    protected void setClassOwnership(Class<?> parentClass, Class<?> targetClass, Map<String, FieldMetadata> attributes, FieldInfo field) {
        FieldMetadata metadata = attributes.get(field.getName());
        if (metadata != null) {
            AdminPresentationClass adminPresentationClass;
            if (parentClass != null) {
                metadata.setOwningClass(parentClass.getName());
                adminPresentationClass = parentClass.getAnnotation(AdminPresentationClass.class);
            } else {
                adminPresentationClass = targetClass.getAnnotation(AdminPresentationClass.class);
            }
            if (adminPresentationClass != null) {
                String friendlyName = adminPresentationClass.friendlyName();
                if (!StringUtils.isEmpty(friendlyName) && StringUtils.isEmpty(metadata.getOwningClassFriendlyName())) {
                    metadata.setOwningClassFriendlyName(friendlyName);
                }
            }
        }
    }

    protected FieldInfo buildFieldInfo(Field field) {
        FieldInfo info = new FieldInfo();
        info.setName( field.getName() );
        info.setGenericType( field.getGenericType() );
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
        MapKey mapKey = field.getAnnotation(MapKey.class);
        if (mapKey != null) {
            info.setMapKey(mapKey.name());
        }
        return info;
    }

    /**
     * @deprecated use the overloaded method that takes DynamicEntityDao as well. This version does not always properly detect the override from xml.
     * @param configurationKey
     * @param ceilingEntityFullyQualifiedClassname
     * @return override value
     */
    @Deprecated
    protected Map<String, FieldMetadataOverride> getTargetedOverride(String configurationKey, String ceilingEntityFullyQualifiedClassname) {
        if (metadataOverrides != null && (configurationKey != null || ceilingEntityFullyQualifiedClassname != null)) {
            return metadataOverrides.containsKey(configurationKey)?metadataOverrides.get(configurationKey):metadataOverrides.get(ceilingEntityFullyQualifiedClassname);
        }
        return null;
    }

    protected Map<String, FieldMetadataOverride> getTargetedOverride(DynamicEntityDao dynamicEntityDao, String configurationKey, String ceilingEntityFullyQualifiedClassname) {
        if (metadataOverrides != null && (configurationKey != null || ceilingEntityFullyQualifiedClassname != null)) {
            if (metadataOverrides.containsKey(configurationKey)) {
                return metadataOverrides.get(configurationKey);
            }
            if (metadataOverrides.containsKey(ceilingEntityFullyQualifiedClassname)) {
                return metadataOverrides.get(ceilingEntityFullyQualifiedClassname);
            }
            Class<?> test;
            try {
                test = Class.forName(ceilingEntityFullyQualifiedClassname);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (test.isInterface()) {
                //if it's an interface, get the least derive polymorphic concrete implementation
                Class<?>[] types = dynamicEntityDao.getAllPolymorphicEntitiesFromCeiling(test);
                return metadataOverrides.get(types[types.length-1].getName());
            } else {
                //if it's a concrete implementation, try the interfaces
                Class<?>[] types = test.getInterfaces();
                for (Class<?> type : types) {
                    if (metadataOverrides.containsKey(type.getName())) {
                        return metadataOverrides.get(type.getName());
                    }
                }
            }
        }
        return null;
    }

    protected Class<?> getBasicJavaType(SupportedFieldType fieldType) {
        Class<?> response;
        switch (fieldType) {
            case BOOLEAN:
                response = Boolean.TYPE;
                break;
            case DATE:
                response = Date.class;
                break;
            case DECIMAL:
                response = BigDecimal.class;
                break;
            case MONEY:
                response = BigDecimal.class;
                break;
            case INTEGER:
                response = Integer.TYPE;
                break;
            case UNKNOWN:
                response = null;
                break;
            default:
                response = String.class;
                break;
        }

        return response;
    }

    protected Object convertType(String value, OptionFilterParamType type) {
        Object response;
        switch (type) {
            case BIGDECIMAL:
               response = new BigDecimal(value);
               break;
            case BOOLEAN:
               response = Boolean.parseBoolean(value);
               break;
            case DOUBLE:
               response = Double.parseDouble(value);
               break;
            case FLOAT:
               response = Float.parseFloat(value);
               break;
            case INTEGER:
               response = Integer.parseInt(value);
               break;
            case LONG:
               response = Long.parseLong(value);
               break;
            default:
               response = value;
               break;
        }

        return response;
    }

    protected void setupBroadleafEnumeration(String broadleafEnumerationClass, BasicFieldMetadata fieldMetadata, DynamicEntityDao dynamicEntityDao) {
        try {
            
            Map<String, String> enumVals;
            Class<?> broadleafEnumeration = Class.forName(broadleafEnumerationClass);  

            Method typeMethod = broadleafEnumeration.getMethod("getType");
            Method friendlyTypeMethod = broadleafEnumeration.getMethod("getFriendlyType");
            Field types = dynamicEntityDao.getFieldManager().getField(broadleafEnumeration, "TYPES");
            
            if (Comparable.class.isAssignableFrom(broadleafEnumeration)) {
                enumVals = new LinkedHashMap<String, String>();
                Set<EnumerationType> blcEnumSet = new TreeSet<EnumerationType>();
                if (types != null) {
                    Map typesMap = (Map) types.get(null);
                    for (Object value : typesMap.values()) {
                        blcEnumSet.add((EnumerationType) value);
                    }

                    for (Object value : typesMap.values()) {
                        enumVals.put((String) friendlyTypeMethod.invoke(value), (String) typeMethod.invoke(value));
                    }
                }
            } else {
                enumVals = new TreeMap<String, String>();
                if (types != null) {
                    Map typesMap = (Map) types.get(null);
                    for (Object value : typesMap.values()) {
                        enumVals.put((String) friendlyTypeMethod.invoke(value), (String) typeMethod.invoke(value));
                    }
                } else {
                    Field[] fields = dynamicEntityDao.getAllFields(broadleafEnumeration);
                    for (Field field : fields) {
                        boolean isStatic = Modifier.isStatic(field.getModifiers());
                        if (isStatic && field.getType().isAssignableFrom(broadleafEnumeration)){
                            enumVals.put((String) friendlyTypeMethod.invoke(field.get(null)), (String) typeMethod.invoke(field.get(null)));
                        }
                    }
                }
            }
            
            
            String[][] enumerationValues = new String[enumVals.size()][2];
            int j = 0;
            for (String key : enumVals.keySet()) {
                enumerationValues[j][0] = enumVals.get(key);
                enumerationValues[j][1] = key;
                j++;
            }
            fieldMetadata.setEnumerationValues(enumerationValues);
            fieldMetadata.setEnumerationClass(broadleafEnumerationClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Map<String, AdminPresentationMergeEntry> getAdminPresentationEntries(AdminPresentationMergeEntry[] entries) {
        Map<String, AdminPresentationMergeEntry> response = new HashMap<String, AdminPresentationMergeEntry>();
        for (AdminPresentationMergeEntry entry : entries) {
            response.put(entry.propertyType(), entry);
        }
        return response;
    }
}

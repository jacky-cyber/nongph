package cn.globalph.cms.admin.server.handler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.cms.field.domain.FieldDefinition;
import cn.globalph.cms.field.domain.FieldGroup;
import cn.globalph.cms.structure.domain.StructuredContent;
import cn.globalph.cms.structure.domain.StructuredContentField;
import cn.globalph.cms.structure.domain.StructuredContentFieldImpl;
import cn.globalph.cms.structure.domain.StructuredContentType;
import cn.globalph.cms.structure.domain.StructuredContentTypeImpl;
import cn.globalph.cms.structure.service.StructuredContentService;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.ClassTree;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.service.ValidationException;
import cn.globalph.openadmin.server.service.handler.CustomPersistenceOperationHandlerAdapter;
import cn.globalph.openadmin.server.service.handler.DynamicEntityRetriever;
import cn.globalph.openadmin.server.service.persistence.module.InspectHelper;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

/**
 * Created by jfischer
 */
@Component("blStructuredContentTypeCustomPersistenceHandler")
public class StructuredContentTypeCustomPersistenceHandler extends CustomPersistenceOperationHandlerAdapter implements DynamicEntityRetriever {

    private final Log LOG = LogFactory.getLog(StructuredContentTypeCustomPersistenceHandler.class);

    @Resource(name="blStructuredContentService")
    protected StructuredContentService structuredContentService;
    
    @Resource(name = "blDynamicFieldPersistenceHandlerHelper")
    protected DynamicFieldPersistenceHandlerHelper dynamicFieldUtil;

    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        return
            StructuredContentType.class.getName().equals(ceilingEntityFullyQualifiedClassname) &&
            persistencePackage.getCustomCriteria() != null &&
            persistencePackage.getCustomCriteria().length > 0 &&
            persistencePackage.getCustomCriteria()[0].equals("constructForm");
    }

    @Override
    public Boolean canHandleAdd(PersistencePackage persistencePackage) {
        return canHandleFetch(persistencePackage);
    }

    @Override
    public Boolean canHandleInspect(PersistencePackage persistencePackage) {
        return canHandleFetch(persistencePackage);
    }

    @Override
    public Boolean canHandleRemove(PersistencePackage persistencePackage) {
        return false;
    }

    @Override
    public Boolean canHandleUpdate(PersistencePackage persistencePackage) {
        return canHandleFetch(persistencePackage);
    }

    @Override
    public DynamicResultSet inspect(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, InspectHelper helper) throws ServiceException {
        String ceilingEntityClassname = persistencePackage.getRootEntityClassname();
        try {
            String structuredContentTypeId = persistencePackage.getCustomCriteria()[3];
            StructuredContentType structuredContentType = structuredContentService.findStructuredContentTypeById(Long.valueOf(structuredContentTypeId));
            ClassMetadata metadata = new ClassMetadata();
            metadata.setClassName(StructuredContentType.class.getName());
            ClassTree entities = new ClassTree(StructuredContentTypeImpl.class.getName());
            metadata.setPolymorphicEntities(entities);
            Property[] properties = dynamicFieldUtil.buildDynamicPropertyList(structuredContentType.getStructuredContentFieldTemplate().getFieldGroups(), StructuredContentType.class);
            metadata.setProperties(properties);
            DynamicResultSet results = new DynamicResultSet(metadata);

            return results;
        } catch (Exception e) {
            throw new ServiceException("Unable to perform inspect for entity: "+ceilingEntityClassname, e);
        }
    }


    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        try {
            String structuredContentId = persistencePackage.getCustomCriteria()[1];
            Entity entity = fetchEntityBasedOnId(structuredContentId, null);
            DynamicResultSet results = new DynamicResultSet(new Entity[]{entity}, 1);

            return results;
        } catch (Exception e) {
            throw new ServiceException("Unable to perform fetch for entity: "+ceilingEntityFullyQualifiedClassname, e);
        }
    }

    @Override
    public Entity fetchEntityBasedOnId(String structuredContentId, List<String> dirtyFields) throws Exception {
        StructuredContent structuredContent = structuredContentService.findStructuredContentById(Long.valueOf(structuredContentId));
        return fetchDynamicEntity(structuredContent, dirtyFields, true);
    }

    @Override
    public String getFieldContainerClassName() {
        return StructuredContent.class.getName();
    }

    @Override
    public Entity fetchDynamicEntity(Serializable root, List<String> dirtyFields, boolean includeId) throws Exception {
        StructuredContent structuredContent = (StructuredContent) root;
        Map<String, StructuredContentField> structuredContentFieldMap = structuredContent.getStructuredContentFields();
        Entity entity = new Entity();
        entity.setType(new String[]{StructuredContentType.class.getName()});
        List<Property> propertiesList = new ArrayList<Property>();
        for (FieldGroup fieldGroup : structuredContent.getStructuredContentType().getStructuredContentFieldTemplate().getFieldGroups()) {
            for (FieldDefinition definition : fieldGroup.getFieldDefinitions()) {
                Property property = new Property();
                propertiesList.add(property);
                property.setName(definition.getName());
                String value = null;
                if (!MapUtils.isEmpty(structuredContentFieldMap)) {
                    StructuredContentField structuredContentField = structuredContentFieldMap.get(definition.getName());
                    if (structuredContentField != null) {
                        value = structuredContentField.getValue();
                    }
                }
                property.setValue(value);
                if (!CollectionUtils.isEmpty(dirtyFields) && dirtyFields.contains(property.getName())) {
                    property.setIsDirty(true);
                }
            }
        }
        if (includeId) {
            Property property = new Property();
            propertiesList.add(property);
            property.setName("id");
            property.setValue(String.valueOf(structuredContent.getId()));
        }

        entity.setProperties(propertiesList.toArray(new Property[]{}));

        return entity;
    }

    /**
     * Invoked when {@link StructuredContent} is saved in order to fill out the dynamic form for the structured content type
     */
    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        return addOrUpdate(persistencePackage, dynamicEntityDao, helper);
    }

    /**
     * Invoked when {@link StructuredContent} is saved in order to fill out the dynamic form for the structured content type
     */
    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        return addOrUpdate(persistencePackage, dynamicEntityDao, helper);
    }

    protected Entity addOrUpdate(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        try {
            String structuredContentId = persistencePackage.getCustomCriteria()[1];
            StructuredContent structuredContent = structuredContentService.findStructuredContentById(Long.valueOf(structuredContentId));
            
            Property[] properties = dynamicFieldUtil.buildDynamicPropertyList(structuredContent.getStructuredContentType().getStructuredContentFieldTemplate().getFieldGroups(), StructuredContentType.class);
            Map<String, FieldMetadata> md = new HashMap<String, FieldMetadata>();
            for (Property property : properties) {
                md.put(property.getName(), property.getFieldMetadata());
            }
            
            boolean validated = helper.validate(persistencePackage.getEntity(), null, md);
            if (!validated) {
                throw new ValidationException(persistencePackage.getEntity(), "Structured Content dynamic fields failed validation");
            }
            
            List<String> templateFieldNames = new ArrayList<String>(20);
            for (FieldGroup group : structuredContent.getStructuredContentType().getStructuredContentFieldTemplate().getFieldGroups()) {
                for (FieldDefinition definition: group.getFieldDefinitions()) {
                    templateFieldNames.add(definition.getName());
                }
            }
            Map<String, String> dirtyFieldsOrigVals = new HashMap<String, String>();
            List<String> dirtyFields = new ArrayList<String>();
            Map<String, StructuredContentField> structuredContentFieldMap = structuredContent.getStructuredContentFields();
            for (Property property : persistencePackage.getEntity().getProperties()) {
                if (templateFieldNames.contains(property.getName())) {
                    StructuredContentField structuredContentField = structuredContentFieldMap.get(property.getName());
                    if (structuredContentField != null) {
                        boolean isDirty = (structuredContentField.getValue() == null && property.getValue() != null) ||
                                (structuredContentField.getValue() != null && property.getValue() == null);
                        if (isDirty || (structuredContentField.getValue() != null && property.getValue() != null &&
                                !structuredContentField.getValue().trim().equals(property.getValue().trim()))) {
                            dirtyFields.add(property.getName());
                            dirtyFieldsOrigVals.put(property.getName(), structuredContentField.getValue());
                        }
                        structuredContentField.setValue(property.getValue());
                    } else {
                        structuredContentField = new StructuredContentFieldImpl();
                        structuredContentField.setFieldKey(property.getName());
                        structuredContentField.setValue(property.getValue());
                        dynamicEntityDao.persist(structuredContentField);
                        structuredContentFieldMap.put(property.getName(), structuredContentField);
                        dirtyFields.add(property.getName());
                    }
                }
            }
            List<String> removeItems = new ArrayList<String>();
            for (String key : structuredContentFieldMap.keySet()) {
                if (persistencePackage.getEntity().findProperty(key)==null) {
                    removeItems.add(key);
                }
            }
            if (removeItems.size() > 0) {
                for (String removeKey : removeItems) {
                    structuredContentFieldMap.remove(removeKey);
                }
            }

            Collections.sort(dirtyFields);
            Entity entity = fetchEntityBasedOnId(structuredContentId, dirtyFields);

            for (Entry<String, String> entry : dirtyFieldsOrigVals.entrySet()) {
                entity.getPMap().get(entry.getKey()).setOriginalValue(entry.getValue());
                entity.getPMap().get(entry.getKey()).setOriginalDisplayValue(entry.getValue());
            }

            return entity;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Unable to perform fetch for entity: "+ceilingEntityFullyQualifiedClassname, e);
        }
    }
}

package cn.globalph.cms.admin.server.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.cms.field.domain.FieldDefinition;
import cn.globalph.cms.field.domain.FieldGroup;
import cn.globalph.cms.page.domain.Page;
import cn.globalph.cms.page.domain.PageField;
import cn.globalph.cms.page.domain.PageFieldImpl;
import cn.globalph.cms.page.domain.PageTemplate;
import cn.globalph.cms.page.domain.PageTemplateImpl;
import cn.globalph.cms.page.service.PageService;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.sandbox.service.SandBoxService;
import cn.globalph.common.web.SandBoxContext;
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

/**
 * Created by jfischer
 */
@Component("blPageTemplateCustomPersistenceHandler")
public class PageTemplateCustomPersistenceHandler extends CustomPersistenceOperationHandlerAdapter implements DynamicEntityRetriever {

    private final Log LOG = LogFactory.getLog(PageTemplateCustomPersistenceHandler.class);

    @Resource(name="blPageService")
    protected PageService pageService;

    @Resource(name="blSandBoxService")
    protected SandBoxService sandBoxService;
    
    @Resource(name = "blDynamicFieldPersistenceHandlerHelper")
    protected DynamicFieldPersistenceHandlerHelper dynamicFieldUtil;

    @Override
    public Boolean canHandleFetch(PersistencePackage persistencePackage) {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        return
            PageTemplate.class.getName().equals(ceilingEntityFullyQualifiedClassname) &&
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

    protected SandBox getSandBox() {
        return sandBoxService.retrieveSandBoxById(SandBoxContext.getSandBoxContext().getSandBoxId());
    }

    @Override
    public DynamicResultSet inspect(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, InspectHelper helper) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        try {
            String pageTemplateId = persistencePackage.getCustomCriteria()[3];
            PageTemplate template = pageService.findPageTemplateById(Long.valueOf(pageTemplateId));
            ClassMetadata metadata = new ClassMetadata();
            metadata.setClassName(PageTemplate.class.getName());
            ClassTree entities = new ClassTree(PageTemplateImpl.class.getName());
            metadata.setPolymorphicEntities(entities);
            Property[] properties = dynamicFieldUtil.buildDynamicPropertyList(template.getFieldGroups(), PageTemplate.class);
            metadata.setProperties(properties);
            DynamicResultSet results = new DynamicResultSet(metadata);

            return results;
        } catch (Exception e) {
            throw new ServiceException("Unable to perform inspect for entity: "+ceilingEntityFullyQualifiedClassname, e);
        }
    }

    @Override
    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        try {
            String pageId = persistencePackage.getCustomCriteria()[1];
            Entity entity = fetchEntityBasedOnId(pageId, null);
            DynamicResultSet results = new DynamicResultSet(new Entity[]{entity}, 1);

            return results;
        } catch (Exception e) {
            throw new ServiceException("Unable to perform fetch for entity: "+ceilingEntityFullyQualifiedClassname, e);
        }
    }

    @Override
    public String getFieldContainerClassName() {
        return Page.class.getName();
    }

    @Override
    public Entity fetchEntityBasedOnId(String pageId, List<String> dirtyFields) throws Exception {
        Page page = pageService.findPageById(Long.valueOf(pageId));
        return fetchDynamicEntity(page, dirtyFields, true);
    }

    @Override
    public Entity fetchDynamicEntity(Serializable root, List<String> dirtyFields, boolean includeId) throws Exception {
        Page page = (Page) root;
        Map<String, PageField> pageFieldMap = page.getPageFields();
        Entity entity = new Entity();
        entity.setType(new String[]{PageTemplateImpl.class.getName()});
        List<Property> propertiesList = new ArrayList<Property>();
        for (FieldGroup fieldGroup : page.getPageTemplate().getFieldGroups()) {
            for (FieldDefinition definition : fieldGroup.getFieldDefinitions()) {
                Property property = new Property();
                propertiesList.add(property);
                property.setName(definition.getName());
                String value = null;
                if (!MapUtils.isEmpty(pageFieldMap)) {
                    PageField pageField = pageFieldMap.get(definition.getName());
                    if (pageField == null) {
                        value = "";
                    } else {
                        value = pageField.getValue();
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
            property.setValue(String.valueOf(page.getId()));
        }

        entity.setProperties(propertiesList.toArray(new Property[]{}));

        return entity;
    }

    @Override
    public Entity update(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        return addOrUpdate(persistencePackage, dynamicEntityDao, helper);
    }

    @Override
    public Entity add(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        return addOrUpdate(persistencePackage, dynamicEntityDao, helper);
    }

    protected Entity addOrUpdate(PersistencePackage persistencePackage, DynamicEntityDao dynamicEntityDao, RecordHelper helper) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getRootEntityClassname();
        try {
            String pageId = persistencePackage.getCustomCriteria()[1];
            Page page = pageService.findPageById(Long.valueOf(pageId));

            Property[] properties = dynamicFieldUtil.buildDynamicPropertyList(page.getPageTemplate().getFieldGroups(), PageTemplate.class);
            Map<String, FieldMetadata> md = new HashMap<String, FieldMetadata>();
            for (Property property : properties) {
                md.put(property.getName(), property.getFieldMetadata());
            }
            
            boolean validated = helper.validate(persistencePackage.getEntity(), null, md);
            if (!validated) {
                throw new ValidationException(persistencePackage.getEntity(), "Page dynamic fields failed validation");
            }

            List<String> templateFieldNames = new ArrayList<String>(20);
            for (FieldGroup group : page.getPageTemplate().getFieldGroups()) {
                for (FieldDefinition definition: group.getFieldDefinitions()) {
                    templateFieldNames.add(definition.getName());
                }
            }
            Map<String, String> dirtyFieldsOrigVals = new HashMap<String, String>();
            List<String> dirtyFields = new ArrayList<String>();
            Map<String, PageField> pageFieldMap = page.getPageFields();
            for (Property property : persistencePackage.getEntity().getProperties()) {
                if (templateFieldNames.contains(property.getName())) {
                    PageField pageField = pageFieldMap.get(property.getName());
                    if (pageField != null) {
                        boolean isDirty = (pageField.getValue() == null && property.getValue() != null) ||
                                (pageField.getValue() != null && property.getValue() == null);
                        if (isDirty || (pageField.getValue() != null && property.getValue() != null &&
                                !pageField.getValue().trim().equals(property.getValue().trim()))) {
                            dirtyFields.add(property.getName());
                            dirtyFieldsOrigVals.put(property.getName(), pageField.getValue());
                        }
                        pageField.setValue(property.getValue());
                    } else {
                        pageField = new PageFieldImpl();
                        pageField.setFieldKey(property.getName());
                        pageField.setValue(property.getValue());
                        dynamicEntityDao.persist(pageField);
                        pageFieldMap.put(property.getName(), pageField);
                        dirtyFields.add(property.getName());
                    }
                }
            }
            List<String> removeItems = new ArrayList<String>();
            for (String key : pageFieldMap.keySet()) {
                if (persistencePackage.getEntity().findProperty(key)==null) {
                    removeItems.add(key);
                }
            }
            if (removeItems.size() > 0) {
                for (String removeKey : removeItems) {
                    pageFieldMap.remove(removeKey);
                }
            }

            Collections.sort(dirtyFields);
            Entity entity = fetchEntityBasedOnId(pageId, dirtyFields);

            for (Map.Entry<String, String> entry : dirtyFieldsOrigVals.entrySet()) {
                entity.getPMap().get(entry.getKey()).setOriginalValue(entry.getValue());
                entity.getPMap().get(entry.getKey()).setOriginalDisplayValue(entry.getValue());
            }

            return entity;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Unable to perform update for entity: "+ceilingEntityFullyQualifiedClassname, e);
        }
    }
}

package cn.globalph.openadmin.server.service;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.persistence.EntityConfiguration;
import cn.globalph.common.presentation.client.AddMethodType;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.common.util.dao.DynamicDaoHelper;
import cn.globalph.common.util.dao.DynamicDaoHelperImpl;
import cn.globalph.openadmin.dto.AdornedTargetCollectionMetadata;
import cn.globalph.openadmin.dto.AdornedTargetList;
import cn.globalph.openadmin.dto.BasicCollectionMetadata;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.CollectionMetadata;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.FilterAndSortCriteria;
import cn.globalph.openadmin.dto.MapMetadata;
import cn.globalph.openadmin.dto.MapStructure;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.dto.PersistencePackageRequest;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.dto.SectionCrumb;
import cn.globalph.openadmin.server.factory.PersistencePackageFactory;
import cn.globalph.openadmin.server.service.persistence.PersistenceResponse;
import cn.globalph.openadmin.web.form.entity.DynamicEntityFormInfo;
import cn.globalph.openadmin.web.form.entity.EntityForm;
import cn.globalph.openadmin.web.form.entity.Field;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * 通用实体管理服务实现
 * @author felix.wu
 */
@Service("blAdminEntityService")
public class EntityAdminServiceImpl implements EntityAdminService {

    @Resource(name = "blDynamicEntityRemoteService")
    protected DynamicEntityService dynamicEntityService;

    @Resource(name = "blPersistencePackageFactory")
    protected PersistencePackageFactory persistencePackageFactory;

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @Resource(name = "blEntityConfiguration")
    protected EntityConfiguration entityConfiguration;

    protected DynamicDaoHelper dynamicDaoHelper = new DynamicDaoHelperImpl();

    @Override
    public PersistenceResponse getPersistenceResponse(PersistencePackageRequest request)
            throws ServiceException {
        PersistenceResponse response = inspect(request);
        ClassMetadata cmd = response.getDynamicResultSet().getClassMetaData();
        cmd.setClassName(request.getRootEntityClassname());
        cmd.setSecurityCeilingType(request.getSecurityRootEntityClassname());
        return response;
    }

    @Override
    public PersistenceResponse getRecords(PersistencePackageRequest request) throws ServiceException {
        return fetch(request);
    }

    @Override
    public PersistenceResponse getRecord(PersistencePackageRequest request, String id, ClassMetadata cmd, boolean isCollectionRequest)
            throws ServiceException {
        String idProperty = cmd.getIdProperty();
        
        FilterAndSortCriteria fasc = new FilterAndSortCriteria(idProperty);
        fasc.setFilterValue(id);
        request.addFilterAndSortCriteria(fasc);

        PersistenceResponse response = fetch(request);
        Entity[] entities = response.getDynamicResultSet().getRecords();
        Assert.isTrue(entities != null && entities.length == 1, "Entity not found");

        return response;
    }

    @Override
    public PersistenceResponse addEntity(EntityForm entityForm, String[] customCriteria, List<SectionCrumb> sectionCrumb) throws ServiceException {
        PersistencePackageRequest ppr = getRequestForEntityForm(entityForm, customCriteria, sectionCrumb);
        // If the entity form has dynamic forms inside of it, we need to persist those as well.
        // They are typically done in their own custom persistence handlers, which will get triggered
        // based on the criteria specific in the PersistencePackage.
        for (Entry<String, EntityForm> entry : entityForm.getDynamicForms().entrySet()) {
            DynamicEntityFormInfo info = entityForm.getDynamicFormInfo(entry.getKey());

            if (info.getCustomCriteriaOverride() != null) {
                customCriteria = info.getCustomCriteriaOverride();
            } else {
                String propertyName = info.getPropertyName();
                String propertyValue = entityForm.getFields().get(propertyName).getValue();
                customCriteria = new String[] {info.getCriteriaName(), entityForm.getId(), propertyName, propertyValue};
            }

            PersistencePackageRequest subRequest = getRequestForEntityForm(entry.getValue(), customCriteria, sectionCrumb);
            ppr.addSubRequest(info.getPropertyName(), subRequest);
        }
        return add(ppr);
    }

    @Override
    public PersistenceResponse updateEntity(EntityForm entityForm, String[] customCriteria, List<SectionCrumb> sectionCrumb) throws ServiceException {
        PersistencePackageRequest ppr = getRequestForEntityForm(entityForm, customCriteria, sectionCrumb);
        ppr.setRequestingEntityName(entityForm.getMainEntityName());
        // If the entity form has dynamic forms inside of it, we need to persist those as well.
        // They are typically done in their own custom persistence handlers, which will get triggered
        // based on the criteria specific in the PersistencePackage.
        for (Entry<String, EntityForm> entry : entityForm.getDynamicForms().entrySet()) {
            DynamicEntityFormInfo info = entityForm.getDynamicFormInfo(entry.getKey());

            if (info.getCustomCriteriaOverride() != null) {
                customCriteria = info.getCustomCriteriaOverride();
            } else {
                String propertyName = info.getPropertyName();
                String propertyValue = entityForm.getFields().get(propertyName).getValue();
                customCriteria = new String[] { info.getCriteriaName(), entityForm.getId(), propertyName, propertyValue };
            }

            PersistencePackageRequest subRequest = getRequestForEntityForm(entry.getValue(), customCriteria, sectionCrumb);
            subRequest.withSecurityCeilingEntityClassname(info.getSecurityCeilingClassName());
            ppr.addSubRequest(info.getPropertyName(), subRequest);
        }
        return update(ppr);
    }

    @Override
    public PersistenceResponse removeEntity(EntityForm entityForm, String[] customCriteria, List<SectionCrumb> sectionCrumb)
            throws ServiceException {
        PersistencePackageRequest ppr = getRequestForEntityForm(entityForm, customCriteria, sectionCrumb);
        return remove(ppr);
    }
    
    protected List<Property> getPropertiesFromEntityForm(EntityForm entityForm) {
        List<Property> properties = new ArrayList<Property>(entityForm.getFields().size());
        
        for (Entry<String, Field> entry : entityForm.getFields().entrySet()) {
            Property p = new Property();
            p.setName(entry.getKey());
            p.setValue(entry.getValue().getValue());
            p.setDisplayValue(entry.getValue().getDisplayValue());
            p.setIsDirty(entry.getValue().getIsDirty());
            properties.add(p);
        }
        
        return properties;
    }

    protected PersistencePackageRequest getRequestForEntityForm(EntityForm entityForm, String[] customCriteria, List<SectionCrumb> sectionCrumbs) {
        // Ensure the ID property is on the form
        Field idField = entityForm.getFields().get(entityForm.getIdProperty());
        if (idField == null) {
            idField = new Field();
            idField.setName(entityForm.getIdProperty());
            idField.setValue(entityForm.getId());
            entityForm.getFields().put(entityForm.getIdProperty(), idField);
        } else {
            idField.setValue(entityForm.getId());
        }

        List<Property> propList = getPropertiesFromEntityForm(entityForm);
        Property[] properties = new Property[propList.size()];
        properties = propList.toArray(properties);

        Entity entity = new Entity();
        entity.setProperties(properties);
        String entityType = entityForm.getEntityType();
        if (StringUtils.isEmpty(entityType)) {
            entityType = entityForm.getRootEntityClassname();
        }
        entity.setType(new String[] { entityType });

        PersistencePackageRequest ppr = PersistencePackageRequest.standard()
                .withEntity(entity)
                .withCustomCriteria(customCriteria)
                .withRootEntityClassname(entityForm.getRootEntityClassname())
                .withSectionCrumbs(sectionCrumbs)
                .withRequestingEntityName(entityForm.getMainEntityName());
        return ppr;
    }

    @Override
    public PersistenceResponse getAdvancedCollectionRecord(ClassMetadata containingClassMetadata, Entity containingEntity,
            Property collectionProperty, String collectionItemId, List<SectionCrumb> sectionCrumbs)
            throws ServiceException {
        PersistencePackageRequest ppr = PersistencePackageRequest.fromMetadata(collectionProperty.getFieldMetadata(), sectionCrumbs);

        FieldMetadata md = collectionProperty.getFieldMetadata();
        String containingEntityId = getContextSpecificRelationshipId(containingClassMetadata, containingEntity, 
                collectionProperty.getName());
        ppr.setSectionEntityField(collectionProperty.getName());

        PersistenceResponse response;

        if (md instanceof AdornedTargetCollectionMetadata) {
            FilterAndSortCriteria fasc = new FilterAndSortCriteria(ppr.getAdornedList().getCollectionFieldName());
            fasc.setFilterValue(containingEntityId);
            ppr.addFilterAndSortCriteria(fasc);

            fasc = new FilterAndSortCriteria(ppr.getAdornedList().getCollectionFieldName() + "Target");
            fasc.setFilterValue(collectionItemId);
            ppr.addFilterAndSortCriteria(fasc);

            response = fetch(ppr);
            Entity[] entities = response.getDynamicResultSet().getRecords();
            Assert.isTrue(entities != null && entities.length == 1, "Entity not found");
        } else if (md instanceof MapMetadata) {
            MapMetadata mmd = (MapMetadata) md;
            FilterAndSortCriteria fasc = new FilterAndSortCriteria(ppr.getForeignKey().getManyToField());
            fasc.setFilterValue(containingEntityId);
            ppr.addFilterAndSortCriteria(fasc);

            response = fetch(ppr);
            Entity[] entities = response.getDynamicResultSet().getRecords();
            for (Entity e : entities) {
                String idProperty = containingClassMetadata.getIdProperty();
                if (mmd.isSimpleValue()) {
                    idProperty = "key";
                }
                Property p = e.getPMap().get(idProperty);
                if (p.getValue().equals(collectionItemId)) {
                    response.setEntity(e);
                    break;
                }
            }
        } else {
            throw new IllegalArgumentException(String.format("The specified field [%s] for class [%s] was not an " +
                    "advanced collection field.", collectionProperty.getName(), containingClassMetadata.getClassName()));
        }

        if (response == null) {
            throw new NoResultException(String.format("Could not find record for class [%s], field [%s], main entity id " +
                    "[%s], collection entity id [%s]", containingClassMetadata.getClassName(),
                    collectionProperty.getName(), containingEntityId, collectionItemId));
        }

        return response;
    }

    @Override
    public PersistenceResponse getRecordsForCollection(ClassMetadata containingClassMetadata, Entity containingEntity,
            Property collectionProperty, FilterAndSortCriteria[] fascs, Integer startIndex, Integer maxIndex, List<SectionCrumb> sectionCrumb)
            throws ServiceException {
        return getRecordsForCollection(containingClassMetadata, containingEntity, collectionProperty, fascs, startIndex, 
                maxIndex, null, sectionCrumb);
    }
    
    @Override
    public PersistenceResponse getRecordsForCollection(ClassMetadata containingClassMetadata, Entity containingEntity,
            Property collectionProperty, FilterAndSortCriteria[] fascs, Integer startIndex, Integer maxIndex,
            String idValueOverride, List<SectionCrumb> sectionCrumbs) throws ServiceException {
        
        PersistencePackageRequest ppr = PersistencePackageRequest.fromMetadata(collectionProperty.getFieldMetadata(), sectionCrumbs)
                .withFilterAndSortCriteria(fascs)
                .withStartIndex(startIndex)
                .withMaxIndex(maxIndex);
        
        FilterAndSortCriteria fasc;

        FieldMetadata md = collectionProperty.getFieldMetadata();
        String collectionCeilingClass = null;

        if (md instanceof BasicCollectionMetadata) {
            fasc = new FilterAndSortCriteria(ppr.getForeignKey().getManyToField());
            collectionCeilingClass = ((CollectionMetadata) md).getCollectionRootEntity();
        } else if (md instanceof AdornedTargetCollectionMetadata) {
            fasc = new FilterAndSortCriteria(ppr.getAdornedList().getCollectionFieldName());
            collectionCeilingClass = ((CollectionMetadata) md).getCollectionRootEntity();
        } else if (md instanceof MapMetadata) {
            fasc = new FilterAndSortCriteria(ppr.getForeignKey().getManyToField());
        } else {
            throw new IllegalArgumentException(String.format("The specified field [%s] for class [%s] was not a " +
                    "collection field.", collectionProperty.getName(), containingClassMetadata.getClassName()));
        }

        String id;
        if (idValueOverride == null) {
            id = getContextSpecificRelationshipId(containingClassMetadata, containingEntity, collectionProperty.getName());
        } else {
            id = idValueOverride;
        }
        fasc.setFilterValue(id);
        ppr.addFilterAndSortCriteria(fasc);

        if (collectionCeilingClass != null) {
            ppr.setRootEntityClassname(collectionCeilingClass);
        }
        ppr.setSectionEntityField(collectionProperty.getName());

        return fetch(ppr);
    }

    @Override
    public Map<String, DynamicResultSet> getRecordsForAllSubCollections(PersistencePackageRequest ppr, Entity containingEntity, List<SectionCrumb> sectionCrumb)
            throws ServiceException {
        Map<String, DynamicResultSet> map = new HashMap<String, DynamicResultSet>();

        PersistenceResponse response = getPersistenceResponse(ppr);
        ClassMetadata cmd = response.getDynamicResultSet().getClassMetaData();
        for (Property p : cmd.getProperties()) {
            if (p.getFieldMetadata() instanceof CollectionMetadata) {
                PersistenceResponse response2 = getRecordsForCollection(cmd, containingEntity, p, null, null, null, sectionCrumb);
                map.put(p.getName(), response2.getDynamicResultSet());
            }
        }

        return map;
    }

    @Override
    public PersistenceResponse addSubCollectionEntity(EntityForm entityForm, ClassMetadata mainMetadata, Property field,
            Entity parentEntity, List<SectionCrumb> sectionCrumbs)
            throws ServiceException, ClassNotFoundException {
        // Assemble the properties from the entity form
        List<Property> properties = new ArrayList<Property>();
        for (Entry<String, Field> entry : entityForm.getFields().entrySet()) {
            Property p = new Property();
            p.setName(entry.getKey());
            p.setValue(entry.getValue().getValue());
            properties.add(p);
        }

        FieldMetadata md = field.getFieldMetadata();

        PersistencePackageRequest ppr = PersistencePackageRequest.fromMetadata(md, sectionCrumbs)
                .withEntity(new Entity());

        if (md instanceof BasicCollectionMetadata) {
            BasicCollectionMetadata fmd = (BasicCollectionMetadata) md;
            ppr.getEntity().setType(new String[] { entityForm.getEntityType() });
            
            // If we're looking up an entity instead of trying to create one on the fly, let's make sure 
            // that we're not changing the target entity at all and only creating the association to the id
            if (fmd.getAddMethodType().equals(AddMethodType.LOOKUP)) {
                List<String> fieldsToRemove = new ArrayList<String>();
                
                String idProp = mainMetadata.getIdProperty();
                for (String key : entityForm.getFields().keySet()) {
                    if (!idProp.equals(key)) {
                        fieldsToRemove.add(key);
                    }
                }
                
                for (String key : fieldsToRemove) {
                    ListIterator<Property> li = properties.listIterator();
                    while (li.hasNext()) {
                        if (li.next().getName().equals(key)) {
                            li.remove();
                        }
                    }
                }
                
                ppr.setValidateUnsubmittedProperties(false);
            }

            Property fp = new Property();
            fp.setName(ppr.getForeignKey().getManyToField());
            fp.setValue(getContextSpecificRelationshipId(mainMetadata, parentEntity, field.getName()));
            properties.add(fp);
        } else if (md instanceof AdornedTargetCollectionMetadata) {
            ppr.getEntity().setType(new String[] { ppr.getAdornedList().getAdornedTargetEntityClassname() });
            
            String[] maintainedFields = ((AdornedTargetCollectionMetadata) md).getMaintainedAdornedTargetFields();
            if (maintainedFields == null || maintainedFields.length == 0) {
                ppr.setValidateUnsubmittedProperties(false);
            }
        } else if (md instanceof MapMetadata) {
            ppr.getEntity().setType(new String[] { entityForm.getEntityType() });
            
            Property p = new Property();
            p.setName("symbolicId");
            p.setValue(getContextSpecificRelationshipId(mainMetadata, parentEntity, field.getName()));
            properties.add(p);
        } else {
            throw new IllegalArgumentException(String.format("The specified field [%s] for class [%s] was" +
                    " not a collection field.", field.getName(), mainMetadata.getClassName()));
        }

        ppr.setRootEntityClassname(ppr.getEntity().getType()[0]);
        String sectionField = field.getName();
        if (sectionField.contains(".")) {
            sectionField = sectionField.substring(0, sectionField.lastIndexOf("."));
        }
        ppr.setSectionEntityField(sectionField);
        
        Property parentNameProp = parentEntity.getPMap().get(AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY);
        if (parentNameProp != null) {
            ppr.setRequestingEntityName(parentNameProp.getValue());
        }

        Property[] propArr = new Property[properties.size()];
        properties.toArray(propArr);
        ppr.getEntity().setProperties(propArr);

        return add(ppr);
    }

    @Override
    public PersistenceResponse updateSubCollectionEntity(EntityForm entityForm, ClassMetadata mainMetadata, Property field,
            Entity parentEntity, String collectionItemId, List<SectionCrumb> sectionCrumbs)
            throws ServiceException, ClassNotFoundException {
        List<Property> properties = getPropertiesFromEntityForm(entityForm);

        FieldMetadata md = field.getFieldMetadata();

        PersistencePackageRequest ppr = PersistencePackageRequest.fromMetadata(md, sectionCrumbs)
                .withEntity(new Entity());

        if (md instanceof BasicCollectionMetadata) {
            BasicCollectionMetadata fmd = (BasicCollectionMetadata) md;
            ppr.getEntity().setType(new String[] { fmd.getCollectionRootEntity() });

            Property fp = new Property();
            fp.setName(ppr.getForeignKey().getManyToField());
            fp.setValue(getContextSpecificRelationshipId(mainMetadata, parentEntity, field.getName()));
            properties.add(fp);
        } else if (md instanceof AdornedTargetCollectionMetadata) {
            ppr.getEntity().setType(new String[] { ppr.getAdornedList().getAdornedTargetEntityClassname() });
            for (Property property : properties) {
                if (property.getName().equals(ppr.getAdornedList().getLinkedObjectPath() +
                                    "." + ppr.getAdornedList().getLinkedIdProperty())) {
                    break;
                }
            }
        } else if (md instanceof MapMetadata) {
            ppr.getEntity().setType(new String[] { entityForm.getEntityType() });
            
            Property p = new Property();
            p.setName("symbolicId");
            p.setValue(getContextSpecificRelationshipId(mainMetadata, parentEntity, field.getName()));
            properties.add(p);
        } else {
            throw new IllegalArgumentException(String.format("The specified field [%s] for class [%s] was" +
                    " not a collection field.", field.getName(), mainMetadata.getClassName()));
        }

        ppr.setRootEntityClassname(ppr.getEntity().getType()[0]);
        String sectionField = field.getName();
        if (sectionField.contains(".")) {
            sectionField = sectionField.substring(0, sectionField.lastIndexOf("."));
        }
        ppr.setSectionEntityField(sectionField);
        
        Property parentNameProp = parentEntity.getPMap().get(AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY);
        if (parentNameProp != null) {
            ppr.setRequestingEntityName(parentNameProp.getValue());
        }

        Property p = new Property();
        p.setName(entityForm.getIdProperty());
        p.setValue(collectionItemId);
        if (!properties.contains(p)) {
            properties.add(p);
        }

        Property[] propArr = new Property[properties.size()];
        properties.toArray(propArr);
        ppr.getEntity().setProperties(propArr);

        return update(ppr);
    }

    @Override
    public PersistenceResponse removeSubCollectionEntity(ClassMetadata mainMetadata, Property field, Entity parentEntity, String itemId,
            String priorKey, List<SectionCrumb> sectionCrumbs)
            throws ServiceException {
        List<Property> properties = new ArrayList<Property>();

        Property p;
        String parentId = getContextSpecificRelationshipId(mainMetadata, parentEntity, field.getName());

        Entity entity = new Entity();
        PersistencePackageRequest ppr = PersistencePackageRequest.fromMetadata(field.getFieldMetadata(), sectionCrumbs)
                .withEntity(entity);

        if (field.getFieldMetadata() instanceof BasicCollectionMetadata) {
            BasicCollectionMetadata fmd = (BasicCollectionMetadata) field.getFieldMetadata();

            p = new Property();
            p.setName("id");
            p.setValue(itemId);
            properties.add(p);

            p = new Property();
            p.setName(ppr.getForeignKey().getManyToField());
            p.setValue(parentId);
            properties.add(p);

            entity.setType(new String[] { fmd.getCollectionRootEntity() });
        } else if (field.getFieldMetadata() instanceof AdornedTargetCollectionMetadata) {
            AdornedTargetList adornedList = ppr.getAdornedList();

            p = new Property();
            p.setName(adornedList.getLinkedObjectPath() + "." + adornedList.getLinkedIdProperty());
            p.setValue(parentId);
            properties.add(p);

            p = new Property();
            p.setName(adornedList.getTargetObjectPath() + "." + adornedList.getTargetIdProperty());
            p.setValue(itemId);
            properties.add(p);

            entity.setType(new String[] { adornedList.getAdornedTargetEntityClassname() });
        } else if (field.getFieldMetadata() instanceof MapMetadata) {
            MapMetadata fmd = (MapMetadata) field.getFieldMetadata();

            p = new Property();
            p.setName("symbolicId");
            p.setValue(getContextSpecificRelationshipId(mainMetadata, parentEntity, field.getName()));
            properties.add(p);

            p = new Property();
            p.setName("priorKey");
            p.setValue(priorKey);
            properties.add(p);
            
            MapStructure mapStructure = ppr.getMapStructure();
            
            p = new Property();
            p.setName(mapStructure.getKeyPropertyName());
            p.setValue(itemId);
            properties.add(p);

            entity.setType(new String[] { fmd.getTargetClass() });
        }

        String sectionField = field.getName();
        if (sectionField.contains(".")) {
            sectionField = sectionField.substring(0, sectionField.lastIndexOf("."));
        }
        ppr.setSectionEntityField(sectionField);
        
        Property parentNameProp = parentEntity.getPMap().get(AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY);
        if (parentNameProp != null) {
            ppr.setRequestingEntityName(parentNameProp.getValue());
        }

        Property[] propArr = new Property[properties.size()];
        properties.toArray(propArr);
        ppr.getEntity().setProperties(propArr);

        return remove(ppr);
    }

    @Override
    public String getContextSpecificRelationshipId(ClassMetadata cmd, Entity entity, String propertyName) {
        String prefix;
        if (propertyName.contains(".")) {
            prefix = propertyName.substring(0, propertyName.lastIndexOf("."));
        } else {
            prefix = "";
        }
                
        if (prefix.equals("")) {
            return entity.findProperty("id").getValue();
        } else {
            //we need to check all the parts of the prefix. For example, the prefix could include an @Embedded class like
            //defaultSku.dimension. In this case, we want the id from the defaultSku property, since the @Embedded does
            //not have an id property - nor should it.
            String[] prefixParts = prefix.split("\\.");
            for (int j = 0; j < prefixParts.length; j++) {
                StringBuilder sb = new StringBuilder();
                for (int x = 0; x < prefixParts.length - j; x++) {
                    sb.append(prefixParts[x]);
                    if (x < prefixParts.length - j - 1) {
                        sb.append(".");
                    }
                }
                String tempPrefix = sb.toString();
                
                for (Property property : entity.getProperties()) {
                    if (property.getName().startsWith(tempPrefix)) {
                        if (cmd.getPMap().containsKey(property.getName())) {
                            BasicFieldMetadata md = (BasicFieldMetadata) cmd.getPMap().get(property.getName()).getFieldMetadata();
                            if (md.getFieldType().equals(SupportedFieldType.ID)) {
                                return property.getValue();
                            }
                        }
                    }
                }
            }
        }
        if (!prefix.contains(".")) {
            //this may be an embedded class directly on the root entity (e.g. embeddablePriceList.restrictedPriceLists on OfferImpl)
            return entity.findProperty("id").getValue();
        }
        throw new RuntimeException("Unable to establish a relationship id");
    }
    
    protected PersistenceResponse add(PersistencePackageRequest request)
            throws ServiceException {
        PersistencePackage pkg = persistencePackageFactory.create(request);
        try {
            return dynamicEntityService.add(pkg);
        } catch (ValidationException e) {
            return new PersistenceResponse().withEntity(e.getEntity());
        }
    }

    protected PersistenceResponse update(PersistencePackageRequest request)
            throws ServiceException {
        PersistencePackage pkg = persistencePackageFactory.create(request);
        try {
            return dynamicEntityService.update(pkg);
        } catch (ValidationException e) {
            return new PersistenceResponse().withEntity(e.getEntity());
        }
    }

    protected PersistenceResponse inspect(PersistencePackageRequest request)
            throws ServiceException {
        PersistencePackage pkg = persistencePackageFactory.create(request);
        return dynamicEntityService.inspect(pkg);
    }

    protected PersistenceResponse remove(PersistencePackageRequest request)
            throws ServiceException {
        PersistencePackage pkg = persistencePackageFactory.create(request);
        return dynamicEntityService.remove(pkg);
    }

    protected PersistenceResponse fetch(PersistencePackageRequest request)
            throws ServiceException {
        PersistencePackage pkg = persistencePackageFactory.create(request);

        CriteriaTransferObject cto = getDefaultCto();
        if(request.getFilterAndSortCriteria() != null) {
            cto.addAll(Arrays.asList(request.getFilterAndSortCriteria()));
        }
        
        if (request.getStartIndex() == null) {
            cto.setFirstResult(0);
        } else {
            cto.setFirstResult(request.getStartIndex());
        }
        
        if (request.getMaxIndex() != null) {
            int requestedMaxResults = request.getMaxIndex() - request.getStartIndex() + 1;
            if (requestedMaxResults >= 0 && requestedMaxResults < cto.getMaxResults()) {
                cto.setMaxResults(requestedMaxResults);
            }
        }
        
        return dynamicEntityService.fetch(pkg, cto);
     }
    
    protected CriteriaTransferObject getDefaultCto() {
        CriteriaTransferObject cto = new CriteriaTransferObject();
        cto.setMaxResults(50);
        return cto;
    }

}

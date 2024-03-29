package cn.globalph.openadmin.server.service;

import java.util.List;
import java.util.Map;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FilterAndSortCriteria;
import cn.globalph.openadmin.dto.PersistencePackageRequest;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.dto.SectionCrumb;
import cn.globalph.openadmin.server.service.persistence.PersistenceResponse;
import cn.globalph.openadmin.web.form.entity.EntityForm;

/**
 * 通用实体管理服务
 * @author felix.wu
 */
public interface EntityAdminService {

    /**
     * Returns class metadata for the given request object
     * 
     * @param request
     * @return ClassMetadata for the given request
     * @throws ServiceException
     */
    public PersistenceResponse getPersistenceResponse(PersistencePackageRequest request)
            throws ServiceException;

    /**
     * Returns the DynamicResultSet containing the total records for the query and the currently fetched Entity[]
     * 
     * @param request
     * @return DynamicResultSet 
     * @throws ServiceException
     */
    public PersistenceResponse getRecords(PersistencePackageRequest request)
            throws ServiceException;

    /**
     * Returns a specific record for the given request and primary key id/property
     * 
     * @param request
     * @param id
     * @param cmd
     * @param isCollectionRequest whether or not this record request was initiated from a collection on a parent entity
     * @return the Entity
     * @throws ServiceException
     */
    public PersistenceResponse getRecord(PersistencePackageRequest request, String id, ClassMetadata cmd, boolean isCollectionRequest)
            throws ServiceException;

    /**
     * Persists the given entity
     * 
     * @param entityForm
     * @param customCriteria
     * @return the persisted Entity
     * @throws ServiceException
     */
    public PersistenceResponse addEntity(EntityForm entityForm, String[] customCriteria, List<SectionCrumb> sectionCrumb)
            throws ServiceException;

    /**
     * Updates the given entity
     * 
     * @param entityForm
     * @param customCriteria
     * @return the persisted Entity
     * @throws ServiceException
     */
    public PersistenceResponse updateEntity(EntityForm entityForm, String[] customCriteria, List<SectionCrumb> sectionCrumb)
            throws ServiceException;

    /**
     * Removes the given entity
     * 
     * @param entityForm
     * @param customCriteria
     * @throws ServiceException
     */
    public PersistenceResponse removeEntity(EntityForm entityForm, String[] customCriteria, List<SectionCrumb> sectionCrumb)
            throws ServiceException;

    /**
     * Gets an Entity representing a specific collection item
     * 
     * @param containingClassMetadata
     * @param containingEntity
     * @param collectionProperty
     * @param collectionItemId
     * @return the Entity
     * @throws ServiceException
     */
    public PersistenceResponse getAdvancedCollectionRecord(ClassMetadata containingClassMetadata, Entity containingEntity,
            Property collectionProperty, String collectionItemId, List<SectionCrumb> sectionCrumb)
            throws ServiceException;

    /**
     * Returns the DynamicResultSet representing the records that belong to the specified collectionProperty for the 
     * given containingClass and the primary key for the containingClass
     * 
     * @param containingClassMetadata
     * @param containingEntity
     * @param collectionProperty
     * @param fascs
     * @param startIndex
     * @param maxIndex
     * @return the DynamicResultSet
     * @throws ServiceException
     */
    public PersistenceResponse getRecordsForCollection(ClassMetadata containingClassMetadata, Entity containingEntity,
            Property collectionProperty, FilterAndSortCriteria[] fascs, Integer startIndex, Integer maxIndex, List<SectionCrumb> sectionCrumb)
            throws ServiceException;
    
    /**
     * The same as the other getRecordsForCollection method, except that this one expects allow the caller to explicitly
     * set the id value that will be used in the fetch instead of delegating to {@link #getContextSpecificRelationshipId()}
     * 
     * @param containingClassMetadata
     * @param containingEntity
     * @param collectionProperty
     * @param fascs
     * @param startIndex
     * @param maxIndex
     * @param idValueOverride
     * @return the PersistenceResponse
     * @throws ServiceException
     */
    public PersistenceResponse getRecordsForCollection(ClassMetadata containingClassMetadata, Entity containingEntity,
            Property collectionProperty, FilterAndSortCriteria[] fascs, Integer startIndex, Integer maxIndex, 
            String idValueOverride, List<SectionCrumb> sectionCrumb) throws ServiceException;
    /**
     * Returns all records for all subcollections of the specified request and its primary key
     * 
     * @param ppr
     * @param containingEntity
     * @return all Entity[] for all collections for the specified containingClass
     * @throws ServiceException
     * 
     * @see #getRecordsForCollection(ClassMetadata, String, Property)
     */
    public Map<String, DynamicResultSet> getRecordsForAllSubCollections(PersistencePackageRequest ppr, 
            Entity containingEntity, List<SectionCrumb> sectionCrumb)
            throws ServiceException;

    /**
     * Adds an item into the specified collection
     * 
     * @param entityForm
     * @param mainMetadata
     * @param field
     * @param parentEntity
     * @return the persisted Entity
     * @throws ServiceException
     * @throws ClassNotFoundException
     */
    public PersistenceResponse addSubCollectionEntity(EntityForm entityForm, ClassMetadata mainMetadata, Property field,
            Entity parentEntity, List<SectionCrumb> sectionCrumb)
            throws ServiceException, ClassNotFoundException;

    /**
     * Updates the specified collection item
     * 
     * @param entityForm
     * @param mainMetadata
     * @param field
     * @param parentEntity
     * @param collectionItemId
     * @return the persisted Entity
     * @throws ServiceException
     * @throws ClassNotFoundException
     */
    public PersistenceResponse updateSubCollectionEntity(EntityForm entityForm, ClassMetadata mainMetadata, Property field,
            Entity parentEntity, String collectionItemId, List<SectionCrumb> sectionCrumb)
            throws ServiceException, ClassNotFoundException;

    /**
     * Removes the given item from the specified collection.
     * 
     * @param mainMetadata
     * @param field
     * @param parentId
     * @param itemId
     * @param priorKey - only needed for Map type collections
     * @throws ServiceException
     */
    public PersistenceResponse removeSubCollectionEntity(ClassMetadata mainMetadata, Property field, Entity parentEntity, String itemId,
            String priorKey, List<SectionCrumb> sectionCrumb)
            throws ServiceException;

    /**
     * Returns the appropriate id to use for the given entity/metadata and prefix when dealing with collections. For
     * example, on the Product screen, we display associated media. However, this media is actually owned by the Sku entity,
     * which means its property name is "defaultSku.skuMedia". In this case, when wanting to look up media for this product,
     * we cannot use the id of the product. Instead, we need to use the id of the sku.
     * 
     * @param cmd
     * @param entity
     * @param propertyName
     * @return the id to be used for this relationship
     */
    public String getContextSpecificRelationshipId(ClassMetadata cmd, Entity entity, String propertyName);
}

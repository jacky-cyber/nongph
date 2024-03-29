package cn.globalph.openadmin.server.service.persistence;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.admin.domain.AdminMainEntity;
import cn.globalph.common.exception.NoPossibleResultsException;
import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.presentation.client.OperationScope;
import cn.globalph.openadmin.dto.BasicFieldMetadata;
import cn.globalph.openadmin.dto.ClassMetadata;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.FieldMetadata;
import cn.globalph.openadmin.dto.MergedPropertyType;
import cn.globalph.openadmin.dto.PersistenceAssociation;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.dao.DynamicEntityDao;
import cn.globalph.openadmin.server.security.remote.AdminSecurityServiceRemote;
import cn.globalph.openadmin.server.security.remote.EntityOperationType;
import cn.globalph.openadmin.server.security.remote.SecurityVerifier;
import cn.globalph.openadmin.server.service.ValidationException;
import cn.globalph.openadmin.server.service.handler.CustomPersistenceHandlerFilter;
import cn.globalph.openadmin.server.service.handler.CustomPersistenceOperationHandler;
import cn.globalph.openadmin.server.service.persistence.module.InspectHelper;
import cn.globalph.openadmin.server.service.persistence.module.PersistenceModule;
import cn.globalph.openadmin.server.service.persistence.module.RecordHelper;
import cn.globalph.openadmin.web.form.entity.DynamicEntityFormInfo;

import org.hibernate.mapping.PersistentClass;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;

/**
 * 持久操作执行对象，有状态实现
 * @author felix.wu
 *
 */
@Component("blPersistenceManager")
@Scope("prototype")
public class PersistenceOperationExecutorImpl implements InspectHelper, PersistenceOperationExecutor, ApplicationContextAware {

    private static final Log LOG = LogFactory.getLog(PersistenceOperationExecutorImpl.class);

    @Resource(name="blDynamicEntityDao")
    protected DynamicEntityDao dynamicEntityDao;

    @Resource(name="blCustomPersistenceHandlers")
    protected List<CustomPersistenceOperationHandler> customPersistenceHandlers = new ArrayList<CustomPersistenceOperationHandler>();

    @Resource(name="blCustomPersistenceHandlerFilters")
    protected List<CustomPersistenceHandlerFilter> customPersistenceHandlerFilters = new ArrayList<CustomPersistenceHandlerFilter>();

    @Resource(name="blTargetEntityManagers")
    protected Map<String, String> targetEntityManagers = new HashMap<String, String>();

    @Resource(name="blAdminSecurityRemoteService")
    protected SecurityVerifier adminRemoteSecurityService;

    @Resource(name="blPersistenceModules")
    protected PersistenceModule[] modules;

    @Resource(name="blPersistenceManagerEventHandlers")
    protected List<PersistenceOperationEventHandler> persistenceManagerEventHandlers;

    protected TargetModeType targetMode;
    protected ApplicationContext applicationContext;

    @PostConstruct
    public void postConstruct() {
        for (PersistenceModule module : modules) {
            module.setPersistenceManager(this);
        }
        Collections.sort(persistenceManagerEventHandlers, new Comparator<PersistenceOperationEventHandler>() {
                    @Override
                    public int compare(PersistenceOperationEventHandler o1, PersistenceOperationEventHandler o2) {
                return Integer.valueOf(o1.getOrder()).compareTo(Integer.valueOf(o2.getOrder()));
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Class<?>[] getAllPolymorphicEntitiesFromCeiling(Class<?> ceilingClass) {
        return dynamicEntityDao.getAllPolymorphicEntitiesFromCeiling(ceilingClass);
    }

    @Override
    public Class<?>[] getUpDownInheritance(String testClassname) throws ClassNotFoundException {
        return getUpDownInheritance(Class.forName(testClassname));
    }

    @Override
    public Class<?>[] getUpDownInheritance(Class<?> testClass) {
        Class<?>[] pEntities = dynamicEntityDao.getAllPolymorphicEntitiesFromCeiling(testClass);
        if (ArrayUtils.isEmpty(pEntities)) {
            return pEntities;
        }
        Class<?> topConcreteClass = pEntities[pEntities.length - 1];
        List<Class<?>> temp = new ArrayList<Class<?>>(pEntities.length);
        temp.addAll(Arrays.asList(pEntities));
        Collections.reverse(temp);
        boolean eof = false;
        while (!eof) {
            Class<?> superClass = topConcreteClass.getSuperclass();
            PersistentClass persistentClass = dynamicEntityDao.getPersistentClass(superClass.getName());
            if (persistentClass == null) {
                eof = true;
            } else {
                temp.add(0, superClass);
                topConcreteClass = superClass;
            }
        }

        return temp.toArray(new Class<?>[temp.size()]);
    }

    @Override
    public Class<?>[] getPolymorphicEntities(String ceilingEntityFullyQualifiedClassname) throws ClassNotFoundException {
        Class<?>[] entities = getAllPolymorphicEntitiesFromCeiling(Class.forName(ceilingEntityFullyQualifiedClassname));
        return entities;
    }

    @Override
    public Map<String, FieldMetadata> getSimpleMergedProperties(String entityName, PersistenceAssociation persistencePerspective) {
        return dynamicEntityDao.getSimpleMergedProperties(entityName, persistencePerspective);
    }

    @Override
    public ClassMetadata getMergedClassMetadata(final Class<?>[] entities, Map<MergedPropertyType, Map<String, FieldMetadata>> mergedProperties) {
        ClassMetadata classMetadata = new ClassMetadata();
        classMetadata.setPolymorphicEntities(dynamicEntityDao.getClassTree(entities));

        List<Property> propertiesList = new ArrayList<Property>();
        for (PersistenceModule module : modules) {
            module.extractProperties(entities, mergedProperties, propertiesList);
        }

        Property[] properties = new Property[propertiesList.size()];
        properties = propertiesList.toArray(properties);
        Arrays.sort(properties, new Comparator<Property>() {
            @Override
            public int compare(Property o1, Property o2) {
                Integer tabOrder1 = o1.getFieldMetadata().getTabOrder() == null ? 99999 : o1.getFieldMetadata().getTabOrder();
                Integer tabOrder2 = o2.getFieldMetadata().getTabOrder() == null ? 99999 : o2.getFieldMetadata().getTabOrder();

                Integer groupOrder1 = null;
                Integer groupOrder2 = null;
                if (o1.getFieldMetadata() instanceof BasicFieldMetadata) {
                    BasicFieldMetadata b1 = (BasicFieldMetadata) o1.getFieldMetadata();
                    groupOrder1 = b1.getGroupOrder();
                }
                groupOrder1 = groupOrder1 == null ? 99999 : groupOrder1;

                if (o2.getFieldMetadata() instanceof BasicFieldMetadata) {
                    BasicFieldMetadata b2 = (BasicFieldMetadata) o2.getFieldMetadata();
                    groupOrder2 = b2.getGroupOrder();
                }
                groupOrder2 = groupOrder2 == null ? 99999 : groupOrder2;

                Integer fieldOrder1 = o1.getFieldMetadata().getOrder() == null ? 99999 : o1.getFieldMetadata().getOrder();
                Integer fieldOrder2 = o2.getFieldMetadata().getOrder() == null ? 99999 : o2.getFieldMetadata().getOrder();

                String friendlyName1 = o1.getFieldMetadata().getFriendlyName() == null ? "zzzz" : o1.getFieldMetadata().getFriendlyName();
                String friendlyName2 = o2.getFieldMetadata().getFriendlyName() == null ? "zzzz" : o2.getFieldMetadata().getFriendlyName();

                String name1 = o1.getName() == null ? "zzzzz" : o1.getName();
                String name2 = o2.getName() == null ? "zzzzz" : o2.getName();

                return new CompareToBuilder()
                        .append(tabOrder1, tabOrder2)
                        .append(groupOrder1, groupOrder2)
                        .append(fieldOrder1, fieldOrder2)
                        .append(friendlyName1, friendlyName2)
                        .append(name1, name2)
                        .toComparison();
            }
        });
        classMetadata.setProperties(properties);

        return classMetadata;
    }

    @Override
    public PersistenceResponse inspect(PersistencePackage persistencePackage) throws ServiceException, ClassNotFoundException {
        for (PersistenceOperationEventHandler handler : persistenceManagerEventHandlers) {
            PersistenceOperationEventHandlerResponse response = handler.preInspect(this, persistencePackage);
            if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED_BREAK==response.getStatus()) {
                break;
            }
        }
        // check to see if there is a custom handler registered
        for (CustomPersistenceOperationHandler handler : getCustomPersistenceHandlers()) {
            if (handler.canHandleInspect(persistencePackage)) {
                if (!handler.willHandleSecurity(persistencePackage)) {
                    adminRemoteSecurityService.securityCheck(persistencePackage, EntityOperationType.INSPECT);
                }
                DynamicResultSet results = handler.inspect(persistencePackage, dynamicEntityDao, this);
                return executePostInspectHandlers(persistencePackage, new PersistenceResponse().withDynamicResultSet
                        (results));
            }
        }

        adminRemoteSecurityService.securityCheck(persistencePackage, EntityOperationType.INSPECT);
        Class<?>[] entities = getPolymorphicEntities( persistencePackage.getRootEntityClassname() );
        Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties = new HashMap<MergedPropertyType, Map<String, FieldMetadata>>();
        for (PersistenceModule module : modules) {
            module.updateMergedProperties(persistencePackage, allMergedProperties);
        }
        ClassMetadata mergedMetadata = getMergedClassMetadata(entities, allMergedProperties);
        DynamicResultSet results = new DynamicResultSet(mergedMetadata);

        return executePostInspectHandlers(persistencePackage, new PersistenceResponse().withDynamicResultSet(results));
    }

    protected PersistenceResponse executePostInspectHandlers(PersistencePackage persistencePackage,
                                                             PersistenceResponse persistenceResponse) throws ServiceException {
        for (PersistenceOperationEventHandler handler : persistenceManagerEventHandlers) {
            PersistenceOperationEventHandlerResponse response = handler.postInspect(this, persistenceResponse.getDynamicResultSet(), persistencePackage);
            if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED_BREAK==response.getStatus()) {
                persistenceResponse.setDynamicResultSet(response.getDynamicResultSet());
                if (!MapUtils.isEmpty(response.getAdditionalData())) {
                    persistenceResponse.getAdditionalData().putAll(response.getAdditionalData());
                }
                break;
            } else if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED==response.getStatus()) {
                persistenceResponse.setDynamicResultSet(response.getDynamicResultSet());
                if (!MapUtils.isEmpty(response.getAdditionalData())) {
                    persistenceResponse.getAdditionalData().putAll(response.getAdditionalData());
                }
            }
        }
        return persistenceResponse;
    }

    @Override
    public PersistenceResponse fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException {
        for(PersistenceOperationEventHandler handler : persistenceManagerEventHandlers) {
            PersistenceOperationEventHandlerResponse response = handler.preFetch(this, persistencePackage, cto);
            if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED_BREAK==response.getStatus()) {
                break;
            }
        }
        //check to see if there is a custom handler registered
        for(CustomPersistenceOperationHandler handler : getCustomPersistenceHandlers()) {
            if (handler.canHandleFetch(persistencePackage)) {
                if (!handler.willHandleSecurity(persistencePackage)) {
                    adminRemoteSecurityService.securityCheck(persistencePackage, EntityOperationType.FETCH);
                }
                DynamicResultSet results = handler.fetch(persistencePackage, cto, dynamicEntityDao, (RecordHelper) getCompatibleModule(OperationScope.BASIC));
                return executePostFetchHandlers(persistencePackage, cto, new PersistenceResponse().withDynamicResultSet(results));
            }
        }
        adminRemoteSecurityService.securityCheck(persistencePackage, EntityOperationType.FETCH);
        PersistenceModule myModule = getCompatibleModule(persistencePackage.getPersistenceAssociation().getPersistenceOperationType().getFetchType());

        try {
            DynamicResultSet results = myModule.fetch(persistencePackage, cto);
            return executePostFetchHandlers(persistencePackage, cto, new PersistenceResponse().withDynamicResultSet(results));
        } catch (ServiceException e) {
            if (e.getCause() instanceof NoPossibleResultsException) {
                DynamicResultSet drs = new DynamicResultSet(null, new Entity[] {}, 0);
                return executePostFetchHandlers(persistencePackage, cto, new PersistenceResponse().withDynamicResultSet(drs));
            }
            throw e;
        }
    }

    protected PersistenceResponse executePostFetchHandlers(PersistencePackage persistencePackage, CriteriaTransferObject
            cto, PersistenceResponse persistenceResponse) throws ServiceException {
        for (PersistenceOperationEventHandler handler : persistenceManagerEventHandlers) {
            PersistenceOperationEventHandlerResponse response = handler.postFetch(this, persistenceResponse.getDynamicResultSet(), persistencePackage, cto);
            if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED_BREAK==response.getStatus()) {
                persistenceResponse.setDynamicResultSet(response.getDynamicResultSet());
                if (!MapUtils.isEmpty(response.getAdditionalData())) {
                    persistenceResponse.getAdditionalData().putAll(response.getAdditionalData());
                }
                break;
            } else if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED==response.getStatus()) {
                persistenceResponse.setDynamicResultSet(response.getDynamicResultSet());
                if (!MapUtils.isEmpty(response.getAdditionalData())) {
                    persistenceResponse.getAdditionalData().putAll(response.getAdditionalData());
                }
            }
        }
        //support legacy api
        persistenceResponse.setDynamicResultSet(postFetch(persistenceResponse.getDynamicResultSet(), persistencePackage, cto));
        persistenceResponse.getDynamicResultSet().setStartIndex(cto.getFirstResult());
        persistenceResponse.getDynamicResultSet().setPageSize(cto.getMaxResults());

        return persistenceResponse;
    }

    /**
     * Called after the fetch event
     *
     * @param resultSet
     * @param persistencePackage
     * @param cto
     * @return the modified result set
     * @throws ServiceException
     * @deprecated use the PersistenceManagerEventHandler api instead
     */
    @Deprecated
    protected DynamicResultSet postFetch(DynamicResultSet resultSet, PersistencePackage persistencePackage,
            CriteriaTransferObject cto)
            throws ServiceException {
        return resultSet;
    }

    @Override
    public PersistenceResponse add(PersistencePackage persistencePackage) throws ServiceException {
        for (PersistenceOperationEventHandler handler : persistenceManagerEventHandlers) {
            PersistenceOperationEventHandlerResponse response = handler.preAdd(this, persistencePackage);
            if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED_BREAK==response.getStatus()) {
                break;
            }
        }
        //check to see if there is a custom handler registered
        //execute the root PersistencePackage
        Entity response;
        try {
            checkRoot: {
                //if there is a validation exception in the root check, let it bubble, as we need a valid, persisted
                //entity to execute the subPackage code later
                for (CustomPersistenceOperationHandler handler : getCustomPersistenceHandlers()) {
                    if (handler.canHandleAdd(persistencePackage)) {
                        if (!handler.willHandleSecurity(persistencePackage)) {
                            adminRemoteSecurityService.securityCheck(persistencePackage, EntityOperationType.ADD);
                        }
                        response = handler.add(persistencePackage, dynamicEntityDao, (RecordHelper) getCompatibleModule(OperationScope.BASIC));
                        break checkRoot;
                    }
                }
                adminRemoteSecurityService.securityCheck(persistencePackage, EntityOperationType.ADD);
                PersistenceModule myModule = getCompatibleModule(persistencePackage.getPersistenceAssociation().getPersistenceOperationType().getAddType());
                response = myModule.add(persistencePackage);
            }
        } catch (ServiceException e) {
            if (e.getCause() instanceof ValidationException) {
                response = ((ValidationException) e.getCause()).getEntity();
            } else {
                throw e;
            }
        }

        if (!MapUtils.isEmpty(persistencePackage.getSubPackages())) {
            // Once the entity has been saved, we can utilize its id for the subsequent dynamic forms
            Class<?> entityClass;
            try {
                entityClass = Class.forName(response.getType()[0]);
            } catch (ClassNotFoundException e) {
                throw new ServiceException(e);
            }
            Map<String, Object> idMetadata = getDynamicEntityDao().getIdMetadata(entityClass);
            String idProperty = (String) idMetadata.get("name");
            String idVal = response.findProperty(idProperty).getValue();

            Map<String, List<String>> subPackageValidationErrors = new HashMap<String, List<String>>();
            for (Map.Entry<String,PersistencePackage> subPackage : persistencePackage.getSubPackages().entrySet()) {
                Entity subResponse;
                try {
                    subPackage.getValue().getCustomCriteria()[1] = idVal;
                    //Run through any subPackages -- add up any validation errors
                    checkHandler: {
                        for (CustomPersistenceOperationHandler handler : getCustomPersistenceHandlers()) {
                            if (handler.canHandleAdd(subPackage.getValue())) {
                                subResponse = handler.add(subPackage.getValue(), dynamicEntityDao, (RecordHelper) getCompatibleModule(OperationScope.BASIC));
                                subPackage.getValue().setEntity(subResponse);

                                break checkHandler;
                            }
                        }
                        PersistenceModule subModule = getCompatibleModule(subPackage.getValue().getPersistenceAssociation().getPersistenceOperationType().getAddType());
                        subResponse = subModule.add(persistencePackage);
                        subPackage.getValue().setEntity(subResponse);
                    }
                } catch (ValidationException e) {
                    subPackage.getValue().setEntity(e.getEntity());
                } catch (ServiceException e) {
                    if (e.getCause() instanceof ValidationException) {
                        response = ((ValidationException) e.getCause()).getEntity();
                    } else {
                        throw e;
                    }
                }
            }
            
            //Build up validation errors in all of the subpackages, even those that might not have thrown ValidationExceptions
            for (Map.Entry<String, PersistencePackage> subPackage : persistencePackage.getSubPackages().entrySet()) {
                for (Map.Entry<String, List<String>> error : subPackage.getValue().getEntity().getPropertyValidationErrors().entrySet()) {
                    subPackageValidationErrors.put(subPackage.getKey() + DynamicEntityFormInfo.FIELD_SEPARATOR + error.getKey(), error.getValue());
                }
            }
            
            response.getPropertyValidationErrors().putAll(subPackageValidationErrors);
        }

        if (response.isValidationFailure()) {
            PersistenceResponse validationResponse = executeValidationProcessors(persistencePackage, new PersistenceResponse().withEntity(response));
            throw new ValidationException(validationResponse.getEntity(), "The entity has failed validation");
        }

        return executePostAddHandlers(persistencePackage, new PersistenceResponse().withEntity(response));
    }

    protected PersistenceResponse executeValidationProcessors(PersistencePackage persistencePackage, PersistenceResponse persistenceResponse) throws ServiceException {
        for (PersistenceOperationEventHandler handler : persistenceManagerEventHandlers) {
            PersistenceOperationEventHandlerResponse response = handler.processValidationError(this,
                    persistenceResponse.getEntity(), persistencePackage);
            if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED_BREAK==response.getStatus()) {
                persistenceResponse.setEntity(response.getEntity());
                if (!MapUtils.isEmpty(response.getAdditionalData())) {
                    persistenceResponse.getAdditionalData().putAll(response.getAdditionalData());
                }
                break;
            } else if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED==response.getStatus()) {
                persistenceResponse.setEntity(response.getEntity());
                if (!MapUtils.isEmpty(response.getAdditionalData())) {
                    persistenceResponse.getAdditionalData().putAll(response.getAdditionalData());
                }
            }
        }

        return persistenceResponse;
    }

    protected PersistenceResponse executePostAddHandlers(PersistencePackage persistencePackage, PersistenceResponse persistenceResponse) throws ServiceException {
        setMainEntityName(persistencePackage, persistenceResponse.getEntity());
        for (PersistenceOperationEventHandler handler : persistenceManagerEventHandlers) {
            PersistenceOperationEventHandlerResponse response = handler.postAdd(this, persistenceResponse.getEntity(), persistencePackage);
            if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED_BREAK==response.getStatus()) {
                persistenceResponse.setEntity(response.getEntity());
                if (!MapUtils.isEmpty(response.getAdditionalData())) {
                    persistenceResponse.getAdditionalData().putAll(response.getAdditionalData());
                }
                break;
            } else if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED==response.getStatus()) {
                persistenceResponse.setEntity(response.getEntity());
                if (!MapUtils.isEmpty(response.getAdditionalData())) {
                    persistenceResponse.getAdditionalData().putAll(response.getAdditionalData());
                }
            }
        }
        //support legacy api
        persistenceResponse.setEntity(postAdd(persistenceResponse.getEntity(), persistencePackage));

        return persistenceResponse;
    }

    /**
     * Called after the add event
     *
     * @param entity
     * @param persistencePackage
     * @return the modified Entity instance
     * @throws ServiceException
     * @deprecated use the PersistenceManagerEventHandler api instead
     */
    @Deprecated
    protected Entity postAdd(Entity entity, PersistencePackage persistencePackage) throws ServiceException {
        //do nothing
        return entity;
    }

    @Override
    public PersistenceResponse update(PersistencePackage persistencePackage) throws ServiceException {
        for (PersistenceOperationEventHandler handler : persistenceManagerEventHandlers) {
            PersistenceOperationEventHandlerResponse response = handler.preUpdate(this, persistencePackage);
            if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED_BREAK==response.getStatus()) {
                break;
            }
        }
        //check to see if there is a custom handler registered
        //execute the root PersistencePackage
        Entity response;
        try {
            checkRoot: {
                for (CustomPersistenceOperationHandler handler : getCustomPersistenceHandlers()) {
                    if (handler.canHandleUpdate(persistencePackage)) {
                        if (!handler.willHandleSecurity(persistencePackage)) {
                            adminRemoteSecurityService.securityCheck(persistencePackage, EntityOperationType.UPDATE);
                        }
                        response = handler.update(persistencePackage, dynamicEntityDao, (RecordHelper) getCompatibleModule(OperationScope.BASIC));
                        break checkRoot;
                    }
                }
                adminRemoteSecurityService.securityCheck(persistencePackage, EntityOperationType.UPDATE);
                PersistenceModule myModule = getCompatibleModule(persistencePackage.getPersistenceAssociation().getPersistenceOperationType().getUpdateType());
                response = myModule.update(persistencePackage);
            }
        } catch (ValidationException e) {
            response = e.getEntity();
        } catch (ServiceException e) {
            if (e.getCause() instanceof ValidationException) {
                response = ((ValidationException) e.getCause()).getEntity();
            } else {
                throw e;
            }
        }

        Map<String, List<String>> subPackageValidationErrors = new HashMap<String, List<String>>();
        for (Map.Entry<String,PersistencePackage> subPackage : persistencePackage.getSubPackages().entrySet()) {
            try {
                //Run through any subPackages -- add up any validation errors
                checkHandler: {
                    for (CustomPersistenceOperationHandler handler : getCustomPersistenceHandlers()) {
                        if (handler.canHandleUpdate(subPackage.getValue())) {
                            Entity subResponse = handler.update(subPackage.getValue(), dynamicEntityDao, (RecordHelper) getCompatibleModule(OperationScope.BASIC));
                            subPackage.getValue().setEntity(subResponse);
                            break checkHandler;
                        }
                    }
                    PersistenceModule subModule = getCompatibleModule(subPackage.getValue().getPersistenceAssociation().getPersistenceOperationType().getUpdateType());
                    Entity subResponse = subModule.update(persistencePackage);
                    subPackage.getValue().setEntity(subResponse);
                }
            } catch (ValidationException e) {
                subPackage.getValue().setEntity(e.getEntity());
            } catch (ServiceException e) {
                if (e.getCause() instanceof ValidationException) {
                    response = ((ValidationException) e.getCause()).getEntity();
                } else {
                    throw e;
                }
            }
        }
        
        //Build up validation errors in all of the subpackages, even those that might not have thrown ValidationExceptions
        for (Map.Entry<String, PersistencePackage> subPackage : persistencePackage.getSubPackages().entrySet()) {
            for (Map.Entry<String, List<String>> error : subPackage.getValue().getEntity().getPropertyValidationErrors().entrySet()) {
                subPackageValidationErrors.put(subPackage.getKey() + DynamicEntityFormInfo.FIELD_SEPARATOR + error.getKey(), error.getValue());
            }
        }

        response.getPropertyValidationErrors().putAll(subPackageValidationErrors);

        if (response.isValidationFailure()) {
            PersistenceResponse validationResponse = executeValidationProcessors(persistencePackage, new PersistenceResponse().withEntity(response));
            throw new ValidationException(validationResponse.getEntity(), "The entity has failed validation");
        }

        return executePostUpdateHandlers(persistencePackage, new PersistenceResponse().withEntity(response));
    }

    protected PersistenceResponse executePostUpdateHandlers(PersistencePackage persistencePackage, PersistenceResponse persistenceResponse) throws ServiceException {
        setMainEntityName(persistencePackage, persistenceResponse.getEntity());
        for (PersistenceOperationEventHandler handler : persistenceManagerEventHandlers) {
            PersistenceOperationEventHandlerResponse response = handler.postUpdate(this, persistenceResponse.getEntity(), persistencePackage);
            if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED_BREAK==response.getStatus()) {
                persistenceResponse.setEntity(response.getEntity());
                if (!MapUtils.isEmpty(response.getAdditionalData())) {
                    persistenceResponse.getAdditionalData().putAll(response.getAdditionalData());
                }
                break;
            } else if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED==response.getStatus()) {
                persistenceResponse.setEntity(response.getEntity());
                if (!MapUtils.isEmpty(response.getAdditionalData())) {
                    persistenceResponse.getAdditionalData().putAll(response.getAdditionalData());
                }
            }
        }
        //support legacy api
        persistenceResponse.setEntity(postUpdate(persistenceResponse.getEntity(), persistencePackage));

        return persistenceResponse;
    }

    /**
     * Called after the update event
     *
     * @param entity
     * @param persistencePackage
     * @return the modified Entity instance
     * @throws ServiceException
     * @deprecated use the PersistenceManagerEventHandler api instead
     */
    @Deprecated
    protected Entity postUpdate(Entity entity, PersistencePackage persistencePackage) throws ServiceException {
        //do nothing
        return entity;
    }

    @Override
    public PersistenceResponse remove(PersistencePackage persistencePackage) throws ServiceException {
        for (PersistenceOperationEventHandler handler : persistenceManagerEventHandlers) {
            PersistenceOperationEventHandlerResponse response = handler.preRemove(this, persistencePackage);
            if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED_BREAK==response.getStatus()) {
                break;
            }
        }
        //check to see if there is a custom handler registered
        for (CustomPersistenceOperationHandler handler : getCustomPersistenceHandlers()) {
            if (handler.canHandleRemove(persistencePackage)) {
                if (!handler.willHandleSecurity(persistencePackage)) {
                    adminRemoteSecurityService.securityCheck(persistencePackage, EntityOperationType.REMOVE);
                }
                handler.remove(persistencePackage, dynamicEntityDao, (RecordHelper) getCompatibleModule(OperationScope.BASIC));
                return executePostRemoveHandlers(persistencePackage, new PersistenceResponse());
            }
        }
        adminRemoteSecurityService.securityCheck(persistencePackage, EntityOperationType.REMOVE);
        PersistenceModule myModule = getCompatibleModule(persistencePackage.getPersistenceAssociation().getPersistenceOperationType().getRemoveType());
        myModule.remove(persistencePackage);

        return executePostRemoveHandlers(persistencePackage, new PersistenceResponse());
    }

    protected PersistenceResponse executePostRemoveHandlers(PersistencePackage persistencePackage, PersistenceResponse persistenceResponse) throws ServiceException {
        setMainEntityName(persistencePackage, persistenceResponse.getEntity());
        for (PersistenceOperationEventHandler handler : persistenceManagerEventHandlers) {
            PersistenceOperationEventHandlerResponse response = handler.postRemove(this, persistencePackage);
            if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED_BREAK==response.getStatus()) {
                if (!MapUtils.isEmpty(response.getAdditionalData())) {
                    persistenceResponse.getAdditionalData().putAll(response.getAdditionalData());
                }
                break;
            } else if (PersistenceOperationEventHandlerResponse.ResponseStatus.HANDLED==response.getStatus()) {
                if (!MapUtils.isEmpty(response.getAdditionalData())) {
                    persistenceResponse.getAdditionalData().putAll(response.getAdditionalData());
                }
            }
        }

        return persistenceResponse;
    }

    @Override
    public PersistenceModule getCompatibleModule(OperationScope operationType) {
        PersistenceModule myModule = null;
        for (PersistenceModule module : modules) {
            if (module.isCompatible(operationType)) {
                myModule = module;
                break;
            }
        }
        if (myModule == null) {
            LOG.error("Unable to find a compatible remote service module for the operation type: " + operationType);
            throw new RuntimeException("Unable to find a compatible remote service module for the operation type: " + operationType);
        }

        return myModule;
    }

    @Override
    public DynamicEntityDao getDynamicEntityDao() {
        return dynamicEntityDao;
    }

    @Override
    public void setDynamicEntityDao(DynamicEntityDao dynamicEntityDao) {
        this.dynamicEntityDao = dynamicEntityDao;
    }

    @Override
    public Map<String, String> getTargetEntityManagers() {
        return targetEntityManagers;
    }

    @Override
    public void setTargetEntityManagers(Map<String, String> targetEntityManagers) {
        this.targetEntityManagers = targetEntityManagers;
    }

    @Override
    public TargetModeType getTargetMode() {
        return targetMode;
    }

    @Override
    public void setTargetMode(TargetModeType targetMode) {
        String targetManagerRef = targetEntityManagers.get(targetMode.getType());
        EntityManager targetManager = (EntityManager) applicationContext.getBean(targetManagerRef);
        if (targetManager == null) {
            throw new RuntimeException("Unable to find a target entity manager registered with the key: " + targetMode + ". Did you add an entity manager with this key to the targetEntityManagers property?");
        }
        dynamicEntityDao.setStandardEntityManager(targetManager);
        this.targetMode = targetMode;
    }

    @Override
    public List<CustomPersistenceOperationHandler> getCustomPersistenceHandlers() {
        List<CustomPersistenceOperationHandler> cloned = new ArrayList<CustomPersistenceOperationHandler>();
        cloned.addAll(customPersistenceHandlers);
        if (getCustomPersistenceHandlerFilters() != null) {
            for (CustomPersistenceHandlerFilter filter : getCustomPersistenceHandlerFilters()) {
                Iterator<CustomPersistenceOperationHandler> itr = cloned.iterator();
                while (itr.hasNext()) {
                    CustomPersistenceOperationHandler handler = itr.next();
                    if (!filter.shouldUseHandler(handler.getClass().getName())) {
                        itr.remove();
                    }
                }
            }
        }
        Collections.sort(cloned, new Comparator<CustomPersistenceOperationHandler>() {
            @Override
            public int compare(CustomPersistenceOperationHandler o1, CustomPersistenceOperationHandler o2) {
                return new Integer(o1.getOrder()).compareTo(new Integer(o2.getOrder()));
            }
        });
        return cloned;
    }
    
    protected void setMainEntityName(PersistencePackage pp, Entity entity) {
        if (StringUtils.isBlank(pp.getRequestingEntityName()) && entity != null) {
            Property nameProp = entity.getPMap().get(AdminMainEntity.MAIN_ENTITY_NAME_PROPERTY);
            if (nameProp != null) {
                pp.setRequestingEntityName(nameProp.getValue());
            }
        }
    }

    @Override
    public void setCustomPersistenceHandlers(List<CustomPersistenceOperationHandler> customPersistenceHandlers) {
        this.customPersistenceHandlers = customPersistenceHandlers;
    }

    public SecurityVerifier getAdminRemoteSecurityService() {
        return adminRemoteSecurityService;
    }

    public void setAdminRemoteSecurityService(AdminSecurityServiceRemote adminRemoteSecurityService) {
        this.adminRemoteSecurityService = adminRemoteSecurityService;
    }

    public List<CustomPersistenceHandlerFilter> getCustomPersistenceHandlerFilters() {
        return customPersistenceHandlerFilters;
    }

    public void setCustomPersistenceHandlerFilters(List<CustomPersistenceHandlerFilter> customPersistenceHandlerFilters) {
        this.customPersistenceHandlerFilters = customPersistenceHandlerFilters;
    }

    public PersistenceModule[] getModules() {
        return modules;
    }

    public void setModules(PersistenceModule[] modules) {
        this.modules = modules;
    }
}

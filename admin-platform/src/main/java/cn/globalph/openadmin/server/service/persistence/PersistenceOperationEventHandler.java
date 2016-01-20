package cn.globalph.openadmin.server.service.persistence;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.PersistencePackage;

import org.springframework.core.Ordered;

/**
 * Interface for handling various lifecycle event for a persistence operation execution.
 * These events occur as part of the standard admin persistence lifecycle for entities.
 * <p/>
 * PersistenceOperationEventHandler instances are generally registered via the following approach in application
 * context xml
 * <p/>
 * {@code
 * <bean class="cn.globalph.common.extensibility.context.merge.LateStageMergeBeanPostProcessor">
 *      <property name="collectionRef" value="blSandBoxPersistenceManagerEventHandlers"/>
 *      <property name="targetRef" value="blPersistenceManagerEventHandlers"/>
 * </bean>
 * <bean id="blSandBoxPersistenceManagerEventHandlers" class="org.springframework.beans.factory.config.ListFactoryBean">
 *      <property name="sourceList">
 *          <list>
 *              <ref bean="blSandBoxPersistenceManagerEventHandler"/>
 *          </list>
*       </property>
 * </bean>
 * }
 *
 * @author felix.wu
 */
public interface PersistenceOperationEventHandler extends Ordered {

    /**
     * Called prior to inspection for the entity described by persistencePackage
     *
     * @param persistenceManager the PersistenceManager instance making the call
     * @param persistencePackage the descriptive information for the call
     * @return the response containing any changes, status or additional data
     * @throws ServiceException
     */
    PersistenceOperationEventHandlerResponse preInspect(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage) throws ServiceException;

    /**
     * Called after the inspection for the entity described by persistencePackage
     *
     * @param persistenceManager the PersistenceManager instance making the call
     * @param resultSet the inspection result data
     * @param persistencePackage the descriptive information for the call
     * @return the response containing any changes, status or additional data
     * @throws ServiceException
     */
    PersistenceOperationEventHandlerResponse postInspect(PersistenceOperationExecutor persistenceManager, DynamicResultSet resultSet, PersistencePackage persistencePackage) throws ServiceException;

    /**
     * Called prior to a fetch, which is a request for one or more persisted entities
     *
     * @param persistenceManager the PersistenceManager instance making the call
     * @param persistencePackage the descriptive information for the call
     * @param cto the criteria describing the parameters of the fetch - converted into the where clause for the select query
     * @return the response containing any changes, status or additional data
     * @throws ServiceException
     */
    PersistenceOperationEventHandlerResponse preFetch(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException;

    /**
     * Called after the fetch, which is a request for one or more persisted entities
     *
     * @param persistenceManager the PersistenceManager instance making the call
     * @param resultSet the fetch result data
     * @param persistencePackage the descriptive information for the call
     * @param cto the criteria describing the parameters of the fetch - converted into the where clause for the select query
     * @return the response containing any changes, status or additional data
     * @throws ServiceException
     */
    PersistenceOperationEventHandlerResponse postFetch(PersistenceOperationExecutor persistenceManager, DynamicResultSet resultSet, PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException;

    /**
     * Called prior to an add
     *
     * @param persistenceManager the PersistenceManager instance making the call
     * @param persistencePackage the descriptive information for the call
     * @return the response containing any changes, status or additional data
     * @throws ServiceException
     */
    PersistenceOperationEventHandlerResponse preAdd(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage) throws ServiceException;

    /**
     * Called after an add
     *
     * @param persistenceManager the PersistenceManager instance making the call
     * @param entity the result of the add
     * @param persistencePackage the descriptive information for the call
     * @return the response containing any changes, status or additional data
     * @throws ServiceException
     */
    PersistenceOperationEventHandlerResponse postAdd(PersistenceOperationExecutor persistenceManager, Entity entity, PersistencePackage persistencePackage) throws ServiceException;

    /**
     * Called prior to an update
     *
     * @param persistenceManager the PersistenceManager instance making the call
     * @param persistencePackage the descriptive information for the call
     * @return the response containing any changes, status or additional data
     * @throws ServiceException
     */
    PersistenceOperationEventHandlerResponse preUpdate(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage) throws ServiceException;

    /**
     * Called after an update
     *
     * @param persistenceManager the PersistenceManager instance making the call
     * @param entity the result of the update
     * @param persistencePackage the descriptive information for the call
     * @return the response containing any changes, status or additional data
     * @throws ServiceException
     */
    PersistenceOperationEventHandlerResponse postUpdate(PersistenceOperationExecutor persistenceManager, Entity entity, PersistencePackage persistencePackage) throws ServiceException;

    /**
     * Called prior to a remove
     *
     * @param persistenceManager the PersistenceManager instance making the call
     * @param persistencePackage the descriptive information for the call
     * @return the response containing any changes, status or additional data
     * @throws ServiceException
     */
    PersistenceOperationEventHandlerResponse preRemove(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage) throws ServiceException;

    /**
     * Called after a remove
     *
     * @param persistenceManager the PersistenceManager instance making the call
     * @param persistencePackage the descriptive information for the call
     * @return the response containing any changes, status or additional data
     * @throws ServiceException
     */
    PersistenceOperationEventHandlerResponse postRemove(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage) throws ServiceException;

    /**
     * Called after a validation error. Validations occur on adds, updates and removes. The validation confirms the persistence request is
     * correct and does not have any errors. This event handling hook provides an opportunity to impact and/or modify
     * the results of validation errors. Errors are generally reviewed in this method by looking at
     * {@link cn.globalph.openadmin.dto.Entity#getPropertyValidationErrors()}
     *
     * @param persistenceManager the PersistenceManager instance making the call
     * @param entity the results of the persistence request
     * @param persistencePackage the descriptive information for the call
     * @return the response containing any changes, status or additional data
     * @throws ServiceException
     */
    PersistenceOperationEventHandlerResponse processValidationError(PersistenceOperationExecutor persistenceManager, Entity entity, PersistencePackage persistencePackage) throws ServiceException;
}

package cn.globalph.openadmin.server.service.persistence;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.DynamicResultSet;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.PersistencePackage;

/**
 * Convenience adapter for PersistenceManagerEventHandler implementations that don't need to implement every
 * method of the interface.
 *
 * @author Jeff Fischer
 */
public class PersistenceManagerEventHandlerAdapter implements PersistenceOperationEventHandler {

    @Override
    public PersistenceOperationEventHandlerResponse postAdd(PersistenceOperationExecutor persistenceManager, Entity entity, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceOperationEventHandlerResponse().withEntity(entity).
                withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceOperationEventHandlerResponse preFetch(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException {
        return new PersistenceOperationEventHandlerResponse().
                withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceOperationEventHandlerResponse postFetch(PersistenceOperationExecutor persistenceManager, DynamicResultSet resultSet, PersistencePackage persistencePackage,
                                      CriteriaTransferObject cto) throws ServiceException {
        return new PersistenceOperationEventHandlerResponse().withDynamicResultSet(resultSet).
                withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceOperationEventHandlerResponse preAdd(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceOperationEventHandlerResponse().
                withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceOperationEventHandlerResponse preUpdate(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceOperationEventHandlerResponse().
                withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceOperationEventHandlerResponse postUpdate(PersistenceOperationExecutor persistenceManager, Entity entity, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceOperationEventHandlerResponse().withEntity(entity).
                withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceOperationEventHandlerResponse preRemove(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceOperationEventHandlerResponse().
                withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceOperationEventHandlerResponse postRemove(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceOperationEventHandlerResponse().
                withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceOperationEventHandlerResponse postInspect(PersistenceOperationExecutor persistenceManager, DynamicResultSet resultSet, PersistencePackage
            persistencePackage) throws ServiceException {
        return new PersistenceOperationEventHandlerResponse().withDynamicResultSet(resultSet).
                withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceOperationEventHandlerResponse preInspect(PersistenceOperationExecutor persistenceManager, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceOperationEventHandlerResponse().
                withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.NOT_HANDLED);
    }

    @Override
    public PersistenceOperationEventHandlerResponse processValidationError(PersistenceOperationExecutor persistenceManager, Entity entity, PersistencePackage persistencePackage) throws ServiceException {
        return new PersistenceOperationEventHandlerResponse().
                        withStatus(PersistenceOperationEventHandlerResponse.ResponseStatus.NOT_HANDLED);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}

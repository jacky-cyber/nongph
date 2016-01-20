package cn.globalph.openadmin.server.service;

import java.lang.reflect.Constructor;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.exception.ServiceException;
import cn.globalph.common.security.service.CleanStringException;
import cn.globalph.common.security.service.ExploitProtectionService;
import cn.globalph.openadmin.dto.BatchDynamicResultSet;
import cn.globalph.openadmin.dto.BatchPersistencePackage;
import cn.globalph.openadmin.dto.CriteriaTransferObject;
import cn.globalph.openadmin.dto.Entity;
import cn.globalph.openadmin.dto.PersistencePackage;
import cn.globalph.openadmin.dto.Property;
import cn.globalph.openadmin.server.service.persistence.Persistable;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationExecuteProxy;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationExecutor;
import cn.globalph.openadmin.server.service.persistence.PersistenceOperationExecutorFactory;
import cn.globalph.openadmin.server.service.persistence.PersistenceResponse;
import cn.globalph.openadmin.server.service.persistence.TargetModeType;

import org.codehaus.jackson.map.util.LRUMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author felix.wu
 */
@Service("blDynamicEntityRemoteService")
@Transactional(value="blTransactionManager", rollbackFor = ServiceException.class)
public class DynamicEntityRemoteService implements DynamicEntityService {

    private static final Log LOG = LogFactory.getLog(DynamicEntityRemoteService.class);
    protected static final Map<BatchPersistencePackage, BatchDynamicResultSet> METADATA_CACHE = MapUtils.synchronizedMap(new LRUMap<BatchPersistencePackage, BatchDynamicResultSet>(100, 1000));

    @Resource(name="blExploitProtectionService")
    protected ExploitProtectionService exploitProtectionService;

    @Resource(name="blPersistenceThreadManager")
    protected PersistenceOperationExecuteProxy poExecuteProxy;

    protected ServiceException recreateSpecificServiceException(ServiceException e, String message, Throwable cause) {
        try {
            ServiceException newException;
            if (cause == null) {
                Constructor<? extends ServiceException> constructor = e.getClass().getConstructor(String.class);
                newException = constructor.newInstance(message);
            } else {
                Constructor<? extends ServiceException> constructor = e.getClass().getConstructor(String.class, Throwable.class);
                newException = constructor.newInstance(message, cause);
            }

            return newException;
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }

    @Override
    public PersistenceResponse inspect(final PersistencePackage persistencePackage) throws ServiceException {
        return poExecuteProxy.operation(TargetModeType.SANDBOX, new Persistable <PersistenceResponse, ServiceException>() {
            @Override
            public PersistenceResponse execute() throws ServiceException {
                try {
                    PersistenceOperationExecutor ppExecutor = PersistenceOperationExecutorFactory.getPersistenceManager();
                    return ppExecutor.inspect(persistencePackage);
                } catch (ServiceException e) {
                    String message = exploitProtectionService.cleanString(e.getMessage());
                    throw recreateSpecificServiceException(e, message, e.getCause());
                } catch (Exception e) {
                	String ceilingEntityClassname = persistencePackage.getRootEntityClassname();
                    LOG.error("Problem inspecting results for " + ceilingEntityClassname, e);
                    throw new ServiceException(exploitProtectionService.cleanString("Unable to fetch results for " + ceilingEntityClassname), e);
                }
            }
        });
    }

    @Override
    public PersistenceResponse fetch(final PersistencePackage persistencePackage, final CriteriaTransferObject cto) throws ServiceException {
        return poExecuteProxy.operation(TargetModeType.SANDBOX, new Persistable<PersistenceResponse, ServiceException>() {
            @Override
            public PersistenceResponse execute() throws ServiceException {
                try {
                    PersistenceOperationExecutor persistenceManager = PersistenceOperationExecutorFactory.getPersistenceManager();
                    return persistenceManager.fetch(persistencePackage, cto);
                } catch (ServiceException e) {
                    LOG.error("Problem fetching results for " + persistencePackage.getRootEntityClassname(), e);
                    String message = exploitProtectionService.cleanString(e.getMessage());
                    throw recreateSpecificServiceException(e, message, e.getCause());
                }
            }
        });
    }

    protected void cleanEntity(Entity entity) throws ServiceException {
        Property currentProperty = null;
        try {
            for (Property property : entity.getProperties()) {
                currentProperty = property;
                property.setRawValue(property.getValue());
                property.setValue(exploitProtectionService.cleanStringWithResults(property.getValue()));
                property.setUnHtmlEncodedValue(StringEscapeUtils.unescapeHtml(property.getValue()));
            }
        } catch (CleanStringException e) {
            StringBuilder sb = new StringBuilder();
            for (int j=0;j<e.getCleanResults().getNumberOfErrors();j++){
                sb.append(j+1);
                sb.append(") ");
                sb.append((String) e.getCleanResults().getErrorMessages().get(j));
                sb.append("\n");
            }
            sb.append("\nNote - ");
            sb.append(exploitProtectionService.getAntiSamyPolicyFileLocation());
            sb.append(" policy in effect. Set a new policy file to modify validation behavior/strictness.");
            entity.addValidationError(currentProperty.getName(), sb.toString());
        }
    }

    @Override
    public PersistenceResponse add(final PersistencePackage persistencePackage) throws ServiceException {
        return poExecuteProxy.operation(TargetModeType.SANDBOX, new Persistable<PersistenceResponse, ServiceException>() {
            @Override
            public PersistenceResponse execute() throws ServiceException {
                cleanEntity(persistencePackage.getEntity());
                if (persistencePackage.getEntity().isValidationFailure()) {
                    return new PersistenceResponse().withEntity(persistencePackage.getEntity());
                }
                try {
                    PersistenceOperationExecutor persistenceManager = PersistenceOperationExecutorFactory.getPersistenceManager();
                    return persistenceManager.add(persistencePackage);
                } catch (ServiceException e) {
                    //immediately throw validation exceptions without printing a stack trace
                    if (e instanceof ValidationException) {
                        throw e;
                    } else if (e.getCause() instanceof ValidationException) {
                        throw (ValidationException) e.getCause();
                    }
                    String message = exploitProtectionService.cleanString(e.getMessage());
                    LOG.error("Problem adding new " + persistencePackage.getRootEntityClassname(), e);
                    throw recreateSpecificServiceException(e, message, e.getCause());
                }
            }
        });
    }

    @Override
    public PersistenceResponse update(final PersistencePackage persistencePackage) throws ServiceException {
        return poExecuteProxy.operation(TargetModeType.SANDBOX, new Persistable<PersistenceResponse, ServiceException>() {
            @Override
            public PersistenceResponse execute() throws ServiceException {
                cleanEntity(persistencePackage.getEntity());
                if (persistencePackage.getEntity().isValidationFailure()) {
                    return new PersistenceResponse().withEntity(persistencePackage.getEntity());
                }
                try {
                    PersistenceOperationExecutor persistenceManager = PersistenceOperationExecutorFactory.getPersistenceManager();
                    return persistenceManager.update(persistencePackage);
                } catch (ServiceException e) {
                    //immediately throw validation exceptions without printing a stack trace
                    if (e instanceof ValidationException) {
                        throw e;
                    } else if (e.getCause() instanceof ValidationException) {
                        throw (ValidationException) e.getCause();
                    }
                    LOG.error("Problem updating " + persistencePackage.getRootEntityClassname(), e);
                    String message = exploitProtectionService.cleanString(e.getMessage());
                    throw recreateSpecificServiceException(e, message, e.getCause());
                }
            }
        });
    }

    @Override
    public PersistenceResponse remove(final PersistencePackage persistencePackage) throws ServiceException {
        return poExecuteProxy.operation(TargetModeType.SANDBOX, new Persistable<PersistenceResponse, ServiceException>() {
            @Override
            public PersistenceResponse execute() throws ServiceException {
                try {
                    PersistenceOperationExecutor persistenceManager = PersistenceOperationExecutorFactory.getPersistenceManager();
                    return persistenceManager.remove(persistencePackage);
                } catch (ServiceException e) {
                    //immediately throw validation exceptions without printing a stack trace
                    if (e instanceof ValidationException) {
                        throw e;
                    } else if (e.getCause() instanceof ValidationException) {
                        throw (ValidationException) e.getCause();
                    }
                    LOG.error("Problem removing " + persistencePackage.getRootEntityClassname(), e);
                    String message = exploitProtectionService.cleanString(e.getMessage());
                    throw recreateSpecificServiceException(e, message, e.getCause());
                }
            }
        });
    }
}

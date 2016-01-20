package cn.globalph.openadmin.server.service.persistence;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @author felix.wu
 */
@Service("blPersistenceManagerFactory")
public class PersistenceOperationExecutorFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    public static final String DEFAULTPERSISTENCEMANAGERREF = "blPersistenceManager";
    protected static String persistenceManagerRef = DEFAULTPERSISTENCEMANAGERREF;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        PersistenceOperationExecutorFactory.applicationContext = applicationContext;
    }

    public static PersistenceOperationExecutor getPersistenceManager() {
        if (PersistenceOperationExecuteContext.getPersistenceManagerContext() != null) {
            return PersistenceOperationExecuteContext.getPersistenceManagerContext().getPersistenceManager();
        }
        throw new IllegalStateException("PersistenceManagerContext is not set on ThreadLocal. If you want to use the " +
                "non-cached version, try getPersistenceManager(TargetModeType)");
    }

    public static PersistenceOperationExecutor getPersistenceManager(TargetModeType targetModeType) {
        PersistenceOperationExecutor persistenceManager = (PersistenceOperationExecutor) applicationContext.getBean(persistenceManagerRef);
        persistenceManager.setTargetMode(targetModeType);

        return persistenceManager;
    }

    public static boolean isPersistenceManagerActive() {
        return applicationContext.containsBean(getPersistenceManagerRef());
    }

    public static void startPersistenceManager(TargetModeType targetModeType) {
        PersistenceOperationExecuteContext context = PersistenceOperationExecuteContext.getPersistenceManagerContext();
        if (context == null) {
            context = new PersistenceOperationExecuteContext();
            PersistenceOperationExecuteContext.addPersistenceManagerContext(context);
        }
        context.addPersistenceManager(getPersistenceManager(targetModeType));
    }

    public static void endPersistenceManager() {
        PersistenceOperationExecuteContext context = PersistenceOperationExecuteContext.getPersistenceManagerContext();
        if (context != null) {
            context.remove();
        }
    }

    public static String getPersistenceManagerRef() {
        return persistenceManagerRef;
    }

    public static void setPersistenceManagerRef(String persistenceManagerRef) {
        PersistenceOperationExecutorFactory.persistenceManagerRef = persistenceManagerRef;
    }
}

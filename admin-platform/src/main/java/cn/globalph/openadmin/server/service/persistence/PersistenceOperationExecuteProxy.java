package cn.globalph.openadmin.server.service.persistence;

import org.springframework.stereotype.Service;

/**
 * @author felix.wu
 */
@Service("blPersistenceThreadManager")
public class PersistenceOperationExecuteProxy {

    public <T, G extends Throwable> T operation(TargetModeType targetModeType, Persistable<T, G> persistable) throws G {
        try {
            PersistenceOperationExecutorFactory.startPersistenceManager(targetModeType);
            return persistable.execute();
        } finally {
            PersistenceOperationExecutorFactory.endPersistenceManager();
        }
    }
}

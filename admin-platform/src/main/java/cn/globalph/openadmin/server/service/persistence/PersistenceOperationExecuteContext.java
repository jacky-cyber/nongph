package cn.globalph.openadmin.server.service.persistence;

import cn.globalph.common.classloader.release.ThreadLocalManager;

import java.util.Stack;

/**
 * @author felix.wu
 */
public class PersistenceOperationExecuteContext {

    private static final ThreadLocal<PersistenceOperationExecuteContext> CURRENT_CONTEXT = ThreadLocalManager.createThreadLocal(PersistenceOperationExecuteContext.class, false);

    public static PersistenceOperationExecuteContext getPersistenceManagerContext() {
        return CURRENT_CONTEXT.get();
    }

    public static void addPersistenceManagerContext(PersistenceOperationExecuteContext persistenceManagerContext) {
        CURRENT_CONTEXT.set(persistenceManagerContext);
    }

    private static void clear() {
        CURRENT_CONTEXT.remove();
    }

    private final Stack<PersistenceOperationExecutor> poeStack = new Stack<PersistenceOperationExecutor>();

    public void addPersistenceManager(PersistenceOperationExecutor persistenceManager) {
        this.poeStack.add(persistenceManager);
    }

    public PersistenceOperationExecutor getPersistenceManager() {
        return !poeStack.empty()?poeStack.peek():null;
    }

    public void remove() {
        if (!poeStack.empty()) {
            poeStack.pop();
        }
        if (poeStack.empty()) {
            PersistenceOperationExecuteContext.clear();
        }
    }
}

package cn.globalph.common.util;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Jeff Fischer
 */
public interface StreamingTransactionCapable {

    int getPageSize();

    void setPageSize(int pageSize);

    int getRetryMax();

    void setRetryMax(int retryMax);

    <G extends Throwable> void runStreamingTransactionalOperation(final StreamCapableTransactionalOperation
                                                                          streamOperation, Class<G> exceptionType) throws G;

    <G extends Throwable> void runStreamingTransactionalOperation(final StreamCapableTransactionalOperation
                                                                          streamOperation, Class<G> exceptionType, int transactionBehavior, int isolationLevel) throws G;

    <G extends Throwable> void runTransactionalOperation(StreamCapableTransactionalOperation operation,
                                                         Class<G> exceptionType) throws G;

    <G extends Throwable> void runTransactionalOperation(StreamCapableTransactionalOperation operation,
                                                         Class<G> exceptionType, int transactionBehavior, int
            isolationLevel) throws G;

    <G extends Throwable> void runOptionalTransactionalOperation(StreamCapableTransactionalOperation operation,
                                                                 Class<G> exceptionType, boolean useTransaction)
            throws G;

    <G extends Throwable> void runOptionalTransactionalOperation(StreamCapableTransactionalOperation operation,
                                                                 Class<G> exceptionType, boolean useTransaction,
                                                                 int transactionBehavior, int isolationLevel) throws G;

    PlatformTransactionManager getTransactionManager();

    void setTransactionManager(PlatformTransactionManager transactionManager);

}

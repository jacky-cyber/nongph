package cn.globalph.common.util;

/**
 * @author Jeff Fischer
 */
public abstract class StreamCapableTransactionalOperationAdapter implements StreamCapableTransactionalOperation {

    protected Object[] pagedItems;

    @Override
    public void pagedExecute(Object[] param) throws Throwable {
        //do nothing
    }

    @Override
    public void executeAfterCommit(Object[] param) {
        //do nothing
    }

    @Override
    public void execute() throws Throwable {
        //do nothing
    }

    @Override
    public Object[] retrievePage(int startPos, int pageSize) {
        return null;
    }

    @Override
    public Long retrieveTotalCount() {
        return null;
    }

    public Object[] getPagedItems() {
        return pagedItems;
    }

    public void setPagedItems(Object[] pagedItems) {
        this.pagedItems = pagedItems;
    }

    @Override
    public boolean shouldRetryOnTransactionLockAcquisitionFailure() {
        return false;
    }

    @Override
    public int retryMaxCountOverrideForLockAcquisitionFailure() {
        return -1;
    }
}

package cn.globalph.batch.core.notify;

import cn.globalph.batch.core.context.JobContext;

public interface JobResultNotify {
    public void notifyFail(JobContext jobContext, Exception exception);

    public void notifySuccess(JobContext jobContext);
}

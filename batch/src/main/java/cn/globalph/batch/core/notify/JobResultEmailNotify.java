package cn.globalph.batch.core.notify;

import cn.globalph.batch.core.context.JobContext;
import org.springframework.stereotype.Component;

@Component("cn.globalph.batch.core.notify.JobResultEmailNotify")
public class JobResultEmailNotify implements JobResultNotify {
    @Override
    public void notifyFail(JobContext jobContext, Exception exception) {
        //TODO..send email to IT Support
    }

    @Override
    public void notifySuccess(JobContext jobContext) {
        //do nothing
    }
}

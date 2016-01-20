package cn.globalph.batch.Test;

import cn.globalph.batch.core.SingletonJob;
import cn.globalph.batch.core.context.JobContext;
import cn.globalph.batch.core.annotation.JobId;
import org.springframework.stereotype.Component;


@Component("TestJob1")
@JobId("J01")
public class TestJob01 extends SingletonJob {

    @Override
    protected void doExecute(JobContext jobContext) throws Exception {
        Thread.sleep(15000);
    }
}

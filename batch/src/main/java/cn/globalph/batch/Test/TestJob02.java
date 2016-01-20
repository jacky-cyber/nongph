package cn.globalph.batch.Test;

import cn.globalph.batch.core.Job;
import cn.globalph.batch.core.context.JobContext;
import cn.globalph.batch.core.annotation.JobId;
import org.springframework.stereotype.Component;


@Component("TestJob2")
@JobId("J02")
public class TestJob02 extends Job {

    @Override
    protected void doExecute(JobContext jobContext) throws Exception {
        Thread.sleep(15000);
    }
}

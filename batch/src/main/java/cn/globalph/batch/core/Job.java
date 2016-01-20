package cn.globalph.batch.core;

import cn.globalph.batch.core.context.JobContext;
import cn.globalph.batch.core.context.JobContextHolder;
import cn.globalph.batch.core.notify.JobResultNotify;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.UUID;

@PersistJobDataAfterExecution
public abstract class Job extends QuartzJobBean {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Resource(name = "cn.globalph.batch.core.notify.JobResultEmailNotify")
    private JobResultNotify jobResultNotify;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobContext jobContext = new JobContext();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            jobContext = (JobContext) context.getMergedJobDataMap().get("jobContext");
            jobContext.setJobRequestUUID(uuid);
            JobContextHolder.setJobContext(jobContext);
            logger.info("Start Job " + jobContext.getJobId() + ", request uuid " + uuid);
            //todo..log job request
            doExecute(jobContext);
            //todo..update job run status as success
        } catch (Exception e) {
            //todo..update job run status as fail
            jobResultNotify.notifyFail(jobContext, e);
            logger.error(e.getMessage(), e);
            throw new JobExecutionException(e);
        } finally {
            jobResultNotify.notifySuccess(jobContext);
            logger.info("End Job " + jobContext.getJobId() + ", request uuid " + uuid);
            JobContextHolder.clear();
        }
    }

    protected abstract void doExecute(JobContext jobContext) throws Exception;


}

package cn.globalph.batch.core.startup;

import cn.globalph.batch.core.schedule.JobScheduler;
import cn.globalph.batch.core.annotation.JobId;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class BatchJobSchedulingListener implements ApplicationListener<ContextRefreshedEvent>, PriorityOrdered {
    private final Log logger = LogFactory.getLog(BatchJobSchedulingListener.class);

    private ApplicationContext applicationContext;

    @Resource(name = "cn.globalph.batch.core.schedule.JobScheduleFromDBRefresh")
    private JobScheduler jobScheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            applicationContext = event.getApplicationContext();
            jobScheduler.setJobIdAndClassMapping(getJobIdAndClassMapping());
            jobScheduler.scheduleJobs();
        } catch (Exception e) {
            logger.error("Fail to schedule job", e);
        }
    }

    private Map<String, Class> getJobIdAndClassMapping() {
        Map<String, Object> allJobs = applicationContext.getBeansWithAnnotation(JobId.class);
        Map<String, Class> jobIdAndClassMapping = new HashMap<String, Class>();

        for (Map.Entry<String, Object> jobEntry : allJobs.entrySet()) {
            Class jobClass = jobEntry.getValue().getClass();
            JobId jobIdAnnotation = AnnotationUtils.findAnnotation(jobClass, JobId.class);
            jobIdAndClassMapping.put(jobIdAnnotation.value(), jobClass);
        }
        return jobIdAndClassMapping;
    }

    @Override
    public int getOrder() {
        return 100;
    }
}


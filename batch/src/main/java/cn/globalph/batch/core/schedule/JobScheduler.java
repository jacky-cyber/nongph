/*
* =========================================================================
*  Copyright ?2014 NCS Pte. Ltd. All Rights Reserved
*
*  This software is confidential and proprietary to NCS Pte. Ltd. You shall
*  use this software only in accordance with the terms of the license
*  agreement you entered into with NCS.  No aspect or part or all of this
*  software may be reproduced, modified or disclosed without full and
*  direct written authorisation from NCS.
*
*  NCS SUPPLIES THIS SOFTWARE ON AN ?AS IS? BASIS. NCS MAKES NO
*  REPRESENTATIONS OR WARRANTIES, EITHER EXPRESSLY OR IMPLIEDLY, ABOUT THE
*  SUITABILITY OR NON-INFRINGEMENT OF THE SOFTWARE. NCS SHALL NOT BE LIABLE
*  FOR ANY LOSSES OR DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
*  MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
*  =========================================================================
*/
package cn.globalph.batch.core.schedule;

import cn.globalph.batch.core.*;
import cn.globalph.batch.core.Job;
import cn.globalph.batch.core.context.JobContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

public abstract class JobScheduler {
    protected final Log logger = LogFactory.getLog(getClass());

    private final static String JOB_TYPE_SCHEDULED = "S";
    private final static String JOB_STATUS_ACTIVE = "A";

    private Map<String, Class> jobIdAndClassMapping;

    @Resource(name = "stdScheduler")
    private Scheduler scheduler;

    public void scheduleJobs() {
        List<JobDefinition> jobDefinitionList = getJobDefinitionList();
        for (JobDefinition jobDefinition : jobDefinitionList) {
            Class jobClass = jobIdAndClassMapping.get(jobDefinition.getId());
            if (jobClass != null) {
                if (Job.class.isAssignableFrom(jobClass)) {
                    try {
                        this.scheduleJob(jobDefinition, jobClass);
                    } catch (Exception e) {
                        logger.error("Fail to schedule batch job", e);
                    }
                } else {
                    throw new RuntimeException(jobClass + " doesn't implemented " + cn.globalph.batch.core.Job.class);
                }
            }
        }
    }

    public abstract List<JobDefinition> getJobDefinitionList();

    public void scheduleJob(JobDefinition jobDefinition, Class jobClass) throws Exception {

        String jobId = jobDefinition.getId();
        String jobGroup = jobDefinition.getGroup();
        String cron = jobDefinition.getCron();

        TriggerKey triggerKey = TriggerKey.triggerKey(jobId, jobGroup);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        JobContext jobContext = new JobContext();
        jobContext.setJobId(jobId);
        jobContext.setJobName(jobDefinition.getName());

        if (JOB_TYPE_SCHEDULED.equals(jobDefinition.getType()) && StringUtils.isNotEmpty(cron)) {
            if (JOB_STATUS_ACTIVE.equals(jobDefinition.getStatus())) {
                if (trigger == null) {
                    JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobId, jobGroup).build();
                    jobDetail.getJobDataMap().put("jobContext", jobContext);
                    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                    trigger = TriggerBuilder.newTrigger().withIdentity(jobId, jobGroup).withSchedule(scheduleBuilder).build();
                    scheduler.scheduleJob(jobDetail, trigger);
                } else {
                    if (!cron.equalsIgnoreCase(trigger.getCronExpression())) {
                        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                        scheduler.rescheduleJob(triggerKey, trigger);
                    }
                }
            } else if (trigger != null) {
                scheduler.deleteJob(trigger.getJobKey());
            }
        }
    }

    public void setJobIdAndClassMapping(Map<String, Class> jobIdAndClassMapping) {
        this.jobIdAndClassMapping = jobIdAndClassMapping;
    }
}

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("cn.globalph.batch.core.schedule.JobScheduleRefresh")
public class JobScheduleRefresh {
    protected final Log logger = LogFactory.getLog(this.getClass());
    @Resource(name = "cn.globalph.batch.core.schedule.JobScheduleFromDBRefresh")
    private JobScheduler jobScheduler;

    public void refresh() {
        jobScheduler.scheduleJobs();
        logger.debug("Job schedule refreshed!");
    }
}

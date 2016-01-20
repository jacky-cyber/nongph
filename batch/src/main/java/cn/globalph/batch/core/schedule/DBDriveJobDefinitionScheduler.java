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

import cn.globalph.batch.core.JobDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("cn.globalph.batch.core.schedule.JobScheduleFromDBRefresh")
public class DBDriveJobDefinitionScheduler extends JobScheduler {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<JobDefinition> getJobDefinitionList() {
        return jdbcTemplate.query("SELECT * FROM PH_JOB_DEFINITION", new RowMapper<JobDefinition>() {
            @Override
            public JobDefinition mapRow(ResultSet rs, int rowNum) throws SQLException {
                JobDefinition jobDefinition = new JobDefinition();
                jobDefinition.setId(rs.getString("id"));
                jobDefinition.setName(rs.getString("name"));
                jobDefinition.setGroup(rs.getString("group"));
                jobDefinition.setType(rs.getString("type"));
                jobDefinition.setStatus(rs.getString("status"));
                jobDefinition.setCron(rs.getString("cron"));
                return jobDefinition;
            }
        });
    }
}


package cn.globalph.b2c.workflow;

import cn.globalph.b2c.order.service.call.ActivityMessageDTO;

import java.util.List;


public interface ActivityMessages {

    List<ActivityMessageDTO> getActivityMessages();

    void setActivityMessages(List<ActivityMessageDTO> activityMessages);
}

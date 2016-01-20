package cn.globalph.b2c.workflow;

import cn.globalph.b2c.order.service.call.ActivityMessageDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Default ProcessContext implementation
 * @author "Priyesh Patel"
 *
 * @param <T> SeedData
 */

public class DefaultProcessContextImpl<T> implements ProcessContext<T>, ActivityMessages {
    public final static long serialVersionUID = 1L;
    protected T seedData;
    protected boolean stopEntireProcess = false;
    
    protected List<ActivityMessageDTO> activityMessages = new ArrayList<ActivityMessageDTO>();

    public boolean stopProcess() {
        this.stopEntireProcess = true;
        return stopEntireProcess;
    }

    public boolean isStopped() {
        return stopEntireProcess;
    }

    public T getSeedData() {
        return seedData;
    }

    public void setSeedData(T seedObject) {
        seedData = (T) seedObject;
    }

    public List<ActivityMessageDTO> getActivityMessages() {
        return activityMessages;
    }

    public void setActivityMessages(List<ActivityMessageDTO> activityMessages) {
        this.activityMessages = activityMessages;
    }
}

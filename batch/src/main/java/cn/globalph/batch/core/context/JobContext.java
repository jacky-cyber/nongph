package cn.globalph.batch.core.context;

import java.util.HashMap;
import java.util.Map;

public class JobContext {
    private String jobName;
    private String jobId = "Unknown";
    private String jobRequestUUID;

    private Map<String, Object> storageMap = new HashMap<String, Object>();

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobRequestUUID() {
        return jobRequestUUID;
    }

    public void setJobRequestUUID(String jobRequestUUID) {
        this.jobRequestUUID = jobRequestUUID;
    }

    public void put(String key, Object value) {
        storageMap.put(key, value);
    }

    public Object get(String key) {
        return storageMap.get(key);
    }

}

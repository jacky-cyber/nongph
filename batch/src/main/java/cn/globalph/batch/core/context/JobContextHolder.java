package cn.globalph.batch.core.context;

public class JobContextHolder {

    private static ThreadLocal<JobContext> contextThreadLocal = new ThreadLocal<JobContext>();

    public static JobContext getJobContext() {
        JobContext jobContext = contextThreadLocal.get();
        if (jobContext == null) {
            jobContext = new JobContext();
            contextThreadLocal.set(jobContext);
        }
        return jobContext;
    }

    public static void setJobContext(JobContext jobContext) {
        contextThreadLocal.set(jobContext);
    }

    public static void clear() {
        contextThreadLocal.remove();
    }

}

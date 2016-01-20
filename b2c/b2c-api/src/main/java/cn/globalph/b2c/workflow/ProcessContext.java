package cn.globalph.b2c.workflow;

import java.io.Serializable;

/**
 * 流程上下文对象，用于在流程的活动之间传递数据以及控制命令
 * @author felix.wu
 * @param <T> 流程之间传递的数据
 */
public interface ProcessContext<T> extends Serializable {

    /**
     * Activly informs the workflow process to stop processing
     * no further activities will be executed
     *
     * @return whether or not the stop process call was successful
     */
    public boolean stopProcess();

    /**
     * Is the process stopped
     *
     * @return whether or not the process is stopped
     */
    public boolean isStopped();

    /**
     * Provide seed information to this ProcessContext, usually
     * provided at time of workflow kickoff by the containing
     * workflow processor.
     * 
     * @param seedObject - initial seed data for the workflow
     */
    public void setSeedData(T seedObject);

    /**
     * Returns the seed information
     * @return
     */
    public T getSeedData();

}

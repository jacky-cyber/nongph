package cn.globalph.b2c.workflow;

import java.util.List;


public interface Processor {

    public boolean supports(Activity<? extends ProcessContext<?>> activity);
    
    public ProcessContext<?> doActivities() throws WorkflowException;
    
    public ProcessContext<?> doActivities(Object seedData) throws WorkflowException;
    
    public void setActivities(List<Activity<ProcessContext<?>>> activities);
    
    public void setDefaultErrorHandler(ErrorHandler defaultErrorHandler);
    
    public void setProcessContextFactory(ProcessContextFactory<Object, Object> processContextFactory);

}

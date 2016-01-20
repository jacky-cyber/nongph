package cn.globalph.b2c.workflow;

import org.springframework.stereotype.Component;

@Component("blSilentErrorHandler")
public class SilentErrorHandler implements ErrorHandler {

    @SuppressWarnings("unused")
    private String name;

    /* (non-Javadoc)
     * @see cn.globalph.b2c.workflow.ErrorHandler#handleError(cn.globalph.b2c.workflow.ProcessContext, java.lang.Throwable)
     */
    public void handleError(ProcessContext context, Throwable th) throws WorkflowException {
        context.stopProcess();
        throw new WorkflowException(th);
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
     */
    public void setBeanName(String name) {
        this.name = name;
    }

}

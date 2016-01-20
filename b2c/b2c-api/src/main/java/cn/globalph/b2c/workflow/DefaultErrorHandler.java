package cn.globalph.b2c.workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("blDefaultErrorHandler")
public class DefaultErrorHandler implements ErrorHandler {

    private static final Log LOG = LogFactory.getLog(DefaultErrorHandler.class);
    @SuppressWarnings("unused")
    private String name;
    
    protected List<String> unloggedExceptionClasses = new ArrayList<String>();

    /* (non-Javadoc)
     * @see cn.globalph.b2c.workflow.ErrorHandler#handleError(cn.globalph.b2c.workflow.ProcessContext, java.lang.Throwable)
     */
    public void handleError(ProcessContext context, Throwable th) throws WorkflowException {
        context.stopProcess();

        boolean shouldLog = true;
        Throwable cause = th;
        while (true) {
            if (unloggedExceptionClasses.contains(cause.getClass().getName())) {
                shouldLog = false;
                break;
            }
            cause = cause.getCause();
            if (cause == null) {
                break;
            }
        }
        if (shouldLog) {
            LOG.error("An error occurred during the workflow", th);
        }
        
        throw new WorkflowException(th);
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
     */
    public void setBeanName(String name) {
        this.name = name;
    }

    public List<String> getUnloggedExceptionClasses() {
        return unloggedExceptionClasses;
    }

    public void setUnloggedExceptionClasses(List<String> unloggedExceptionClasses) {
        this.unloggedExceptionClasses = unloggedExceptionClasses;
    }
    
}

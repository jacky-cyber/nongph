package cn.globalph.b2c.workflow;

import cn.globalph.common.exception.BroadleafException;

public class WorkflowException extends BroadleafException {

    private static final long serialVersionUID = 1L;

    public WorkflowException() {
        super();
    }

    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkflowException(String message) {
        super(message);
    }

    public WorkflowException(Throwable cause) {
        super(cause);
    }

}

package cn.globalph.openadmin.server.service;

/**
 * @author Jeff Fischer
 */
public class WorkflowRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WorkflowRuntimeException() {
        //do nothing
    }

    public WorkflowRuntimeException(Throwable cause) {
        super(cause);
    }

    public WorkflowRuntimeException(String message) {
        super(message);
    }

    public WorkflowRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}

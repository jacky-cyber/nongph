package cn.globalph.common.exception;

/**
 * Base exception class for BroadleafExceptions that understands root cause messages.
 * 
 * @author bpolster
 */
public abstract class BroadleafException extends Exception implements RootCauseAccessor {

    private Throwable rootCause;

    public BroadleafException() {
        super();
    }

    public BroadleafException(String message, Throwable cause) {
        super(message, cause);
        if (cause != null) {
            rootCause = findRootCause(cause);
        } else {
            rootCause = this;
        }
    }

    private Throwable findRootCause(Throwable cause) {
        Throwable rootCause = cause;
        while (rootCause != null && rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    public BroadleafException(String message) {
        super(message);
        this.rootCause = this;

    }

    public BroadleafException(Throwable cause) {
        super(cause);
        if (cause != null) {
            rootCause = findRootCause(cause);
        }
    }

    public Throwable getRootCause() {
        return rootCause;
    }

    public String getRootCauseMessage() {
        if (rootCause != null) {
            return rootCause.getMessage();
        } else {
            return getMessage();
        }
    }

}

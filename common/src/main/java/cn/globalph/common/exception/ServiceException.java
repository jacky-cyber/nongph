package cn.globalph.common.exception;

/**
 * Exception thrown when a {@link EntityService service} method fails.
 * 
 * @felix.wu
 */
public class ServiceException extends RuntimeException {
    
    private static final long serialVersionUID = -7084792578727995587L;
    
    // for serialization purposes
    protected ServiceException() {
        super();
    }
    
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Checks to see if any of the causes of the chain of exceptions that led to this ServiceException are an instance
     * of the given class.
     * 
     * @param clazz
     * @return whether or not this exception's causes includes the given class.
     */
    public boolean containsCause(Class<? extends Throwable> clazz) {
        Throwable current = this;

        do {
            if (clazz.isAssignableFrom(current.getClass())) {
                return true;
            }
            current = current.getCause();
        } while (current != null && current.getCause() != null);

        return false;
    }

}

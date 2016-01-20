package cn.globalph.b2c.order.service.exception;


public abstract class IllegalCartOperationException extends RuntimeException {

    private static final long serialVersionUID = 5113456015951023947L;

    protected IllegalCartOperationException() {
        super();
    }
    
    public IllegalCartOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public IllegalCartOperationException(String message) {
        super(message);
    }
    
    public IllegalCartOperationException(Throwable cause) {
        super(cause);
    }
    
    public abstract String getType();

}

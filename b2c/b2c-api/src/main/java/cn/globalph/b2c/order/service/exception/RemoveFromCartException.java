package cn.globalph.b2c.order.service.exception;

public class RemoveFromCartException extends Exception {

    private static final long serialVersionUID = 1L;

    public RemoveFromCartException() {
        super();
    }

    public RemoveFromCartException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoveFromCartException(String message) {
        super(message);
    }

    public RemoveFromCartException(Throwable cause) {
        super(cause);
    }

}

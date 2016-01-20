package cn.globalph.b2c.order.service.exception;

public class UpdateCartException extends Exception {

    private static final long serialVersionUID = 1L;

    public UpdateCartException() {
        super();
    }

    public UpdateCartException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateCartException(String message) {
        super(message);
    }

    public UpdateCartException(Throwable cause) {
        super(cause);
    }

}

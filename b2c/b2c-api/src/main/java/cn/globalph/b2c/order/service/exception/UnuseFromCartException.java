package cn.globalph.b2c.order.service.exception;

public class UnuseFromCartException extends Exception {

    private static final long serialVersionUID = 1L;

    public UnuseFromCartException() {
        super();
    }

    public UnuseFromCartException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnuseFromCartException(String message) {
        super(message);
    }

    public UnuseFromCartException(Throwable cause) {
        super(cause);
    }

}

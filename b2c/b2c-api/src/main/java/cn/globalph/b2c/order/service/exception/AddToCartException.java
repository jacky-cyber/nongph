package cn.globalph.b2c.order.service.exception;

public class AddToCartException extends Exception {

    private static final long serialVersionUID = 1L;

    public AddToCartException() {
        super();
    }

    public AddToCartException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddToCartException(String message) {
        super(message);
    }

    public AddToCartException(Throwable cause) {
        super(cause);
    }

}

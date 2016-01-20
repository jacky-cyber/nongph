package cn.globalph.b2c.payment.service.exception;

public class InsufficientFundsException extends PaymentException {

    private static final long serialVersionUID = 1L;

    public InsufficientFundsException() {
        super();
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(Throwable cause) {
        super(cause);
    }

}

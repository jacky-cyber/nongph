package cn.globalph.common.vendor.service.exception;

import cn.globalph.common.exception.BroadleafException;

public class PaymentException extends BroadleafException {

    private static final long serialVersionUID = 1L;

    public PaymentException() {
        super();
    }

    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentException(String message) {
        super(message);
    }

    public PaymentException(Throwable cause) {
        super(cause);
    }

}

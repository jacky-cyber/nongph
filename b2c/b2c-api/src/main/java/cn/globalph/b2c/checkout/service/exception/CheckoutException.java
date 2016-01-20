package cn.globalph.b2c.checkout.service.exception;

import cn.globalph.b2c.checkout.service.workflow.CheckoutResponse;
import cn.globalph.b2c.checkout.service.workflow.CheckoutSeed;
import cn.globalph.common.exception.BroadleafException;

public class CheckoutException extends BroadleafException {

    private static final long serialVersionUID = 1L;
    
    private CheckoutResponse checkoutResponse;

    public CheckoutException() {
        super();
    }

    public CheckoutException(String message, CheckoutSeed seed) {
        super(message);
        checkoutResponse = seed;
    }

    public CheckoutException(Throwable cause, CheckoutSeed seed) {
        super(cause);
        checkoutResponse = seed;
    }

    public CheckoutException(String message, Throwable cause, CheckoutSeed seed) {
        super(message, cause);
        checkoutResponse = seed;
    }

    public CheckoutResponse getCheckoutResponse() {
        return checkoutResponse;
    }

    public void setCheckoutResponse(CheckoutResponse checkoutResponse) {
        this.checkoutResponse = checkoutResponse;
    }

}

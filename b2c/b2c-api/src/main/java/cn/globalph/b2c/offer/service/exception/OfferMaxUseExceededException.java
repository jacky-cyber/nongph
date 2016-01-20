package cn.globalph.b2c.offer.service.exception;

import cn.globalph.b2c.checkout.service.exception.CheckoutException;
import cn.globalph.b2c.checkout.service.workflow.CheckoutSeed;

public class OfferMaxUseExceededException extends CheckoutException {

    private static final long serialVersionUID = 1L;

    public OfferMaxUseExceededException() {
        super();
    }
    
    public OfferMaxUseExceededException(String message) {
        super(message, null);
    }

    public OfferMaxUseExceededException(String message, Throwable cause, CheckoutSeed seed) {
        super(message, cause, seed);
    }

    public OfferMaxUseExceededException(String message, CheckoutSeed seed) {
        super(message, seed);
    }

    public OfferMaxUseExceededException(Throwable cause, CheckoutSeed seed) {
        super(cause, seed);
    }

}

package cn.globalph.common.vendor.service.exception;

import cn.globalph.common.vendor.service.message.TaxResponse;

public class TaxException extends Exception {

    private static final long serialVersionUID = 1L;

    protected TaxResponse taxResponse;

    public TaxException() {
        super();
    }

    public TaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaxException(String message) {
        super(message);
    }

    public TaxException(Throwable cause) {
        super(cause);
    }

    public TaxResponse getTaxResponse() {
        return taxResponse;
    }

    public void setTaxResponse(TaxResponse taxResponse) {
        this.taxResponse = taxResponse;
    }
}

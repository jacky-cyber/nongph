package cn.globalph.passport.service.exception;


public class AddressVerificationException extends Exception {

    private static final long serialVersionUID = 1L;

    public AddressVerificationException() {
        super();
    }

    public AddressVerificationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public AddressVerificationException(String arg0) {
        super(arg0);
    }

    public AddressVerificationException(Throwable arg0) {
        super(arg0);
    }

}

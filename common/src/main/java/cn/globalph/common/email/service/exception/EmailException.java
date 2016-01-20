package cn.globalph.common.email.service.exception;

/**
 * @felix.wu
 *
 */
public class EmailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailException() {
        super();
    }

    public EmailException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public EmailException(String arg0) {
        super(arg0);
    }

    public EmailException(Throwable arg0) {
        super(arg0);
    }
}

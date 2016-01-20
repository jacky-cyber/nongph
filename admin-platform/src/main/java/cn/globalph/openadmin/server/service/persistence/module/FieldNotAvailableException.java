package cn.globalph.openadmin.server.service.persistence.module;

/**
 * @author Jeff Fischer
 */
public class FieldNotAvailableException extends Exception {

    public FieldNotAvailableException() {
    }

    public FieldNotAvailableException(String s) {
        super(s);
    }

    public FieldNotAvailableException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public FieldNotAvailableException(Throwable throwable) {
        super(throwable);
    }

}

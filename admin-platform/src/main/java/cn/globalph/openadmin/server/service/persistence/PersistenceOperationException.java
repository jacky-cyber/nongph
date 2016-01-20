package cn.globalph.openadmin.server.service.persistence;

/**
 * @author Jeff Fischer
 */
public class PersistenceOperationException extends RuntimeException {

	private static final long serialVersionUID = 3930734804288677041L;

	public PersistenceOperationException() {
        super();
    }

    public PersistenceOperationException(Throwable cause) {
        super(cause);
    }

    public PersistenceOperationException(String message) {
        super(message);
    }

    public PersistenceOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}

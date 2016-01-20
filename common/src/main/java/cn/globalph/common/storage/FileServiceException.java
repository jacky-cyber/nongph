package cn.globalph.common.storage;

import cn.globalph.common.storage.service.StorageService;

/**
 * Marker exception that just extends RuntimeException to be used by the {@link StorageService}
 * @author bpolster
 *
 */
public class FileServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileServiceException() {
        super();
    }

    public FileServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileServiceException(String message) {
        super(message);
    }

    public FileServiceException(Throwable cause) {
        super(cause);
    }
}

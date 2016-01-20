package cn.globalph.cms.common;

import cn.globalph.cms.file.service.StaticAssetService;


/**
 * Exception thrown by the {@link StaticAssetService} indicating that the asset requested does not exist.
 * 
 * @author Andre Azzolini (apazzolini)
 */
public class AssetNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -6349160176427682630L;

    public AssetNotFoundException() {
        //do nothing
    }

    public AssetNotFoundException(Throwable cause) {
        super(cause);
    }

    public AssetNotFoundException(String message) {
        super(message);
    }

    public AssetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}

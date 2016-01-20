
package cn.globalph.common.sitemap.exception;

/**
 * Sitemap exception
 * 
 * @author Joshua Skorton (jskorton)
 */
public class SiteMapException extends RuntimeException {

    /**
     * 
     */
    public SiteMapException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public SiteMapException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public SiteMapException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public SiteMapException(Throwable cause) {
        super(cause);
    }

}

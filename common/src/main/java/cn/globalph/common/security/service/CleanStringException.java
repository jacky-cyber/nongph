package cn.globalph.common.security.service;

import cn.globalph.common.exception.ServiceException;

import org.owasp.validator.html.CleanResults;

/**
 * @author Jeff Fischer
 */
public class CleanStringException extends ServiceException {

    public CleanStringException(CleanResults cleanResults) {
        this.cleanResults = cleanResults;
    }

    protected CleanResults cleanResults;

    public CleanResults getCleanResults() {
        return cleanResults;
    }

    public void setCleanResults(CleanResults cleanResults) {
        this.cleanResults = cleanResults;
    }
}

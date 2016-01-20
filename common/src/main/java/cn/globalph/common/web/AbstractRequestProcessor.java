package cn.globalph.common.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.WebRequest;

/**
 * 
 * @author felix.wu
 *
 */
public abstract class AbstractRequestProcessor implements RequestProcessor {
    
	protected final Log LOG = LogFactory.getLog(getClass());
	
    public void postProcess(WebRequest request) {
    }

}

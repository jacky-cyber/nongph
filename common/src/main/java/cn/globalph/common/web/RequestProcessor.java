package cn.globalph.common.web;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * 过滤逻辑处理器，独立于具体的过滤器(例如，Servlet filter、 Spring拦截器)
 * @see {@link NativeWebRequest}
 * @see {@link ServletWebRequest}
 * @see {@link org.springframework.web.portlet.context.PortletWebRequest}
 * @see {@link WebRequestFilter}
 * @author felix.wu
 */
public interface RequestProcessor {

    /**
     * Process the current request. Examples would be setting the currently logged in customer on the request or handling
     * anonymous customers in session
     * 
     * @param request
     */
    public void process(WebRequest request);
    
    /**
     * Should be called if work needs to be done after the request has been processed.
     * 
     * @param request
     */
    public void postProcess(WebRequest request);

}

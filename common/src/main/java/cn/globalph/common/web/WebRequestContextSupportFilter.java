package cn.globalph.common.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.RequestDTOImpl;
import cn.globalph.common.exception.SiteNotFoundException;
import cn.globalph.common.web.exception.HaltFilterChainException;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Responsible for setting up the WebRequestContext.
 * @felix.wu
 */
@Component("webRequestContextSupportFilter")
public class WebRequestContextSupportFilter extends OncePerRequestFilter {

    private final Log LOG = LogFactory.getLog(getClass());

    /**
     * Parameter/Attribute name for the current language
     */
    static final String REQUEST_DTO_PARAM_NAME = "blRequestDTO";

    static final String ADMIN_USER_ID_PARAM_NAME = "blAdminUserId";

    // Properties to manage URLs that will not be processed by this filter.
    private static final String BLC_ADMIN_GWT = "cn.globalph.admin";
    private static final String BLC_ADMIN_PREFIX = "blcadmin";
    private static final String BLC_ADMIN_SERVICE = ".service";
    
    private String[] ignoreSuffixList = { ".aif", ".aiff", ".asf", ".avi", ".bin", ".bmp", ".css", ".doc", ".eps", ".gif", ".hqx", ".js", ".jpg", ".jpeg", ".mid", ".midi", ".mov", ".mp3", ".mpg", ".mpeg", ".p65", ".pdf", ".pic", ".pict", ".png", ".ppt", ".psd", ".qxd", ".ram", ".ra", ".rm", ".sea", ".sit", ".stk", ".swf", ".tif", ".tiff", ".txt", ".rtf", ".vob", ".wav", ".wmf", ".xls", ".zip" };;
    
    @Resource(name = "blRequestProcessor")
    protected WebRequestContextSupportRequestProcessor requestProcessor;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (!shouldProcessURL(request, request.getRequestURI())) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Process URL not processing URL " + request.getRequestURI());
            }
            filterChain.doFilter(request, response);
            return;
        }
        
        if (LOG.isTraceEnabled()) {
            String requestURIWithoutContext;

            if (request.getContextPath() != null) {
                requestURIWithoutContext = request.getRequestURI().substring(request.getContextPath().length());
            } else {
                requestURIWithoutContext = request.getRequestURI();
            }

            // Remove JSESSION-ID or other modifiers
            int pos = requestURIWithoutContext.indexOf(";");
            if (pos >= 0) {
                requestURIWithoutContext = requestURIWithoutContext.substring(0, pos);
            }

            LOG.trace("Process URL Filter Begin " + requestURIWithoutContext);
        }

        if (request.getAttribute(REQUEST_DTO_PARAM_NAME) == null) {
            request.setAttribute(REQUEST_DTO_PARAM_NAME, new RequestDTOImpl(request));
        }

        try {
            requestProcessor.process(new ServletWebRequest(request, response));
            filterChain.doFilter(request, response);
        } catch (HaltFilterChainException e) {
            return;
        } catch (SiteNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } finally {
            requestProcessor.postProcess(new ServletWebRequest(request, response));
        }
    }

    /**
     * Determines if the passed in URL should be processed by the content management system.
     * <p/>
     * By default, this method returns false for any BLC-Admin URLs and service calls and for all common image/digital mime-types (as determined by an internal call to {@code getIgnoreSuffixes}.
     * <p/>
     * This check is called with the {@code doFilterInternal} method to short-circuit the content processing which can be expensive for requests that do not require it.
     * 
     * @param requestURI
     *            - the HttpServletRequest.getRequestURI
     * @return true if the {@code HttpServletRequest} should be processed
     */
    private boolean shouldProcessURL(HttpServletRequest request, String requestURI) {
        if( requestURI.contains(BLC_ADMIN_GWT) || 
        		requestURI.endsWith(BLC_ADMIN_SERVICE) || 
        		requestURI.contains(BLC_ADMIN_PREFIX)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("ignoring admin request URI " + requestURI);
            }
            return false;
        } else {
            int pos = requestURI.lastIndexOf(".");
            if (pos > 0) {
                String suffix = requestURI.substring(pos);
                if ( isIgnoreSuffixes(suffix.toLowerCase() ) ) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("ignoring request due to suffix " + requestURI);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns a set of suffixes that can be ignored by content processing. The following are returned:
     * <p/>
     * <B>List of suffixes ignored:</B>
     * 
     * ".aif", ".aiff", ".asf", ".avi", ".bin", ".bmp", ".doc", ".eps", ".gif", ".hqx", ".jpg", ".jpeg", ".mid", ".midi", ".mov", ".mp3", ".mpg", ".mpeg", ".p65", ".pdf", ".pic", ".pict", ".png", ".ppt", ".psd", ".qxd", ".ram", ".ra", ".rm", ".sea", ".sit", ".stk", ".swf", ".tif", ".tiff", ".txt", ".rtf", ".vob", ".wav", ".wmf", ".xls", ".zip";
     * 
     */
    private boolean isIgnoreSuffixes(String suffix) {
    	for( String s : ignoreSuffixList ) {
    		if( s.equals( suffix ) )
    			return true;
    	}
    	return false;
    }
}

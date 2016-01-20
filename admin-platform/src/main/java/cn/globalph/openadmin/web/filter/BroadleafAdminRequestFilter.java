package cn.globalph.openadmin.web.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.exception.SiteNotFoundException;
import cn.globalph.common.web.RequestProcessor;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Responsible for setting the necessary attributes on the BroadleafRequestContext
 * 
 * @author Andre Azzolini (apazzolini)
 */
@Component("blAdminRequestFilter")
public class BroadleafAdminRequestFilter extends AbstractBroadleafAdminRequestFilter {

    private final Log LOG = LogFactory.getLog(BroadleafAdminRequestFilter.class);

    @Resource(name = "blAdminRequestProcessor")
    protected RequestProcessor requestProcessor;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        if (!shouldProcessURL(request, request.getRequestURI())) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Process URL not processing URL " + request.getRequestURI());
            }
            filterChain.doFilter(request, response);
            return;
        }

        try {
            requestProcessor.process(new ServletWebRequest(request, response));
            filterChain.doFilter(request, response);
        } catch (SiteNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } finally {
            requestProcessor.postProcess(new ServletWebRequest(request, response));
        }
    }
}

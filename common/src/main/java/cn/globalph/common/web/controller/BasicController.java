package cn.globalph.common.web.controller;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.common.web.WebRequestContext;
import cn.globalph.common.web.deeplink.DeepLink;
import cn.globalph.common.web.deeplink.DeepLinkService;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An abstract controller that provides convenience methods and resource declarations for its  children
 * 
 * Operations that are shared between all controllers belong here.   To use composition rather than
 * extension, implementors can utilize BroadleafControllerUtility.
 * 
 * @see ControllerUtility
 */
public abstract class BasicController {
    
    
    /**
     * A helper method that returns whether or not the given request was invoked via an AJAX call
     * 
     * @param request
     * @return - whether or not it was an AJAX request
     */
    protected boolean isAjaxRequest(HttpServletRequest request) {
        return ControllerUtility.isAjaxRequest(request);       
    }
    
    /**
     * Returns the current servlet context path. This will return a "/" if the application is
     * deployed as root. If it's not deployed as root, it will return the context path BOTH a 
     * leading slash but without a trailing slash.
     * 
     * @param request
     * @return the context path
     */
    protected String getContextPath(HttpServletRequest request) {
        String ctxPath = request.getContextPath();
        if (StringUtils.isBlank(ctxPath)) {
            return "/";
        } else {
            if (ctxPath.charAt(0) != '/') {
                ctxPath = '/' + ctxPath;
            }
            if (ctxPath.charAt(ctxPath.length() - 1) != '/') {
                ctxPath = ctxPath + '/';
            }
            
            return ctxPath;
        }
        
    }
    
    protected <T> void addDeepLink(ModelAndView model, DeepLinkService<T> service, T item) {
        if (service == null) {
            return;
        }

        WebRequestContext brc = WebRequestContext.getWebRequestContext();
        if (brc.getSandBox() != null) {
            List<DeepLink> links = service.getLinks(item);
            if (links.size() == 1) {
                model.addObject("adminDeepLink", links.get(0));
            } else {
                model.addObject("adminDeepLink", links);
            }
        }
    }
    
    /**
     * Typically, controller methods are set to return a String that points to the necessary template path.
     * 
     * However, there may be occasions where the error state for a controller action should instead return
     * JSON instead of a fully rendered template. This convenience method will achieve that by setting the
     * appropriate headers and serializing the given map.
     * 
     * @param response
     * @param responseMap
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    protected String jsonResponse(HttpServletResponse response, Map<?, ?> responseMap) 
            throws JsonGenerationException, JsonMappingException, IOException {
        response.setHeader("Content-Type", "application/json");
        new ObjectMapper().writeValue(response.getWriter(), responseMap);
        return null;
    }

}

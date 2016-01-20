package cn.globalph.common.web.filter;

import cn.globalph.common.web.resource.WebResourceHttpRequestHandler;

import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class is used to control the cache seconds. The default Spring WebContentInterceptor will apply the cacheSeconds 
 * and associated variables to everything that goes through Spring MVC. We only want to apply the configured cache settings
 * to requests that go through controllers. For static resources, we will let Spring use its defaults.
 * 
 * Additionally, for requests for files that are known bundles, we will cache for a full 10 years, as we are generating
 * unique filenames that will make this acceptable.
 * 
 * @author felix.wu
 */
public class ResourceWebContentInterceptor extends WebContentInterceptor {
    
    protected static final int TEN_YEARS_SECONDS = 60 * 60 * 24 * 365 * 10;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws ServletException {
        if (WebResourceHttpRequestHandler.class.isAssignableFrom(handler.getClass())) {
            // Bundle resources are cached for ten years
            WebResourceHttpRequestHandler h = (WebResourceHttpRequestHandler) handler;
            if (h.isBundleRequest(request)) {
                applyCacheSeconds(response, TEN_YEARS_SECONDS);
            }
            return true;
        } else if (ResourceHttpRequestHandler.class.isAssignableFrom(handler.getClass())) {
            // Non-bundle resources will not specify cache parameters - we will rely on the server responding
            // with a 304 if the resource hasn't been modified.
            return true;
        } else {
            // Non-resources (meaning requests that go to controllers) will apply the configured caching parameters.
            return super.preHandle(request, response, handler);
        }
    }

}

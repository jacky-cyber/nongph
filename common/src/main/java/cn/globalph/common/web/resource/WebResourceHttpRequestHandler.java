package cn.globalph.common.web.resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.classloader.release.ThreadLocalManager;
import cn.globalph.common.extension.ExtensionResultHolder;
import cn.globalph.common.resource.GeneratedResource;
import cn.globalph.common.resource.service.ResourceBundlingService;
import cn.globalph.common.resource.service.ResourceMinificationService;
import cn.globalph.common.web.BroadleafSandBoxResolver;
import cn.globalph.common.web.BroadleafThemeResolver;
import cn.globalph.common.web.SiteResolver;
import cn.globalph.common.web.WebRequestContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 先交由handlers进行处理，动态生成Resource；
 * 如果handlers无法处理，那么根据locations返回对应Resource.
 * @author fwu
 *
 */
public class WebResourceHttpRequestHandler extends ResourceHttpRequestHandler {
    private static final Log LOG = LogFactory.getLog(WebResourceHttpRequestHandler.class);
    
    // XML Configured generated resource handlers
    private List<AbstractGeneratedResourceHandler> handlers;
    private List<AbstractGeneratedResourceHandler> sortedHandlers;
    
    @javax.annotation.Resource(name = "blResourceBundlingService")
    private ResourceBundlingService bundlingService;

    @javax.annotation.Resource(name = "blResourceMinificationService")
    private ResourceMinificationService minifyService;
    
    @javax.annotation.Resource(name = "blResourceRequestExtensionManager")
    private ResourceRequestExtensionManager extensionManager;
    
    @javax.annotation.Resource(name = "blSiteResolver")
    private SiteResolver siteResolver;
    
    @javax.annotation.Resource(name = "blSandBoxResolver")
    private BroadleafSandBoxResolver sbResolver;
    
    @javax.annotation.Resource(name = "blThemeResolver")
    private BroadleafThemeResolver themeResolver;

    @Value("${global.admin.prefix}")
    private String globalAdminPrefix;

    @Value("${global.admin.url}")
    private String globalAdminUrl;
    
    /**
     * Checks to see if the requested path corresponds to a registered bundle. If so, returns the generated bundle.
     * Otherwise, checks to see if any of the configured GeneratedResourceHandlers can handle the given request.
     * If neither of those cases match, delegates to the normal ResourceHttpRequestHandler
     */
    @Override
	protected Resource getResource(HttpServletRequest request) {
        establishThinRequestContext();
        return getResourceInternal(request);
    }

	protected Resource getResourceInternal(HttpServletRequest request) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if (bundlingService.hasBundle(path)) {
            return bundlingService.getBundle(path);
        }
        
        Resource unminifiedResource = null;
        
        if (sortedHandlers == null && handlers != null) {
            sortHandlers();
        }
        
        if (sortedHandlers != null) {
            for (AbstractGeneratedResourceHandler handler : sortedHandlers) {
                if (handler.canHandle(path)) {
                    unminifiedResource = handler.getResource(path, getLocations());
                    break;
                }
            }
        }
        
        if (unminifiedResource == null) {
            ExtensionResultHolder<Resource> erh = new ExtensionResultHolder<Resource>();
            extensionManager.getHandlerProxy().getOverrideResource(path, erh);
            if (erh.getContextMap().get(ResourceRequestExtensionHandler.RESOURCE_ATTR) != null) {
                unminifiedResource = (Resource) erh.getContextMap().get(ResourceRequestExtensionHandler.RESOURCE_ATTR);
            }
        }
        
        if (unminifiedResource == null) {
            unminifiedResource = super.getResource(request);
        }

        try {
            if (!minifyService.getEnabled() || !minifyService.getAllowSingleMinification()) {
                return unminifiedResource;
            }
        } finally {
            ThreadLocalManager.remove();
        }

        LOG.warn("Minifying individual file - this should only be used in development to trace down particular " +
        		 "files that are causing an exception in the minification service. The results of the minification " +
        		 "performed outside of a bundle are not stored to disk.");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = null;
        InputStream is = null;
        try {
            is = unminifiedResource.getInputStream();
            StreamUtils.copy(is, baos);
            bytes = baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
                baos.close();
            } catch (IOException e2) {
                throw new RuntimeException("Could not close input stream", e2);
            }
        }
        
        LOG.debug("Attempting to minifiy " + unminifiedResource.getFilename());
        byte[] minifiedBytes = minifyService.minify(unminifiedResource.getFilename(), bytes);

        return new GeneratedResource(minifiedBytes, unminifiedResource.getFilename());
    }
    
    public boolean isBundleRequest(HttpServletRequest request) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        return bundlingService.hasBundle(path);
    }
        
    /**
     * @return a clone of the locations list that is in {@link ResourceHttpRequestHandler}. Note that we must use
     * reflection to access this field as it is marked private.
     */
    @SuppressWarnings("unchecked")
    public List<Resource> getLocations() {
        try {
            List<Resource> locations = (List<Resource>) FieldUtils.readField(this, "locations", true);
            return new ArrayList<Resource>(locations);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected void establishThinRequestContext() {
        WebRequestContext oldBrc = WebRequestContext.getWebRequestContext();
        if (oldBrc == null || oldBrc.getSite() == null || oldBrc.getTheme() == null) {
            // Resolving sites and sandboxes is often dependent on having a security context present in the request.
            // For example, resolving a sandbox requires the current user to have the BLC_ADMIN_USER in his Authentication.
            // For performance reasons, we do not go through the entire Spring Security filter chain on requests
            // for resources like JavaScript and CSS files. However, when theming is enabled, we potentially have to
            // resolve a specific version of the theme for a sandbox so that we can replace variables appropriately. This
            // then depends on the sandbox being resolved, which requires the Authentication object to be present.
            // We will grab the Authentication object associated with this user's session and set it on the
            // SecurityContextHolder since Spring Security will be bypassed.
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = req.getSession(false);
            SecurityContext ctx = readSecurityContextFromSession(session);
            if (ctx != null) {
                SecurityContextHolder.setContext(ctx);
            }
            
            WebRequestContext newBrc = new WebRequestContext();
            if (!isGlobalAdmin(req)) {
                ServletWebRequest swr = new ServletWebRequest(req);
                newBrc.setSite(siteResolver.resolveSite(swr, true));
                newBrc.setSandBox(sbResolver.resolveSandBox(swr, newBrc.getSite()));
                WebRequestContext.setWebRequestContext(newBrc);
                newBrc.setTheme(themeResolver.resolveTheme(swr));
            }
        }
    }
    
    protected boolean isGlobalAdmin(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (!StringUtils.isEmpty(globalAdminPrefix)) {
            if (globalAdminPrefix.equals(getContextName(request))) {
                return true;
            } else {
                if (!StringUtils.isEmpty(globalAdminUrl)) {
                    return uri.startsWith(globalAdminUrl);
                }
            }
        }
        return false;
    }

    protected String getContextName(HttpServletRequest request) {
        String contextName = request.getServerName();
        int pos = contextName.indexOf('.');
        if (pos >= 0) {
            contextName = contextName.substring(0, contextName.indexOf('.'));
        }
        return contextName;
    }

    // **NOTE** This method is lifted from HttpSessionSecurityContextRepository
    protected SecurityContext readSecurityContextFromSession(HttpSession httpSession) {
        if (httpSession == null) {
            return null;
        }

        Object ctxFromSession = httpSession.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        if (ctxFromSession == null) {
            return null;
        }

        if (!(ctxFromSession instanceof SecurityContext)) {
            return null;
        }

        return (SecurityContext) ctxFromSession;
    }
    
    
    protected void sortHandlers() {
        sortedHandlers = new ArrayList<AbstractGeneratedResourceHandler>(handlers);
        Collections.sort(sortedHandlers, new Comparator<AbstractGeneratedResourceHandler>() {
            @Override
            public int compare(AbstractGeneratedResourceHandler o1, AbstractGeneratedResourceHandler o2) {
                return new Integer(o1.getOrder()).compareTo(o2.getOrder());
            }
        });
    }
    
    
    /* *********** */
    /* BOILERPLATE */
    /* *********** */
    
    public List<AbstractGeneratedResourceHandler> getHandlers() {
        if (sortedHandlers == null && handlers != null) {
            sortHandlers();
        }
        return sortedHandlers;
    }
    
    public void setHandlers(List<AbstractGeneratedResourceHandler> handlers) {
        this.handlers = handlers;
    }

}

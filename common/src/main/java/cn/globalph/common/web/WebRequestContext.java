package cn.globalph.common.web;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.RequestDTO;
import cn.globalph.common.classloader.release.ThreadLocalManager;
import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.sandbox.domain.SandBoxType;
import cn.globalph.common.site.domain.Catalog;
import cn.globalph.common.site.domain.Site;
import cn.globalph.common.site.domain.Theme;

import org.springframework.context.MessageSource;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Convenient holder class for various objects to be automatically available on thread local without invoking the various
 * services yourself
 * 
 * @see {@link RequestProcessor}
 */
public class WebRequestContext {
    
    protected static final Log LOG = LogFactory.getLog(WebRequestContext.class);
    
    private static final ThreadLocal<WebRequestContext> WEB_REQUEST_CONTEXT = ThreadLocalManager.createThreadLocal(WebRequestContext.class);
    
    public static WebRequestContext getWebRequestContext() {
        return WEB_REQUEST_CONTEXT.get();
    }
    
    public static void setWebRequestContext(WebRequestContext webRequestContext) {
        WEB_REQUEST_CONTEXT.set(webRequestContext);
    }
    
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected WebRequest webRequest;
    protected SandBox sandBox;
    protected Site site;
    protected Theme theme;
    protected Catalog currentCatalog;
    protected Boolean ignoreSite = false;
    protected Map<String, Object> additionalProperties = new HashMap<String, Object>();
    protected MessageSource messageSource;
    protected RequestDTO requestDTO;
    protected Boolean isAdmin = false;
    protected Long adminUserId;

    protected Boolean internalIgnoreFilters = false;

    /**
     * Gets the current request on the context
     * @return
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Sets the current request on the context. Note that this also invokes {@link #setWebRequest(WebRequest)} by wrapping
     * <b>request</b> in a {@link ServletWebRequest}.
     * 
     * @param request
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
        this.webRequest = new ServletWebRequest(request);
    }

    /**
     * Returns the response for the context
     * 
     * @return
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Sets the response on the context
     * 
     * @param response
     */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Sets the generic request on the context. This is available to be used in non-Servlet environments (like Portlets).
     * Note that if <b>webRequest</b> is an instance of {@link ServletWebRequest} then
     * {@link #setRequest(HttpServletRequest)} will be invoked as well with the native underlying {@link HttpServletRequest}
     * passed as a parameter.
     * <br />
     * <br />
     * Also, if <b>webRequest</b> is an instance of {@link ServletWebRequest} then an attempt is made to set the response
     * (note that this could be null if the ServletWebRequest was not instantiated with both the {@link HttpServletRequest}
     * and {@link HttpServletResponse}
     * @param webRequest
     */
    public void setWebRequest(WebRequest webRequest) {
        this.webRequest = webRequest;
        if (webRequest instanceof ServletWebRequest) {
            this.request = ((ServletWebRequest) webRequest).getRequest();
            setResponse(((ServletWebRequest) webRequest).getResponse());
        }
    }

    /**
     * Returns the generic request for use outside of servlets (like in Portlets). This will be automatically set
     * by invoking {@link #setRequest(HttpServletRequest)}
     * 
     * @return the generic request
     * @see {@link #setWebRequest(WebRequest)}
     */
    public WebRequest getWebRequest() {
        return webRequest;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public SandBox getSandBox() {
        return sandBox;
    }

    public Long getSandBoxId() {
        if (sandBox != null) {
            return sandBox.getId();
        }
        return null;
    }

    public boolean isProductionSandBox() {
        return sandBox == null || SandBoxType.PRODUCTION == sandBox.getSandBoxType();
    }

    public void setSandBox(SandBox sandBox) {
        this.sandBox = sandBox;
    }

    public String getRequestURIWithoutContext() {
        String requestURIWithoutContext = null;

        if (request.getRequestURI() != null) {
            if (request.getContextPath() != null) {
                requestURIWithoutContext = request.getRequestURI().substring(request.getContextPath().length());
            } else {
                requestURIWithoutContext = request.getRequestURI();
            }

            // Remove JSESSION-ID or other modifiers
            int pos = requestURIWithoutContext.indexOf(";");
            if (pos >= 0) {
                requestURIWithoutContext = requestURIWithoutContext.substring(0,pos);
            }
        }
        
        return requestURIWithoutContext;
        
    }
    
    public boolean isSecure() {
        boolean secure = false;
        if (request != null) {
             secure = ("HTTPS".equalsIgnoreCase(request.getScheme()) || request.isSecure());
        }
        return secure;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Catalog getCurrentCatalog() {
        return currentCatalog;
    }

    public void setCurrentCatalog(Catalog currentCatalog) {
        this.currentCatalog = currentCatalog;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String[]> getRequestParameterMap() {
        return getWebRequestContext().getRequest().getParameterMap();
    }

    public Boolean getIgnoreSite() {
        return ignoreSite;
    }

    public void setIgnoreSite(Boolean ignoreSite) {
        this.ignoreSite = ignoreSite;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public RequestDTO getRequestDTO() {
        return requestDTO;
    }

    public void setRequestDTO(RequestDTO requestDTO) {
        this.requestDTO = requestDTO;
    }

    public Boolean getAdmin() {
        return isAdmin == null ? false : isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }

    /**
     * Intended for internal use only
     */
    public Boolean getInternalIgnoreFilters() {
        return internalIgnoreFilters;
    }

    /**
     * Intended for internal use only
     */
    public void setInternalIgnoreFilters(Boolean internalIgnoreFilters) {
        this.internalIgnoreFilters = internalIgnoreFilters;
    }
}

package cn.globalph.common.web;

import org.apache.commons.lang3.StringUtils;

import cn.globalph.common.RequestDTO;
import cn.globalph.common.RequestDTOImpl;
import cn.globalph.common.classloader.release.ThreadLocalManager;
import cn.globalph.common.extension.ExtensionManager;
import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.site.domain.Site;
import cn.globalph.common.site.domain.Theme;
import cn.globalph.common.web.exception.HaltFilterChainException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 对thread local的WebRequestContext进行创建和销毁。
 * @author felix.wu
 */
@Component("blRequestProcessor")
public class WebRequestContextSupportRequestProcessor extends AbstractRequestProcessor {

    private static final String REQUEST_DTO_PARAM_NAME = WebRequestContextSupportFilter.REQUEST_DTO_PARAM_NAME;
    public static final String REPROCESS_PARAM_NAME = "REPROCESS_BLC_REQUEST";
    public static final String SITE_ENFORCE_PRODUCTION_WORKFLOW_KEY = "site.enforce.production.workflow.update";

    @Resource(name = "blSiteResolver")
    protected SiteResolver siteResolver;

    @Resource(name = "blSandBoxResolver")
    protected BroadleafSandBoxResolver sandboxResolver;

    @Resource(name = "blThemeResolver")
    protected BroadleafThemeResolver themeResolver;

    @Resource(name = "messageSource")
    protected MessageSource messageSource;
    
    @Value("${thymeleaf.threadLocalCleanup.enabled}")
    protected boolean thymeleafThreadLocalCleanupEnabled = true;

    @Value("${" + SITE_ENFORCE_PRODUCTION_WORKFLOW_KEY + ":false}")
    protected boolean enforceSiteProductionWorkflowUpdate = false;
    
    @Resource(name="blEntityExtensionManagers")
    protected Map<String, ExtensionManager<?>> entityExtensionManagers;
    
    @Override
    public void process(WebRequest request) {
        Site site = siteResolver.resolveSite(request);

        WebRequestContext brc = new WebRequestContext();
        brc.setSite(site);
        brc.setWebRequest(request);
        if (site == null) {
            brc.setIgnoreSite(true);
        }
        brc.setAdmin(false);

        if (enforceSiteProductionWorkflowUpdate) {
            brc.getAdditionalProperties().put(SITE_ENFORCE_PRODUCTION_WORKFLOW_KEY, enforceSiteProductionWorkflowUpdate);
        }

        WebRequestContext.setWebRequestContext(brc);

        // Assumes Process
        RequestDTO requestDTO = (RequestDTO) request.getAttribute(REQUEST_DTO_PARAM_NAME, WebRequest.SCOPE_REQUEST);
        if (requestDTO == null) {
            requestDTO = new RequestDTOImpl(request);
        }

        SandBox currentSandbox = sandboxResolver.resolveSandBox(request, site);
        
        // When a user selects to switch his sandbox, we want to invalidate the current session. We'll then redirect the 
        // user to the current URL so that the configured filters trigger again appropriately.
        Boolean reprocessRequest = (Boolean) request.getAttribute(WebRequestContextSupportRequestProcessor.REPROCESS_PARAM_NAME, WebRequest.SCOPE_REQUEST);
        if (reprocessRequest != null && reprocessRequest) {
            LOG.debug("Reprocessing request");
            if (request instanceof ServletWebRequest) {
                HttpServletRequest hsr = ((ServletWebRequest) request).getRequest();
                
                clearBroadleafSessionAttrs(request);
                
                StringBuffer url = hsr.getRequestURL();
                if (hsr.getQueryString() != null) {
                    url.append('?').append(hsr.getQueryString());
                }
                try {
                    ((ServletWebRequest) request).getResponse().sendRedirect(url.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                throw new HaltFilterChainException("Reprocess required, redirecting user");
            }
        }
        
        
        if (currentSandbox != null) {
            SandBoxContext previewSandBoxContext = new SandBoxContext();
            previewSandBoxContext.setSandBoxId(currentSandbox.getId());
            previewSandBoxContext.setPreviewMode(true);
            SandBoxContext.setSandBoxContext(previewSandBoxContext);
        }
        brc.setSandBox(currentSandbox);

        // Note that this must happen after the request context is set up as resolving a theme is dependent on site
        Theme theme = themeResolver.resolveTheme(request);
        brc.setTheme(theme);

        brc.setMessageSource(messageSource);
        brc.setRequestDTO(requestDTO);
        Map<String, Object> ruleMap = (Map<String, Object>) request.getAttribute("blRuleMap", WebRequest.SCOPE_REQUEST);
        if (ruleMap == null) {
            LOG.trace("Creating ruleMap and adding in Locale.");
            ruleMap = new HashMap<String, Object>();
            request.setAttribute("blRuleMap", ruleMap, WebRequest.SCOPE_REQUEST);
        } else {
            LOG.trace("Using pre-existing ruleMap - added by non standard BLC process.");
        }

        String adminUserId = request.getParameter(WebRequestContextSupportFilter.ADMIN_USER_ID_PARAM_NAME);
        if (StringUtils.isNotBlank(adminUserId)) {
            //TODO: Add token logic to secure the admin user id
            brc.setAdminUserId(Long.parseLong(adminUserId));
        }
        brc.getAdditionalProperties().putAll(entityExtensionManagers);
    }

    @Override
    public void postProcess(WebRequest request) {
        ThreadLocalManager.remove();
    }
    
    protected void clearBroadleafSessionAttrs(WebRequest request) {
        request.removeAttribute(BroadleafSandBoxResolver.SANDBOX_ID_VAR, WebRequest.SCOPE_GLOBAL_SESSION);

        // From CustomerStateRequestProcessorImpl, using explicit String because it's out of module
        request.removeAttribute("_blc_anonymousCustomer", WebRequest.SCOPE_GLOBAL_SESSION);
        request.removeAttribute("_blc_anonymousCustomerId", WebRequest.SCOPE_GLOBAL_SESSION);
    }
}

package cn.globalph.openadmin.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.classloader.release.ThreadLocalManager;
import cn.globalph.common.exception.SiteNotFoundException;
import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.sandbox.domain.SandBoxType;
import cn.globalph.common.sandbox.service.SandBoxService;
import cn.globalph.common.site.domain.Site;
import cn.globalph.common.web.AbstractRequestProcessor;
import cn.globalph.common.web.BroadleafSandBoxResolver;
import cn.globalph.common.web.SiteResolver;
import cn.globalph.common.web.WebRequestContext;
import cn.globalph.openadmin.server.security.domain.AdminUser;
import cn.globalph.openadmin.server.security.remote.SecurityVerifier;
import cn.globalph.openadmin.server.security.service.AdminSecurityService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import javax.annotation.Resource;


/**
 * 
 * @author Phillip Verheyden
 * @see {@link cn.globalph.common.web.WebRequestFilter}
 */
@Component("blAdminRequestProcessor")
public class BroadleafAdminRequestProcessor extends AbstractRequestProcessor {

    public static final String SANDBOX_REQ_PARAM = "blSandBoxId";
    
    public static final String ADMIN_ENFORCE_PRODUCTION_WORKFLOW_KEY = "admin.enforce.production.workflow.update";

    protected final Log LOG = LogFactory.getLog(getClass());

    @Resource(name = "blSiteResolver")
    protected SiteResolver siteResolver;

    @Resource(name = "messageSource")
    protected MessageSource messageSource;
    
    @Resource(name = "blSandBoxService")
    protected SandBoxService sandBoxService;

    @Resource(name = "blAdminSecurityRemoteService")
    protected SecurityVerifier adminRemoteSecurityService;
    
    @Resource(name = "blAdminSecurityService")
    protected AdminSecurityService adminSecurityService;
    
    @Value("${" + ADMIN_ENFORCE_PRODUCTION_WORKFLOW_KEY + ":true}")
    protected boolean enforceProductionWorkflowUpdate = true;

    @Override
    public void process(WebRequest request) throws SiteNotFoundException {
        WebRequestContext brc = WebRequestContext.getWebRequestContext();
        if (brc == null) {
            brc = new WebRequestContext();
            WebRequestContext.setWebRequestContext(brc);
        }
        if (brc.getSite() == null) {
            Site site = siteResolver.resolveSite(request);
            brc.setSite(site);
        }
        brc.setWebRequest(request);
        brc.setIgnoreSite(brc.getSite() == null);
        brc.setAdmin(true);

        brc.getAdditionalProperties().put(ADMIN_ENFORCE_PRODUCTION_WORKFLOW_KEY, enforceProductionWorkflowUpdate);
        
        brc.setMessageSource(messageSource);
        
        prepareSandBox(request, brc);
    }

    protected void prepareSandBox(WebRequest request, WebRequestContext brc) {
        AdminUser adminUser = adminRemoteSecurityService.getPersistentAdminUser();
        if (adminUser == null) {
            //clear any sandbox
            request.removeAttribute(BroadleafSandBoxResolver.SANDBOX_ID_VAR, WebRequest.SCOPE_GLOBAL_SESSION);
        } else {
            SandBox sandBox = null;
            if (StringUtils.isNotBlank(request.getParameter(SANDBOX_REQ_PARAM))) {
                Long sandBoxId = Long.parseLong(request.getParameter(SANDBOX_REQ_PARAM));
                sandBox = sandBoxService.retrieveUserSandBoxForParent(adminUser.getId(), sandBoxId);
                if (sandBox == null) {
                    SandBox approvalOrUserSandBox = sandBoxService.retrieveSandBoxById(sandBoxId);
                    if (approvalOrUserSandBox.getSandBoxType().equals(SandBoxType.USER)) {
                        sandBox = approvalOrUserSandBox;
                    } else {
                        sandBox = sandBoxService.createUserSandBox(adminUser.getId(), approvalOrUserSandBox);
                    }
                }
            }

            if (sandBox == null) {
                Long previouslySetSandBoxId = (Long) request.getAttribute(BroadleafSandBoxResolver.SANDBOX_ID_VAR,
                        WebRequest.SCOPE_GLOBAL_SESSION);
                if (previouslySetSandBoxId != null) {
                    sandBox = sandBoxService.retrieveSandBoxById(previouslySetSandBoxId);
                }
            }

            if (sandBox == null) {
                List<SandBox> defaultSandBoxes = sandBoxService.retrieveSandBoxesByType(SandBoxType.DEFAULT);
                if (defaultSandBoxes.size() > 1) {
                    throw new IllegalStateException("Only one sandbox should be configured as default");
                }

                SandBox defaultSandBox;
                if (defaultSandBoxes.size() == 1) {
                    defaultSandBox = defaultSandBoxes.get(0);
                } else {
                    defaultSandBox = sandBoxService.createDefaultSandBox();
                }

                sandBox = sandBoxService.retrieveUserSandBoxForParent(adminUser.getId(), defaultSandBox.getId());
                if (sandBox == null) {
                    sandBox = sandBoxService.createUserSandBox(adminUser.getId(), defaultSandBox);
                }
            }

            // If the user just changed sandboxes, we want to update the database record.
            Long previouslySetSandBoxId = (Long) request.getAttribute(BroadleafSandBoxResolver.SANDBOX_ID_VAR, WebRequest.SCOPE_GLOBAL_SESSION);
            if (previouslySetSandBoxId != null && !sandBox.getId().equals(previouslySetSandBoxId)) {
                adminUser.setLastUsedSandBoxId(sandBox.getId());
                adminUser = adminSecurityService.saveAdminUser(adminUser);
            }

            request.setAttribute(BroadleafSandBoxResolver.SANDBOX_ID_VAR, sandBox.getId(), WebRequest.SCOPE_GLOBAL_SESSION);
            brc.setSandBox(sandBox);
            brc.getAdditionalProperties().put("adminUser", adminUser);
        }
    }

    @Override
    public void postProcess(WebRequest request) {
        ThreadLocalManager.remove();
    }

}

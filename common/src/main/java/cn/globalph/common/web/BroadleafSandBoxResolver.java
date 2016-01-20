package cn.globalph.common.web;

import cn.globalph.common.sandbox.domain.SandBox;
import cn.globalph.common.site.domain.Site;

import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Responsible for determining the SandBox to use for the current request. 
 * SandBox's are used to store a user's changes to products, content-items, etc. 
 * until they are ready to be pushed to production.  
 * 
 * If a request is being served with a SandBox parameter, it indicates that the user
 * wants to see the site as if their changes were applied.
 *
 * @author bpolster
 */
public interface BroadleafSandBoxResolver  {

    public static String SANDBOX_ID_VAR = "blSandboxId";

    /**
     * @deprecated use {@link #resolveSandBox(WebRequest, Site)} instead
     */
    @Deprecated
    public SandBox resolveSandBox(HttpServletRequest request, Site site);

    /**
     * Resolve the sandbox for the given site and request
     * 
     * @param request
     * @param site
     * @return the sandbox for the current request
     */
    public SandBox resolveSandBox(WebRequest request, Site site);

}

package cn.globalph.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Responsible for merging an anonymous cart with the currently logged in user's cart
 * 
 * @see {@link BroadleafAuthenticationSuccessHandler}
 * @deprecated this has been replaced by invoking the merge cart service explicitly within the cart state request processor
 */
@Deprecated
public interface MergeCartProcessor {

    /**
     * Convenience method. This will wrap the given <b>request</b> and <b>response</b> inside of a {@link ServletWebRequest}
     * and forward to {@link #execute(WebRequest, Authentication)}
     * 
     * @param request
     * @param response
     * @param authResult
     */
    public void execute(HttpServletRequest request, HttpServletResponse response, Authentication authResult);
    
    /**
     * Merge the cart owned by the anonymous current session {@link Customer} with the {@link Customer} that has just
     * logged in
     * 
     * @param request
     * @param authResult
     */
    public void execute(WebRequest request, Authentication authResult);

}

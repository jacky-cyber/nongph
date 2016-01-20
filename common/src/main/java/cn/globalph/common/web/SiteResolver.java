package cn.globalph.common.web;

import cn.globalph.common.exception.SiteNotFoundException;
import cn.globalph.common.site.domain.Site;

import org.springframework.web.context.request.WebRequest;

/**
 * Responsible for returning the site for the current request.
 * For a single site installation, this will typically return null.
 */
public interface SiteResolver  {

    /**
     * @see #resolveSite(WebRequest, boolean)
     */
    public Site resolveSite(WebRequest request) throws SiteNotFoundException;

    /**
     * Resolves a site for the given WebRequest. Implementations should throw a {@link SiteNotFoundException}
     * when a site could not be resolved unless the allowNullSite parameter is set to true.
     * 
     * @param request
     * @param allowNullSite
     * @return the resolved {@link Site}
     * @throws SiteNotFoundException
     */
    public Site resolveSite(final WebRequest request, final boolean allowNullSite) throws SiteNotFoundException;
}

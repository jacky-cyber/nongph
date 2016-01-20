package cn.globalph.common.web;

import cn.globalph.common.exception.SiteNotFoundException;
import cn.globalph.common.site.domain.Site;

import org.springframework.web.context.request.WebRequest;

/**
 * Returns null for the Site (typical for non-multi-site implementations of)
 */
public class NullSiteResolver implements SiteResolver {

    @Override
    public Site resolveSite(WebRequest request) {
        return resolveSite(request, false);
    }

    @Override
    public Site resolveSite(WebRequest request, boolean allowNullSite) throws SiteNotFoundException {
        return null;
    }
    
}

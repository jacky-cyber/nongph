package cn.globalph.common.web;

import cn.globalph.common.site.domain.Site;
import cn.globalph.common.site.domain.Theme;

import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Responsible for returning the theme used by Broadleaf Commerce for the current request.
 * For a single site installation, this should return a theme whose path and name are empty string.
 *
 * @author bpolster
 */
public interface BroadleafThemeResolver {
    
    /**
     * 
     * @deprecated Use {@link #resolveTheme(WebRequest)} instead
     */
    @Deprecated
    public Theme resolveTheme(HttpServletRequest request, Site site);
    
    public Theme resolveTheme(WebRequest request);
}

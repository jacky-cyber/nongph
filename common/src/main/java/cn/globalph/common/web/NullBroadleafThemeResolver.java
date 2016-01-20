package cn.globalph.common.web;

import cn.globalph.common.site.domain.Site;
import cn.globalph.common.site.domain.Theme;
import cn.globalph.common.site.domain.ThemeDTO;

import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Returns null for the Site (typical for non-multi-site implementations of
 * Broadleaf Commerce.
 *
 * @author bpolster
 */
public class NullBroadleafThemeResolver implements BroadleafThemeResolver {
    private final Theme theme = new ThemeDTO();

    @Override
    public Theme resolveTheme(HttpServletRequest request, Site site) {
        return resolveTheme(new ServletWebRequest(request));
    }
    
    @Override
    public Theme resolveTheme(WebRequest request) {
        return theme;
    }
}

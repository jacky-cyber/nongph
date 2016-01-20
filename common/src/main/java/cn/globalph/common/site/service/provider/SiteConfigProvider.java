package cn.globalph.common.site.service.provider;

import cn.globalph.common.site.domain.Site;

import java.util.Map;

/**
 * @author Jeff Fischer
 */
public interface SiteConfigProvider {

    public void configSite(Site site);
    
    public void init(Map<String, Object> map);

}

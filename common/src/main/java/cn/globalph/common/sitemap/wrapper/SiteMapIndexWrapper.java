
package cn.globalph.common.sitemap.wrapper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representation the sitemapindex element defined in the schema definition at
 * http://www.sitemaps.org/schemas/sitemap/0.9.
 * 
 * @author bpolster
 */
@XmlRootElement(name = "sitemapindex")
public class SiteMapIndexWrapper {

    private static final long serialVersionUID = 1L;

    private List<SiteMapWrapper> siteMapWrappers = new ArrayList<SiteMapWrapper>();
    
    public List<SiteMapWrapper> getSiteMapWrappers() {
        return siteMapWrappers;
    }
    
    @XmlElement(name = "sitemap")
    public void setSiteMapWrappers(List<SiteMapWrapper> siteMapWrappers) {
        this.siteMapWrappers = siteMapWrappers;
    }
}

package cn.globalph.common.sitemap.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Representation the urlset element defined in the schema definition at
 * http://www.sitemaps.org/schemas/sitemap/0.9.
 * 
 * @author bpolster
 */
@XmlRootElement(name = "urlset")
public class SiteMapURLSetWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<SiteMapURLWrapper> siteMapUrlWrappers = new ArrayList<SiteMapURLWrapper>();
    
    public List<SiteMapURLWrapper> getSiteMapUrlWrappers() {
        return siteMapUrlWrappers;
    }
    
    @XmlElement(name = "url")
    public void setSiteMapUrlWrappers(List<SiteMapURLWrapper> siteMapUrlWrappers) {
        this.siteMapUrlWrappers = siteMapUrlWrappers;
    }
}

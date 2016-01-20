
package cn.globalph.common.sitemap.wrapper;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Representation the sitemap element defined in the schema definition at
 * http://www.sitemaps.org/schemas/sitemap/0.9.
 * 
 * @author bpolster
 */
@XmlRootElement(name = "sitemap")
@XmlType(propOrder = { "loc", "lastmod" })
public class SiteMapWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String loc;

    protected String lastmod;


    public String getLoc() {
        return loc;
    }

    @XmlElement
    public void setLoc(String loc) {
        this.loc = loc;
    }


    public String getLastmod() {
        return lastmod;
    }

    @XmlElement
    public void setLastmod(String lastmod) {
        this.lastmod = lastmod;
    }
}


package cn.globalph.common.sitemap.wrapper;

import cn.globalph.common.sitemap.service.type.SiteMapChangeFreqType;
import cn.globalph.common.sitemap.service.type.SiteMapPriorityType;
import cn.globalph.common.util.FormatUtil;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Representation of SiteMapURLEntry that can be used to generate an XML element.
 * 
 * @author bpolster
 */
@XmlRootElement(name = "url")
@XmlType(propOrder = { "loc", "lastmod", "changefreq", "priority" })
public class SiteMapURLWrapper implements Serializable {

    private static final long serialVersionUID = 1L;   

    protected String loc;

    protected String lastmod;

    protected String changefreq;

    protected String priority;

    public void setLastModDate(Date lastModDate) {
        if (lastModDate != null) {
            lastmod = FormatUtil.formatDateUsingW3C(lastModDate);
        } else {
            lastmod = FormatUtil.formatDateUsingW3C(new Date());
        }
    }

    public void setPriorityType(SiteMapPriorityType priorityType) {
        if (priorityType != null) {
            setPriority(priorityType.getType());
        }
    }

    public void setChangeFreqType(SiteMapChangeFreqType changeFreqType) {
        if (changeFreqType != null) {
            setChangefreq(changeFreqType.getFriendlyType());
        }
    }

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

    public String getChangefreq() {
        return changefreq;
    }

    @XmlElement
    public void setChangefreq(String changefreq) {
        this.changefreq = changefreq;
    }
    
    public String getPriority() {
        return priority;
    }

    @XmlElement
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
}

package cn.globalph.b2c.product.domain.wrap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "media")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class MediaWrap{

    /**
     * This allows us to control whether the URL should / can be overwritten, for example by the static asset service.
     */
    @XmlTransient
    protected boolean allowOverrideUrl = true;

    @XmlElement
    protected Long id;

    @XmlElement
    protected String title;

    @XmlElement
    protected String url;

    @XmlElement
    protected String altText;
    
    @XmlElement
    protected String tags;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAltText() {
		return altText;
	}

	public void setAltText(String altText) {
		this.altText = altText;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public boolean isAllowOverrideUrl() {
        return allowOverrideUrl;
    }

    public void setAllowOverrideUrl(boolean allow) {
        this.allowOverrideUrl = allow;
    }

    /**
     * Call this only if allowOverrideUrl is true, and only AFTER you call wrap.
     * @param url
     */
    public void setUrl(String url) {
        if (allowOverrideUrl) {
            this.url = url;
        }
    }
}

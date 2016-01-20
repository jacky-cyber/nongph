package cn.globalph.cms.url.domain;

import cn.globalph.cms.url.type.URLRedirectType;


/**
 * A bean representation of a URLHandler
 * @author bpolster
 *
 */
public class URLHandlerDTO implements URLHandler {

    private static final long serialVersionUID = 1L;
    protected Long id = null;
    protected String incomingURL = "";
    protected String newURL;
    protected String urlRedirectType;

    public URLHandlerDTO(String newUrl, URLRedirectType redirectType) {
        setUrlRedirectType(redirectType);
        setNewURL(newUrl);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIncomingURL() {
        return incomingURL;
    }

    public void setIncomingURL(String incomingURL) {
        this.incomingURL = incomingURL;
    }

    public String getNewURL() {
        return newURL;
    }

    public void setNewURL(String newURL) {
        this.newURL = newURL;
    }

    @Override
    public URLRedirectType getUrlRedirectType() {
        return URLRedirectType.getInstance(urlRedirectType);
    }

    @Override
    public void setUrlRedirectType(URLRedirectType redirectType) {
        this.urlRedirectType = redirectType.getType();
    }
}

package cn.globalph.common.media.domain;

/**
 * A null safe media object.
 */
public class MediaDto implements Media {

    private static final long serialVersionUID = 1L;

    protected long id;
    protected String url = "";
    protected String title = "";
    protected String altText = "";
    protected String tags = "";

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getAltText() {
        return altText;
    }

    @Override
    public void setAltText(String altText) {
        this.altText = altText;
    }
    
    @Override
    public String getTags() {
        return tags;
    }
    
    @Override
    public void setTags(String tags) {
        this.tags = tags;
    }
}

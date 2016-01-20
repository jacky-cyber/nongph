package cn.globalph.common.media.domain;

import java.io.Serializable;

public interface Media extends Serializable {

    public Long getId();

    public void setId(Long id);

    public String getUrl();

    public void setUrl(String url);
    
    public String getTitle();

    public void setTitle(String title);

    public String getAltText();

    public void setAltText(String altText);
    
    public String getTags();

    public void setTags(String tags);

}

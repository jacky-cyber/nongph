package cn.globalph.openadmin.web.form.component;

import cn.globalph.common.media.domain.Media;
import cn.globalph.openadmin.web.form.entity.Field;

/**
 * @author bpolster
 */
public class MediaField extends Field {

    protected Media media;
    protected String height;

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

}

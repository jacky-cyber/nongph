package cn.globalph.common.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import cn.globalph.common.presentation.AdminPresentation;

@Embeddable
public class PreviewStatus implements Serializable, Previewable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "IS_PREVIEW")
    @AdminPresentation(excluded = true)
    protected Boolean isPreview;

    @Override
    public Boolean getPreview() {
        return isPreview;
    }

    @Override
    public void setPreview(Boolean preview) {
        isPreview = preview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreviewStatus)) return false;

        PreviewStatus that = (PreviewStatus) o;

        if (isPreview != null ? !isPreview.equals(that.isPreview) : that.isPreview != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return isPreview != null ? isPreview.hashCode() : 0;
    }
}

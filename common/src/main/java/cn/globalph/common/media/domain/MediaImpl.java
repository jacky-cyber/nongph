package cn.globalph.common.media.domain;

import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.client.SupportedFieldType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="NPH_MEDIA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true)
})
public class MediaImpl implements Media {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "MediaId")
    @GenericGenerator(
        name="MediaId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="MediaImpl"),
            @Parameter(name="entity_name", value="cn.globalph.common.media.domain.MediaImpl")
        }
    )
    @Column(name = "MEDIA_ID")
    protected Long id;

    @Column(name = "URL", nullable = false)
    @Index(name="MEDIA_URL_INDEX", columnNames={"URL"})
    @AdminPresentation(friendlyName = "MediaImpl_Media_Url", order = 1, prominent = true,
            fieldType = SupportedFieldType.ASSET_LOOKUP)
    protected String url;
    
    @Column(name = "TITLE")
    @Index(name="MEDIA_TITLE_INDEX", columnNames={"TITLE"})
    @AdminPresentation(friendlyName = "MediaImpl_Media_Title", order=2, prominent=true)
    protected String title;
    
    @Column(name = "ALT_TEXT")
    @AdminPresentation(friendlyName = "MediaImpl_Media_Alt_Text", order=3, prominent=true)
    protected String altText;
    
    @Column(name = "TAGS")
    @AdminPresentation(friendlyName = "MediaImpl_Media_Tags")
    protected String tags;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((altText == null) ? 0 : altText.hashCode());
        result = prime * result + ((tags == null) ? 0 : tags.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MediaImpl other = (MediaImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }
        
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (altText == null) {
            if (other.altText != null)
                return false;
        } else if (!altText.equals(other.altText))
            return false;
        if (tags == null) {
            if (other.tags != null)
                return false;
        } else if (!tags.equals(other.tags))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

}
